package com.bgvacc.web.controllers.portal;

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
public class PortalController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/portal/dashboard")
  public String portalDashboard(Model model) {
    
    model.addAttribute("pageTitle", getMessage("pilots.airspace.title"));
    model.addAttribute("page", "dashboard");

    return "portal/dashboard";
  }
}
