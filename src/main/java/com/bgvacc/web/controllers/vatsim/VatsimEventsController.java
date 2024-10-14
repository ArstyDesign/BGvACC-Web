package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class VatsimEventsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventApi eventApi;

  @GetMapping("/events")
  public String getEventsInBulgaria(Model model) {

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();

    model.addAttribute("events", vatsimEvents);

    model.addAttribute("pageTitle", getMessage("events.title"));
    model.addAttribute("page", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.events", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "vatsim/events/events";
  }

  @GetMapping("/events/{eventId}")
  public String getEvent(@PathVariable("eventId") Long eventId, Model model) {

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();

    VatsimEventData vatsimEventData = null;

    for (VatsimEventData ved : vatsimEvents.getData()) {
      if (ved.getId().equals(eventId)) {
        vatsimEventData = ved;
      }
    }

    if (vatsimEventData == null) {
      return "redirect:/events";
    }

    model.addAttribute("event", vatsimEventData);
//
    model.addAttribute("pageTitle", getMessage("event.title", vatsimEventData.getName()));
    model.addAttribute("page", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.events", null, LocaleContextHolder.getLocale()), "/events"));
    breadcrumbs.add(new Breadcrumb(vatsimEventData.getName(), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "vatsim/events/event";
  }

//  @GetMapping("/v1/events")
//  public String getEventsInBulgaria(Model model) {
//
//    VatsimEvents events = eventApi.getVatsimEventsByDivision("EUD");
//
//    VatsimEvents bulgariaEvents = new VatsimEvents(new ArrayList<>());
//
//    for (VatsimData vatsimData : events.getData()) {
//      for (VatsimAirports airport : vatsimData.getAirports()) {
//        if (airport.getIcao().equalsIgnoreCase("LBSF")
//                || airport.getIcao().equalsIgnoreCase("LBBG")
//                || airport.getIcao().equalsIgnoreCase("LBWN")
//                || airport.getIcao().equalsIgnoreCase("LBPD")
//                || airport.getIcao().equalsIgnoreCase("LBGO")) {
//          bulgariaEvents.getData().add(vatsimData);
//        }
//      }
//    }
//
//    model.addAttribute("bulgariaEvents", bulgariaEvents);
//
//    model.addAttribute("pageTitle", "VATSIM Events");
//    model.addAttribute("page", "events");
//
//    return "vatsim/events/events";
//  }
//  @GetMapping("/events/{eventId}")
//  public String getEvent(@PathVariable("eventId") String eventId, Model model) {
//
//    VatsimEvent vatsimEvent = eventApi.getVatsimEvent(eventId);
//
//    model.addAttribute("event", vatsimEvent);
//
//    model.addAttribute("page", "events");
//
//    return "vatsim/events/event";
//  }
}
