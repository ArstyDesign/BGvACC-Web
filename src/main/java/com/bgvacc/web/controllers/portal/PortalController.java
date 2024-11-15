package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.enums.UserRoles;
import com.bgvacc.web.responses.events.reports.EventYearlyReportResponse;
import com.bgvacc.web.responses.events.reports.EventsYearlyReportResponse;
import com.bgvacc.web.services.*;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.time.LocalDate;
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

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final VatEudCoreApi vatEudCoreApi;

  private final ControllerOnlineLogService controllerOnlineLogService;

  private final EventService eventService;

  private final UserService userService;

  @GetMapping("/portal/dashboard")
  public String portalDashboard(HttpServletRequest request, Model model) {

    String loggedUserCid = getLoggedUserCid(request);

    VatEudUser member = vatEudCoreApi.getMemberDetails(Long.valueOf(loggedUserCid));
    model.addAttribute("member", member);

    model.addAttribute("totalATCSessionsCount", controllerOnlineLogService.getTotalATCSessionsForUser(loggedUserCid));
    model.addAttribute("totalUserEventApplications", eventService.getTotalUserEventApplications(loggedUserCid));
    model.addAttribute("staffCount", userService.getUsersCountByRoles(UserRoles.STAFF_DIRECTOR, UserRoles.STAFF_EVENTS, UserRoles.STAFF_TRAINING));
    model.addAttribute("usersCount", userService.getUsersCount());

    int thisYear = LocalDate.now().getYear();
    EventsYearlyReportResponse eventsYearlyReport = eventService.getEventsYearlyReportForYear(thisYear);

    List<Integer> eventsCountByMonths = new ArrayList<>();
    for (EventYearlyReportResponse event : eventsYearlyReport.getEvents()) {
      eventsCountByMonths.add(event.getCount());
    }

    List<Integer> cptsCountByMonths = new ArrayList<>();
    for (EventYearlyReportResponse cpt : eventsYearlyReport.getCpts()) {
      cptsCountByMonths.add(cpt.getCount());
    }

    List<Integer> vasOpsCountByMonths = new ArrayList<>();
    for (EventYearlyReportResponse vasops : eventsYearlyReport.getVasops()) {
      vasOpsCountByMonths.add(vasops.getCount());
    }

    model.addAttribute("eventsCountByMonths", eventsCountByMonths);
    model.addAttribute("cptsCountByMonths", cptsCountByMonths);
    model.addAttribute("vasOpsCountByMonths", vasOpsCountByMonths);

    model.addAttribute("pageTitle", getMessage("portal.dashboard.header", member.getData().getFullName()));
    model.addAttribute("page", "dashboard");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/dashboard";
  }
}
