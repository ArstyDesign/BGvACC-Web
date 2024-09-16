package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.ATCApplicationModel;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class VatsimATCController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/atc/career-application")
  public String prepareATCCareerApplication(Model model) {

    model.addAttribute("pageTitle", getMessage("atcapplication.title"));

    model.addAttribute("aam", new ATCApplicationModel());

    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "application");

    return "atc/career-application";
  }

  @PostMapping("/atc/career-application")
  public String sendATCCareerApplication(@ModelAttribute("aam") @Valid ATCApplicationModel aam,
          BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {

    log.debug("TEsting");

    if (bindingResult.hasErrors()) {

      model.addAttribute("pageTitle", getMessage("atcapplication.title"));

      model.addAttribute("page", "atc");
      model.addAttribute("subpage", "application");

      return "atc/career-application";
    }

    return "redirect:/atc/career-application";
  }
}
