package com.ksoot.gateway.security;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "management.endpoints.web", ignoreInvalidFields = true)
@Valid
public class ActuatorEndpointProperties {

  private static final String SLASH = "/";

  private String basePath = "/actuator";

  private Exposure exposure = new Exposure();

  private Map<String, String> pathMapping = Collections.emptyMap();

  static final List<String> ALL_ENDPOINTS =
      Arrays.asList(
          "auditevents",
          "beans",
          "caches",
          "conditions",
          "configprops",
          "env",
          "flyway",
          "health",
          "heapdump",
          "httptrace",
          "info",
          "integrationgraph",
          "liquibase",
          "logfile",
          "loggers",
          "mappings",
          "metrics",
          "prometheus",
          "scheduledtasks",
          "sessions",
          "shutdown",
          "threaddump");

  public String[] getPaths() {
    List<String> exposedEndpoints = null;
    if (this.exposure.getInclude().contains("*")) {
      exposedEndpoints = ActuatorEndpointProperties.ALL_ENDPOINTS;
    } else {
      exposedEndpoints = this.exposure.getInclude();
    }

    if (SLASH.equals(this.basePath)) {
      return exposedEndpoints.stream()
          .map(path -> SLASH + this.pathMapping.getOrDefault(path, path) + "/**")
          .toArray(String[]::new);
    } else {
      return new String[] {this.basePath + "/**"};
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class Exposure {

    private List<String> include = ALL_ENDPOINTS;
  }
}
