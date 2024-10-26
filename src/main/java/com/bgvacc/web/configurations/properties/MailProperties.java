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
@PropertySource("classpath:mail.properties")
public class MailProperties {

  @Value("${mail.host}")
  private String host;

  @Value("${mail.port}")
  private Integer port;

  @Value("${mail.protocol}")
  private String protocol;

  @Value("${mail.username}")
  private String username;

  @Value("${mail.password}")
  private String password;

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
