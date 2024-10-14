package com.bgvacc.web.responses.authentication;

import java.io.Serializable;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class AuthenticationSuccessResponse implements Serializable {

  private String redirectUrl;

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }
}
