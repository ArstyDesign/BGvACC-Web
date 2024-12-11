package com.bgvacc.web.enums;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public enum AuthenticationError {

  BAD_CREDENTIALS(40001, "Bad credentials"),
  USER_NOT_FOUND(40002, "User not found");

  private final Integer errorCode;
  private final String errorDescr;

  private AuthenticationError(Integer errorCode, String errorDescr) {
    this.errorCode = errorCode;
    this.errorDescr = errorDescr;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public String getErrorDescr() {
    return errorDescr;
  }
}
