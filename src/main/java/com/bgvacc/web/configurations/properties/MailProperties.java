package com.bgvacc.web.configurations.properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
//@PropertySource("classpath:mail.properties")
public class MailProperties {

  public String getHost() {
    return getProperty("config/mail/host");
  }

  public Integer getPort() {
    String value = getProperty("config/mail/port");
    return Integer.valueOf(value);
  }

  public String getProtocol() {
    return getProperty("config/mail/protocol");
  }

  public String getUsername() {
    return getProperty("config/mail/username");
  }

  public String getPassword() {
    return getProperty("config/mail/password");
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
