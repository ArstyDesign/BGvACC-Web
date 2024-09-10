package com.bgvacc.web.utils;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogRequest implements Serializable {

  private String method;
  private String url;

  @Override
  public String toString() {
    return method.toUpperCase() + " " + url;
  }
}
