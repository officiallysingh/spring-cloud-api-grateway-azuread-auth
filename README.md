# Read Me First
Demo Spring cloud Gateway and applying Authentication using Azure AD. Demo downstream service available at [`spring-boot-web-auth-demo`](https://github.com/officiallysingh/spring-boot-web-auth-demo)

## Guides
The following guides illustrate how to use Spring cloud Gateway and applying Authentication using Azure AD:

* [Gateway](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)
* [Securing a Java Web App with the Spring Boot Starter for Azure Active Directory](https://aka.ms/spring/msdocs/aad)
* [Azure Active Directory](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#spring-security-with-azure-active-directory)

## Gateway Routes configurations
* `uri` of downstream service. 
* Path predicate
* `TokenRelay` to forward Access Token received from Azure AD as `Authorization` header to downstream services.  

```yaml
spring:
  cloud:
    #------------------------- API Gateway configuration -------------------------
    gateway:
      routes:
        - id: book-service
          uri: http://localhost:8081
          predicates:
            - Path=/api/**
          filters:
            - TokenRelay=
```

## Spring boot Azure AD configurations

Update `tenant-id`, `client-id` and `client-secret` properties for your Azure AD settings, others remain the same.

```yaml
spring:
  cloud:
    #------------------------- Azure AD configuration -------------------------
    azure:
      active-directory:
        enabled: true
        profile:
          tenant-id: ${AZURE_AD_TENANT_ID}
        credential:
          client-id: ${AZURE_AD_CLIENT_ID}
          client-secret: ${AZURE_AD_CLIENT_SECRET}

  security:
    oauth2:
      client:
        registration:
          azure:
            client-id: ${spring.cloud.azure.active-directory.credential.client-id}
            client-secret: ${spring.cloud.azure.active-directory.credential.client-secret}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: openid, profile, email
        provider:
          azure:
            authorization-uri: https://login.microsoftonline.com/${spring.cloud.azure.active-directory.profile.tenant-id}/oauth2/v2.0/authorize
            token-uri: https://login.microsoftonline.com/${spring.cloud.azure.active-directory.profile.tenant-id}/oauth2/v2.0/token
            user-info-uri: https://graph.microsoft.com/oidc/userinfo
            jwk-set-uri: https://login.microsoftonline.com/${spring.cloud.azure.active-directory.profile.tenant-id}/discovery/v2.0/keys
            issuer-uri: https://login.microsoftonline.com/${spring.cloud.azure.active-directory.profile.tenant-id}/v2.0
```

## Run
Run the main class [`SpringCloudApiGratewayAzureADAuthApplication`](src/main/java/com/ksoot/gateway/SpringCloudApiGratewayAzureADAuthApplication.java)
and access the URL http://localhost:8080/api/hello in browser.  
It should redirect you to Azure AD login page, fill in Username and Password.  
After successful authentication, you will get following response from downstream service.

`Hello <You username> from Downstream stream service`