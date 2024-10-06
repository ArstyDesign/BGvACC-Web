package com.bgvacc.web.utils;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class Utils {

  public static String getIpAddress(HttpServletRequest request) {

    String hostName = request.getHeader("X-FORWARDED-FOR");

    if (hostName == null || "".equals(hostName)) {
      hostName = request.getRemoteAddr();
    }

    return hostName;
  }
}
