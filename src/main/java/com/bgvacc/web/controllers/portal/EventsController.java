package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.services.EventService;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import java.time.format.DateTimeFormatter;
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
public class EventsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventApi eventApi;

  private final EventService eventService;

  @GetMapping("/portal/events")
  public String getEvents(Model model) {

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();

    model.addAttribute("events", vatsimEvents);

    model.addAttribute("pageTitle", getMessage("portal.events.events.title"));
    model.addAttribute("page", "events");
    model.addAttribute("subpage", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.events", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/events";
  }

  @GetMapping("/portal/events/calendar")
  public String getEventsCalendar(Model model) {

    List<EventResponse> events = eventService.getEvents();

    List<CalendarEvent> calendarEvents = convertEventsToCalendarEvents(events);

    model.addAttribute("ces", calendarEvents);

    model.addAttribute("pageTitle", getMessage("portal.events.calendar.title"));
    model.addAttribute("page", "events");
    model.addAttribute("subpage", "calendar");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.events", null, LocaleContextHolder.getLocale()), "/portal/events"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.calendar", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/calendar";
  }

  private List<CalendarEvent> convertEventsToCalendarEvents(List<EventResponse> events) {

    List<CalendarEvent> calendarEvents = new ArrayList<>();

    for (EventResponse e : events) {
      CalendarEvent ce = new CalendarEvent();

      ce.setId(String.valueOf(e.getEventId()));
      ce.setTitle(e.getName());

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      String startDate = e.getStartAt().format(formatter);
      String endDate = e.getEndAt().format(formatter);

      ce.setStart(startDate);
      ce.setEnd(endDate);

      String eventColor = "var(--bs-primary)";
      String cptColor = "var(--bs-success)";

      ce.setBackgroundColor(eventColor);
      ce.setBorderColor(eventColor);

      if (e.isCpt()) {
        ce.setBackgroundColor(cptColor);
        ce.setBorderColor(cptColor);
      }

      if (e.isEvent()) {
        ce.setBackgroundColor(eventColor);
        ce.setBorderColor(eventColor);
      }

      calendarEvents.add(ce);
    }

    return calendarEvents;
  }
}
