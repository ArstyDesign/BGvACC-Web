package com.bgvacc.web.services;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface SystemService {

  String getLocale(HttpServletRequest request);

  Object getSessionAttribute(HttpServletRequest request, String attributeName);
}
