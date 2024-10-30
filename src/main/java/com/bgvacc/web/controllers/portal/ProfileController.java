package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProfileController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final VatEudCoreApi vatEudCoreApi;

  @GetMapping("/portal/profile")
  public String profile(HttpServletRequest request, Model model) {

    VatEudUser member = vatEudCoreApi.getMemberDetails(Long.valueOf(getLoggedUserCid(request)));

    model.addAttribute("member", member);

    model.addAttribute("pageTitle", getLoggedUser(request).getNames().getFullName());
    model.addAttribute("page", "profile");
    model.addAttribute("subpage", "profile");

    return "portal/profile/profile";
  }
}
