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
public class EnvironmentProperties {

  public String getEnvironment() {
    return getProperty("config/environment/type");
  }

  public String getTestEmailReceiver() {

    try {
      return getProperty("config/environment/test-email-receiver");
    } catch (RuntimeException e) {
      return null;
    }
  }

  public String getBaseUrl() {
    return getProperty("config/environment/base-url");
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
