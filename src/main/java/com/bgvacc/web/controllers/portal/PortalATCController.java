package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.sessions.ControllerOnlineLogResponse;
import com.bgvacc.web.responses.users.UserEventApplicationResponse;
import com.bgvacc.web.services.ControllerOnlineLogService;
import com.bgvacc.web.services.EventService;
import com.bgvacc.web.services.UserService;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalATCController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserService userService;

  private final EventService eventService;

  private final ControllerOnlineLogService controllerOnlineLogService;

  @GetMapping("/portal/atc/events-participations")
  public String getATCEventParticipations(Model model, HttpServletRequest request) {

    String cid = getLoggedUserCid(request);

    List<UserEventApplicationResponse> ueas = eventService.getUserEventApplications(cid);
    model.addAttribute("ueas", ueas);

    model.addAttribute("pageTitle", getMessage("portal.atc.eventsparticipations.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "events-participations");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.atc.eventsparticipations", null, LocaleContextHolder.getLocale()), "/portal/atc/events-participations"));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/atc/event-participations";
  }

  @GetMapping("/portal/atc/sessions-history")
  public String getATCControlHistory(@RequestParam(name = "limit", defaultValue = "-1", required = false) Integer limit, Model model, HttpServletRequest request) {

    List<ControllerOnlineLogResponse> controllerSessions = controllerOnlineLogService.getControllerSessions(getLoggedUserCid(request), limit);

    model.addAttribute("controllerSessions", controllerSessions);

    model.addAttribute("pageTitle", getMessage("portal.atc.sessionshistory.title"));
    model.addAttribute("page", "atc");
    model.addAttribute("subpage", "sessions-history");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.atc.sessionshistory", null, LocaleContextHolder.getLocale()), "/portal/atc/sessions-history"));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/atc/sessions-history";
  }
}
