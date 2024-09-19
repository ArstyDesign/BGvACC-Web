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

    model.addAttribute("pageTitle", getMessage("atc.application.title"));

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

      model.addAttribute("pageTitle", getMessage("atc.application.title"));

      model.addAttribute("page", "atc");
      model.addAttribute("subpage", "application");

      return "atc/career-application";
    }

    return "redirect:/atc/career-application";
  }

  @GetMapping("/atc/training-files")
  public String getTrainingFiles(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.trainingfiles.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "training-files");

    return "atc/training-files";
  }
  
  @GetMapping("/atc/stations")
  public String getStations(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.stations.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "stations");

    return "atc/stations";
  }
  
  @GetMapping("/atc/downloads")
  public String getDownloads(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.downloads.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "downloads");

    return "atc/downloads";
  }
}
