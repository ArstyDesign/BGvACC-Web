package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.domains.HasPositions;
import com.bgvacc.web.responses.events.EventPositionsResponse;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.responses.users.atc.PositionResponse;
import com.bgvacc.web.services.EventService;
import com.bgvacc.web.services.PositionService;
import com.bgvacc.web.utils.Breadcrumb;
import static com.bgvacc.web.utils.Utils.convertEventsToCalendarEvents;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalEventsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final PositionService positionService;

  @GetMapping("/portal/events/calendar")
  public String getEventsCalendar(Model model) {

    List<EventResponse> events = eventService.getAllEvents();

    List<CalendarEvent> calendarEvents = convertEventsToCalendarEvents(events);

    model.addAttribute("ces", calendarEvents);

    model.addAttribute("pageTitle", getMessage("portal.events.calendar.title"));
    model.addAttribute("page", "events");
    model.addAttribute("subpage", "calendar");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.calendar", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/calendar";
  }

  @GetMapping("/portal/events/upcoming")
  public String getUpcomingEvents(Model model) {

    List<EventResponse> upcomingEvents = eventService.getUpcomingEvents();

    model.addAttribute("events", upcomingEvents);

    model.addAttribute("pageTitle", getMessage("portal.events.events.upcoming.title"));
    model.addAttribute("page", "events");
    model.addAttribute("subpage", "upcoming-events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.upcomingevents", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/events";
  }

  @GetMapping("/portal/events/past")
  public String getPastEvents(Model model) {

    List<EventResponse> pastEvents = eventService.getPastEvents();

    model.addAttribute("events", pastEvents);

    model.addAttribute("pageTitle", getMessage("portal.events.events.past.title"));
    model.addAttribute("page", "events");
    model.addAttribute("subpage", "past-events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.pastevents", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/events";
  }

  @GetMapping("/portal/events/{eventId}")
  public String getEvent(@PathVariable("eventId") Long eventId, Model model) {

    EventResponse event = eventService.getEvent(eventId);
    model.addAttribute("event", event);

    List<EventPositionsResponse> eventPositions = eventService.getEventPositions(eventId);
    model.addAttribute("eventPositions", eventPositions);

    List<PositionResponse> unassignedPositions = positionService.getPositionsNotAssignedForEvent(eventId);
    model.addAttribute("unassignedPositions", unassignedPositions);

    HasPositions hasPositions = getHasPositions(unassignedPositions);
    model.addAttribute("hasPositions", hasPositions);

    model.addAttribute("pageTitle", event.getName());
    model.addAttribute("page", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));

    if (event.getEndAtTimestamp().after(new Timestamp(System.currentTimeMillis()))) {
      model.addAttribute("subpage", "upcoming-events");
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.upcomingevents", null, LocaleContextHolder.getLocale()), "/portal/events/upcoming"));
    } else {
      model.addAttribute("subpage", "past-events");
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.events.pastevents", null, LocaleContextHolder.getLocale()), "/portal/events/past"));
    }

    breadcrumbs.add(new Breadcrumb(event.getName(), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/events/event";
  }

  @PostMapping("/portal/events/{eventId}/positions/add")
  public String addPositionToEvent(@PathVariable("eventId") Long eventId, @RequestParam("position") String position, @RequestParam("minimumRating") Integer minimumRating) {

    log.debug("Adding position '" + position + "' to event with ID '" + eventId + "'");

    boolean isEventPositionAdded = eventService.addEventPosition(eventId, position, minimumRating);

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/positions/remove/{eventPositionId}")
  public String remmovePositionFromEvent(@PathVariable("eventId") Long eventId, @PathVariable("eventPositionId") String eventPositionId) {

    log.debug("Removing event position with ID '" + eventPositionId + "' from event with ID '" + eventId + "'");

    boolean isEventPositionRemoved = eventService.removeEventPosition(eventId, eventPositionId);

    return "redirect:/portal/events/" + eventId;
  }

  private HasPositions getHasPositions(List<PositionResponse> unassignedPositions) {

    HasPositions hasPositions = new HasPositions();

    for (PositionResponse p : unassignedPositions) {
      if (p.getPosition().contains("CTR")) {
        hasPositions.setHasAnyControl(true);
      }

      if (p.getPosition().contains("LBSF")) {
        hasPositions.setHasAnySofia(true);
      }

      if (p.getPosition().contains("LBWN")) {
        hasPositions.setHasAnyVarna(true);
      }

      if (p.getPosition().contains("LBBG")) {
        hasPositions.setHasAnyBurgas(true);
      }

      if (p.getPosition().contains("LBPD") || p.getPosition().contains("LBGO")) {
        hasPositions.setHasAnyOthers(true);
      }
    }

    return hasPositions;
  }
}
