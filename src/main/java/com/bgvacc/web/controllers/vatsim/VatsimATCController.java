package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.ATCApplicationModel;
import com.bgvacc.web.requests.atc.ATCApplicationRequest;
import com.bgvacc.web.services.MailService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class VatsimATCController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MailService mailService;

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

    log.debug("Sending new ATC training application");

    if (bindingResult.hasErrors()) {

      model.addAttribute("pageTitle", getMessage("atc.application.title"));

      model.addAttribute("page", "atc");
      model.addAttribute("subpage", "application");

      return "atc/career-application";
    }

    ATCApplicationRequest aar = new ATCApplicationRequest();
    aar.setFirstName(aam.getFirstName());
    aar.setLastName(aam.getLastName());
    aar.setCid(aam.getCid());
    aar.setEmail(aam.getEmail());
    aar.setCurrentRating(aam.getCurrentRating());
    aar.setReason(aam.getReason());

    boolean isSent = mailService.sendNewATCTrainingApplicationMail(aar);

    return "redirect:/atc/career-application";
  }

  @GetMapping("/atc/information")
  public String getAtcInformation(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.information.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "information");

    return "atc/information";
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
