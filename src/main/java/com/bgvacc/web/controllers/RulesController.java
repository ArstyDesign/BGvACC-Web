package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
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
public class RulesController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/rules/privacy-policy")
  public String getPrivacyPolicy(Model model) {

    model.addAttribute("pageTitle", "Privacy Policy for VATSIM BULGARIA");
    model.addAttribute("page", "rules");
    model.addAttribute("subpage", "privacy-policy");

    return "rules/privacy-policy";
  }
}
