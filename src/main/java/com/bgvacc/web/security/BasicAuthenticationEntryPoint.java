package com.bgvacc.web.security;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {

//    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    response.sendRedirect(request.getContextPath() + "/login");
  }
}
