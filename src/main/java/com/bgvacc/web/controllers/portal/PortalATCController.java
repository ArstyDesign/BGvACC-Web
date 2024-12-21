package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.paging.PaginationResponse;
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

  private final int DEFAULT_PAGE_LIMIT = 20;

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
  public String getATCControlHistory(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(name = "limit", required = false, defaultValue = "20") int limit, Model model, HttpServletRequest request) {

    if (page <= 0) {

      String redirectParams = "page=1";

      if (limit <= 0) {
        redirectParams += "&limit=" + DEFAULT_PAGE_LIMIT;
      }
      return "redirect:/portal/atc/sessions-history?" + redirectParams;
    }

    if (limit <= 0) {

      String redirectParams = "";

      if (page <= 0) {
        redirectParams = "page=1";
      }

      redirectParams += "&limit=" + DEFAULT_PAGE_LIMIT;

      return "redirect:/portal/atc/sessions-history?" + redirectParams;
    }
    log.debug("page: " + page);
    log.debug("limit: " + limit);

    PaginationResponse<ControllerOnlineLogResponse> controllerSessions = controllerOnlineLogService.getControllerSessions(page, limit, getLoggedUserCid(request));
    model.addAttribute("limit", limit);

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
