package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class BGVErrorController extends Base implements ErrorController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/customError")
  public String handleError(HttpServletRequest request, Model model) {

    Object originalUrl = request.getAttribute("javax.servlet.error.request_uri");

    boolean isPortal = false;

    // Можеш да логваш или да извършиш друга логика тук
    if (originalUrl != null) {
//      log.debug("Error accessing URL: " + originalUrl.toString());

      if (originalUrl.toString().contains("portal")) {
        isPortal = true;
      }
    } else {
      log.debug("Original URL cannot be found.");
    }

    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

//    log.debug("URI: " + request.getRequestURI());
//    log.debug("URL: " + request.getRequestURL());
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());

//      log.debug("Status code: " + statusCode);
      if (statusCode == HttpStatus.FORBIDDEN.value() || statusCode == HttpStatus.UNAUTHORIZED.value()) {
        log.debug("Not allowed!");

        if (isLoggedIn()) {
          if (isPortal) {
            return "redirect:/portal/403";
          }
        }

        return "redirect:/403";

      } else if (statusCode == HttpStatus.NOT_FOUND.value()) {

        if (isLoggedIn()) {
          if (isPortal) {
            return "redirect:/portal/404";
          }
        }

        return "redirect:/404";

      } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
        log.debug("Method not allowed");
        return "redirect:/";
      } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {

        if (isLoggedIn()) {
          if (isPortal) {
            return "redirect:/portal/500";
          }
        }

        return "redirect:/500";
      }
    }

    return "redirect:/500";
  }

  @GetMapping("/403")
  public String error403(Model model) {

    model.addAttribute("errorCode", "403");

//    if (hasSpecialRole()) {
//      return "errors/adminError";
//    }
//    if (!isLoggedIn()) {
//      return "errors/error_anon";
//    }
    return "errors/error";
  }

  @GetMapping("/404")
  public String error404(Model model) {

    model.addAttribute("errorCode", "404");

//    if (hasSpecialRole()) {
//      return "errors/adminError";
//    }
//    if (!isLoggedIn()) {
//      return "errors/error_anon";
//    }
    return "errors/error";
  }

  @GetMapping("/500")
  public String error500(Model model) {

    model.addAttribute("errorCode", "500");

//    if (hasSpecialRole()) {
//      return "errors/adminError";
//    }
//    if (!isLoggedIn()) {
//      return "errors/error_anon";
//    }
    return "errors/error";
  }

//  @Override
//  public String getErrorPath() {
//    return null;
//  }
}
