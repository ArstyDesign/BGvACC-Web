package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
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

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.rules", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.rules.privacypolicy", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "rules/privacy-policy";
  }
}
