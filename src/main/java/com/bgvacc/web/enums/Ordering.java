package com.bgvacc.web.enums;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public enum Ordering {

  ASCENDING("ASC"),
  DESCENDING("DESC");

  private final String order;

  Ordering(String order) {
    this.order = order;
  }

  public String getOrder() {
    return order;
  }
}
