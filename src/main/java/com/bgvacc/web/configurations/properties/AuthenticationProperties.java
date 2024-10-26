package com.bgvacc.web.configurations.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
@PropertySource("classpath:authentication.properties")
public class AuthenticationProperties {

  @Value("${vatsim.client-secret}")
  private String vatsimClientSecret;

  @Value("${vateud.api.key}")
  private String vatEudApiKey;

  @Value("${db.host}")
  private String dbHost;

  @Value("${db.port}")
  private String dbPort;

  @Value("${db.schema}")
  private String dbSchema;

  @Value("${db.username}")
  private String dbUsername;

  @Value("${db.password}")
  private String dbPassword;

  public String getVatsimClientSecret() {
    return vatsimClientSecret;
  }

  public String getVatEudApiKey() {
    return vatEudApiKey;
  }

  public String getDbHost() {
    return dbHost;
  }

  public String getDbPort() {
    return dbPort;
  }

  public String getDbSchema() {
    return dbSchema;
  }

  public String getDbUsername() {
    return dbUsername;
  }

  public String getDbPassword() {
    return dbPassword;
  }
}
