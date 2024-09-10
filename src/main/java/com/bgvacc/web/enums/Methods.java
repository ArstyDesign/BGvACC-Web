package com.bgvacc.web.enums;

import org.springframework.http.HttpMethod;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public enum Methods {

  GET("GET", HttpMethod.GET),
  POST("POST", HttpMethod.POST),
  PUT("PUT", HttpMethod.PUT),
  DELETE("DELETE", HttpMethod.DELETE);

  private String method;
  private final HttpMethod httpMethod;

  private Methods(String method, HttpMethod httpMethod) {
    this.method = method;
    this.httpMethod = httpMethod;
  }

  /**
   * @return the method name
   */
  public String getMethod() {
    return method.toUpperCase();
  }

  /**
   * @param method the method name to be set
   */
  public void setMethod(String method) {
    this.method = method.toUpperCase();
  }

  public HttpMethod getHttpMethod() {
    return httpMethod;
  }
}
