package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.exceptions.GeneralErrorException;
import com.bgvacc.web.models.authentication.LoginModel;
import com.bgvacc.web.responses.authentication.AuthenticationResponse;
import com.bgvacc.web.responses.authentication.AuthenticationSuccessResponse;
import com.bgvacc.web.services.AuthenticationService;
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
public class AuthenticationController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AuthenticationService authenticationService;

  private final AuthenticationManager authenticationManager;

  @GetMapping("/login")
  public String prepareLogin(Model model) {

    model.addAttribute("lm", new LoginModel());

    model.addAttribute("page", "login");

    return "authentication/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("lm") @Valid LoginModel login, BindingResult bindingResult, HttpServletRequest req, HttpServletResponse res, RedirectAttributes redirectAttributes, Model model) throws IOException, ServletException, Exception {

    log.debug(login.toString());

    if (bindingResult.hasErrors()) {

      model.addAttribute("page", "login");

      return "authentication/login";
    }

    AuthenticationResponse authenticationResponse = new AuthenticationResponse();
    Authentication authentication;
    try {

      UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(login.getCidEmail(), login.getPassword());
      WebAuthenticationDetails details = new WebAuthenticationDetails(req);
      authReq.setDetails(details);
      authentication = authenticationManager.authenticate(authReq);
      log.debug("Auth Response: " + authenticationResponse);
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
}
