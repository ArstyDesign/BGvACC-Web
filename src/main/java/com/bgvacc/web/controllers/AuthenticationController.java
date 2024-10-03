package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.authentication.LoginModel;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class AuthenticationController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/login")
  public String prepareLogin(Model model) {

    model.addAttribute("lm", new LoginModel("events@bgvacc.com", "1234"));

    model.addAttribute("page", "login");

    return "authentication/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("lm") @Valid LoginModel login, BindingResult bindingResult, Model model) {

    log.debug(login.toString());

    if (bindingResult.hasErrors()) {

      model.addAttribute("page", "login");

      return "authentication/login";
    }

    return "redirect:/login";
  }
  
  @GetMapping("/vatsim/auth/callback")
  public String vatsimAuthCallback(Model model) {
    
    return "authentication/vatsim/callback";
  }
}
