package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.portal.profile.settings.ChangePasswordModel;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.services.ControllerOnlineLogService;
import com.bgvacc.web.services.UserService;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalProfileController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final VatEudCoreApi vatEudCoreApi;

  private final ControllerOnlineLogService controllerOnlineLogService;

  private final UserService userService;

  @GetMapping("/portal/profile")
  public String profile(HttpServletRequest request, Model model) {

    String cidString = getLoggedUserCid(request);

    Long cid = Long.valueOf(getLoggedUserCid(request));

    VatEudUser member = vatEudCoreApi.getMemberDetails(cid);

    model.addAttribute("member", member);

//    List<ControllerOnlineLogResponse> sessions = controllerOnlineLogService.getControllerLastOnlineSessions(cidString, 5, false);
//
//    model.addAttribute("sessions", sessions);
    model.addAttribute("pageTitle", getLoggedUser(request).getNames().getFullName() + " - " + cid);
    model.addAttribute("page", "profile");
    model.addAttribute("subpage", "profile");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.profile", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/profile/profile";
  }

  @GetMapping("/portal/profile/settings")
  public String profileSettings(Model model) {

    model.addAttribute("pageTitle", getMessage("portal.profile.settings.title"));

    model.addAttribute("page", "profile");
    model.addAttribute("subpage", "settings");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.profile", null, LocaleContextHolder.getLocale()), "/portal/profile"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.settings", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/profile/settings/general";
  }

  @GetMapping("/portal/profile/settings/change-password")
  public String prepareChangePassword(Model model) {

    model.addAttribute("cpm", new ChangePasswordModel());

    model.addAttribute("pageTitle", getMessage("portal.profile.settings.changepassword.title"));

    model.addAttribute("page", "profile");
    model.addAttribute("subpage", "settings");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.profile", null, LocaleContextHolder.getLocale()), "/portal/profile"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.settings", null, LocaleContextHolder.getLocale()), "/portal/profile/settings"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.changepassword", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/profile/settings/change-password";
  }

  @PostMapping("/portal/profile/settings/change-password")
  public String changePassword(@ModelAttribute("cpm") @Valid ChangePasswordModel cpm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

    if (bindingResult.hasErrors()) {

      model.addAttribute("pageTitle", getMessage("portal.profile.settings.changepassword.title"));

      model.addAttribute("page", "profile");
      model.addAttribute("subpage", "settings");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.profile", null, LocaleContextHolder.getLocale()), "/portal/profile"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.settings", null, LocaleContextHolder.getLocale()), "/portal/profile/settings"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.changepassword", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "portal/profile/settings/change-password";
    }

    LoggedUser loggedUser = getLoggedUser(request);

    if (!userService.doPasswordMatch(loggedUser.getCid(), cpm.getCurrentPassword())) {
      log.error("Password does not match");

      bindingResult.rejectValue("currentPassword", "portal.profile.settings.changepassword.error.currentpasswordnomatch");
    }

    if (!cpm.getNewPassword().equals(cpm.getConfirmPassword())) {

      log.error("The two passwords do not match");

      bindingResult.rejectValue("newPassword", "portal.profile.settings.changepassword.error.newconfirmpasswordsnomatch");
      bindingResult.rejectValue("confirmPassword", "portal.profile.settings.changepassword.error.newconfirmpasswordsnomatch");
    }

    if (bindingResult.hasErrors()) {

      model.addAttribute("pageTitle", getMessage("portal.profile.settings.changepassword.title"));

      model.addAttribute("page", "profile");
      model.addAttribute("subpage", "settings");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.profile", null, LocaleContextHolder.getLocale()), "/portal/profile"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.settings", null, LocaleContextHolder.getLocale()), "/portal/profile/settings"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.main.profile.changepassword", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "portal/profile/settings/change-password";
    }

    log.info("Changing password for user with CID '" + loggedUser.getCid() + "'.");

    boolean isPasswordChanged = userService.changePassword(loggedUser.getCid(), cpm.getNewPassword());

    if (isPasswordChanged) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.profile.settings.changepassword.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.profile.settings.changepassword.error"));
    }

    return "redirect:/portal/profile/settings";
  }
}
