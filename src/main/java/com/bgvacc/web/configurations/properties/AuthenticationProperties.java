package com.bgvacc.web.configurations.properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class AuthenticationProperties {

  public String getVatsimClientSecret() {
    return getProperty("config/vatsim/client-secret");
  }

  public String getVatEudApiKey() {
    return getProperty("config/vateud/api-key");
  }

  public String getDbHost() {
    return getProperty("config/db/host");
  }

  public String getDbPort() {
    return getProperty("config/db/port");
  }

  public String getDbSchema() {
    return getProperty("config/db/schema");
  }

  public String getDbUsername() {
    return getProperty("config/db/username");
  }

  public String getDbPassword() {
    return getProperty("config/db/password");
  }

  private String getProperty(String propertyName) {
    try {
      Context ctx = new InitialContext();
      return (String) ctx.lookup("java:comp/env/" + propertyName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch JNDI resource", e);
    }
  }
}
