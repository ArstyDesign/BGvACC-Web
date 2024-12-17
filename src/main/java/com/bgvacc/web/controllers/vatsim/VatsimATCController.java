package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.ATCApplicationModel;
import com.bgvacc.web.requests.atc.ATCApplicationRequest;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.services.MailService;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
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
  public String prepareATCCareerApplication(Model model, HttpServletRequest request) {

    model.addAttribute("pageTitle", getMessage("atc.application.title"));

    ATCApplicationModel atcApplicationModel = new ATCApplicationModel();

    LoggedUser loggedUser = getLoggedUser(request);

    model.addAttribute("isLoggedIn", loggedUser != null);

    if (loggedUser != null) {
      atcApplicationModel.setFirstName(loggedUser.getNames().getFirstName());
      atcApplicationModel.setLastName(loggedUser.getNames().getLastName());
      atcApplicationModel.setEmail(loggedUser.getEmail());
      atcApplicationModel.setCid(loggedUser.getCid());
    }

    model.addAttribute("aam", atcApplicationModel);

    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "application");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.application", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

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

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.application", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

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

    if (isSent) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("atc.application.form.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("atc.application.form.error"));
    }

    return "redirect:/atc/career-application";
  }

  @GetMapping("/atc/information")
  public String getAtcInformation(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.information.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "information");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.information", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc/information";
  }

  @GetMapping("/atc/training-files")
  public String getTrainingFiles(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.trainingfiles.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "training-files");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.trainingfiles", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc/training-files";
  }

  @GetMapping("/atc/letter-of-agreements")
  public String getLetterOfAgreements(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.letterofagreements.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "letter-of-agreements");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.letterofagreements", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc/letter-of-agreements";
  }

  @GetMapping("/atc/stations")
  public String getStations(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.stations.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "stations");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.stations", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc/stations";
  }

  @GetMapping("/atc/downloads")
  public String getDownloads(Model model) {

    model.addAttribute("pageTitle", getMessage("atc.downloads.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "downloads");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.atc.atcdownloads", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc/downloads";
  }
}
