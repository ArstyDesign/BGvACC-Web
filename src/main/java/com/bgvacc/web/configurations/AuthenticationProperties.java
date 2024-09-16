package com.bgvacc.web.configurations;

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

  @Value("${api.key}")
  private String apiKey;

  public String getAPIKey() {
    return apiKey;
  }
}
