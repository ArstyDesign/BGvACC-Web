package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class ProfileController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/portal/profile")
  public String profile(HttpServletRequest request, Model model) {

    model.addAttribute("pageTitle", getLoggedUser(request).getFullName());
    model.addAttribute("page", "profile");
    model.addAttribute("subpage", "profile");

    return "portal/profile/profile";
  }
}
