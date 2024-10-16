package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.services.UserService;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UsersController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserService userService;

  @GetMapping("/portal/users")
  public String getUsers(Model model) {

    List<UserResponse> users = userService.getUsers();

    model.addAttribute("users", users);

    model.addAttribute("pageTitle", getMessage("portal.users.users.title"));
    model.addAttribute("page", "users");
    model.addAttribute("subpage", "users");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/users/users";
  }
}
