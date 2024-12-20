package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
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
public class PortalATCController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/portal/atc/events-participations")
  public String getATCEventParticipations(Model model) {

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
  public String getATCControllHistory(Model model) {
    
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
