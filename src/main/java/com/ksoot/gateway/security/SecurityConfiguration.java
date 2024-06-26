package com.ksoot.gateway.security;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

/**
 * @author Rajveer Singh
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {ActuatorEndpointProperties.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity // Allow method annotations like @PreAuthorize
class SecurityConfiguration {

  private final String[] SWAGGER_URLS =
      new String[] {
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/swagger-ui.*",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/webjars/**"
      };

  private boolean actuatorBypassSecurity = true;

  private final ServerAuthenticationEntryPoint authenticationEntryPoint;

  private final ServerAccessDeniedHandler accessDeniedHandler;

  private final ActuatorEndpointProperties actuatorEndpointProperties;

  SecurityConfiguration(
      @Nullable final ServerAuthenticationEntryPoint authenticationEntryPoint,
      @Nullable final ServerAccessDeniedHandler accessDeniedHandler,
      @Nullable final ActuatorEndpointProperties actuatorEndpointProperties) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.accessDeniedHandler = accessDeniedHandler;
    this.actuatorEndpointProperties = actuatorEndpointProperties;
  }

  @Bean
  SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(
            (exchanges) ->
                exchanges
                    .pathMatchers(
                        this.actuatorBypassSecurity && this.actuatorEndpointProperties != null
                            ? ArrayUtils.addAll(
                                SWAGGER_URLS, this.actuatorEndpointProperties.getPaths())
                            : SWAGGER_URLS)
                    .permitAll()
                    //                    .pathMatchers("/problems/**")
                    //                    .permitAll()
                    .anyExchange()
                    .authenticated())
        .oauth2Login(Customizer.withDefaults());
    return http.build();
  }
}
