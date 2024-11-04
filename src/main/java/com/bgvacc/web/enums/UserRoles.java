package com.bgvacc.web.enums;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public enum UserRoles {

  SYS_ADMIN("SYS_ADMIN"),
  STAFF_DIRECTOR("STAFF_DIRECTOR"),
  STAFF_EVENTS("STAFF_EVENTS"),
  STAFF_TRAINING("STAFF_TRAINING"),
  ATC_S1("ATC_S1"),
  ATC_S2("ATC_S2"),
  ATC_S3("ATC_S3"),
  ATC_C1("ATC_C1"),
  ATC_C3("ATC_C3"),
  ATC_I1("ATC_I1"),
  ATC_I3("ATC_I3"),
  USER("USER"),
  ATC_TRAINING("ATC_TRAINING");

  private String roleName;

  private UserRoles(String roleName) {
    this.roleName = roleName.toUpperCase();
  }

  /**
   * @return the role name
   */
  public String getRoleName() {
    return roleName;
  }

  /**
   * @param roleName the role name to be set
   */
  public void setRoleName(String roleName) {
    this.roleName = roleName.toUpperCase();
  }
}
