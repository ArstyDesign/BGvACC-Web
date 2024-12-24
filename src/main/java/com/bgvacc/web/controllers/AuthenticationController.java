package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.enums.AuthenticationError;
import com.bgvacc.web.exceptions.GeneralErrorException;
import com.bgvacc.web.models.authentication.*;
import com.bgvacc.web.responses.authentication.*;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.services.*;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
public class AuthenticationController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthenticationService authenticationService;

  private final AuthenticationManager authenticationManager;

  private final UserService userService;

  private final MailService mailService;

  @GetMapping("/login")
  public String prepareLogin(Model model) {

    model.addAttribute("lm", new LoginModel());

    model.addAttribute("page", "login");

    return "authentication/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("lm") @Valid LoginModel login, BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res, RedirectAttributes redirectAttributes, Model model) throws IOException, ServletException, Exception {

    if (bindingResult.hasErrors()) {

      model.addAttribute("page", "login");

      return "authentication/login";
    }

    AuthAttemptResponse authAttempt = authenticationService.authenticate(login);

    if (authAttempt.hasErrors()) {

      if (authAttempt.getError() == AuthenticationError.BAD_CREDENTIALS) {
        bindingResult.rejectValue("password", "login.error.badcredentials");

      } else if (authAttempt.getError() == AuthenticationError.USER_NOT_FOUND) {
        bindingResult.rejectValue("cidEmail", "login.error.usernotfound");
      }
    }

    if (bindingResult.hasErrors()) {

      model.addAttribute("page", "login");

      return "authentication/login";
    }

    UserResponse user = userService.getUser(login.getCidEmail());

    if (!user.getIsActive()) {
      return "redirect:/activate-user-account?token=" + user.getPasswordResetToken();
    }

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    Authentication authentication;
    try {

      UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login.getCidEmail(), login.getPassword());
      WebAuthenticationDetails details = new WebAuthenticationDetails(req);
      authReq.setDetails(details);
      authentication = authenticationManager.authenticate(authReq);
    } catch (GeneralErrorException e) {

      log.error("Erorr: " + e);

      return "login";
    }

    AuthenticationSuccessResponse authResponse = authenticationService.saveAuthentication(req, res, authentication);

    return "redirect:" + authResponse.getRedirectUrl();
  }

  @GetMapping("/vatsim/auth/callback")
  public String vatsimAuthCallback(Model model) {

    return "authentication/vatsim/callback";
  }

  @GetMapping("/forgot-password")
  public String prepareForgotPassword(Model model) {

    model.addAttribute("fpm", new ForgotPasswordModel());

    return "authentication/forgot-password";
  }

  @PostMapping("/forgot-password")
  public String forgotPassword(@ModelAttribute("fpm") @Valid ForgotPasswordModel fpm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    if (bindingResult.hasErrors()) {

      return "authentication/forgot-password";
    }

    if (!userService.doUserExistByEmail(fpm.getEmail())) {

      bindingResult.rejectValue("email", "forgotpassword.usernoexist");
    }

    if (bindingResult.hasErrors()) {

      return "authentication/forgot-password";
    }

    log.debug("Sending forgotten password email to '" + fpm.getEmail() + "'.");

    String passwordResetToken = authenticationService.forgotPassword(fpm.getEmail());

    if (passwordResetToken != null) {
      UserResponse user = userService.getUser(fpm.getEmail());
      mailService.sendForgottenPasswordMail(user.getNames(), fpm.getEmail(), passwordResetToken);

      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("forgotpassword.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("forgotpassword.error"));
    }

    return "redirect:/login";
  }

  @GetMapping("/reset-password/{passwordResetToken}")
  public String prepareResetPassword(@PathVariable("passwordResetToken") String passwordResetToken, Model model) {

    UserResponse user = userService.getUserByPasswordResetToken(passwordResetToken);

    if (user == null) {
      return "redirect:/login";
    }

    model.addAttribute("user", user);

    ResetPasswordModel rpm = new ResetPasswordModel();

    model.addAttribute("rpm", rpm);
    model.addAttribute("passwordResetToken", passwordResetToken);

    return "authentication/reset-password";
  }

  @PostMapping("/reset-password/{passwordResetToken}")
  public String resetPassword(@PathVariable("passwordResetToken") String passwordResetToken, @ModelAttribute("rpm") @Valid ResetPasswordModel rpm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    UserResponse user = userService.getUserByPasswordResetToken(passwordResetToken);

    if (user == null) {

      model.addAttribute("passwordResetToken", passwordResetToken);

      return "redirect:/login";
    }

    model.addAttribute("user", user);

    if (bindingResult.hasErrors()) {

      model.addAttribute("passwordResetToken", passwordResetToken);

      return "authentication/reset-password";
    }

    if (!rpm.getNewPassword().equals(rpm.getConfirmPassword())) {

      bindingResult.rejectValue("newPassword", "resetpassword.passwordsnomatch");
      bindingResult.rejectValue("confirmPassword", "resetpassword.passwordsnomatch");
    }

    if (bindingResult.hasErrors()) {

      model.addAttribute("passwordResetToken", passwordResetToken);

      return "authentication/reset-password";
    }

    boolean isPasswordReset = authenticationService.resetPassword(rpm.getNewPassword(), passwordResetToken);

    if (isPasswordReset) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("resetpassword.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("resetpassword.error"));
    }

    return "redirect:/login";
  }

  @GetMapping(value = "/activate-user-account", params = {"token"})
  public String prepareActivateUserAccount(@RequestParam(name = "token", required = true) String token, Model model) {

    UserResponse user = userService.getUserByPasswordResetToken(token);

    if (user == null) {
      return "redirect:/";
    }

    model.addAttribute("token", token);
    model.addAttribute("user", user);
    model.addAttribute("auam", new ActivateUserAccountModel());

    return "authentication/activate-user-account";
  }

  @PostMapping(value = "/activate-user-account", params = {"token"})
  public String activateUserAccount(@RequestParam(name = "token", required = true) String token, @ModelAttribute("auam") @Valid ActivateUserAccountModel auam, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    UserResponse user = userService.getUserByPasswordResetToken(token);

    if (user == null) {
      return "redirect:/";
    }

    if (bindingResult.hasErrors()) {

      model.addAttribute("token", token);
      model.addAttribute("user", user);

      return "authentication/activate-user-account";
    }

    if (!auam.getNewPassword().equals(auam.getConfirmPassword())) {
      bindingResult.rejectValue("newPassword", "activateuseraccount.passwordsnomatch");
      bindingResult.rejectValue("confirmPassword", "activateuseraccount.passwordsnomatch");
    }

    if (bindingResult.hasErrors()) {

      model.addAttribute("token", token);
      model.addAttribute("user", user);

      return "authentication/activate-user-account";
    }

    boolean isActivated = userService.activateUserAccount(user.getCid(), auam.getNewPassword());

    if (isActivated) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("activateuseraccount.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("activateuseraccount.error"));
    }

    return "redirect:/login";
  }
}
