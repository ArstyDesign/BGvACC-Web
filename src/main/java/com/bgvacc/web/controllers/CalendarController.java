package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.services.ATCReservationService;
import com.bgvacc.web.services.EventService;
import com.bgvacc.web.utils.Breadcrumb;
import static com.bgvacc.web.utils.Utils.convertATCReservationsToCalendarEvents;
import static com.bgvacc.web.utils.Utils.convertEventsToCalendarEvents;
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
public class CalendarController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final ATCReservationService atcReservationService;

  @GetMapping("/calendar")
  public String showCalendar(Model model) {

    List<EventResponse> events = eventService.getAllEvents();
    List<ATCReservationResponse> allFutureATCReservations = atcReservationService.getAllFutureATCReservations();

    List<CalendarEvent> calendarEvents = convertEventsToCalendarEvents(events, "events");
    calendarEvents.addAll(convertATCReservationsToCalendarEvents(allFutureATCReservations, "atc-reservations", getMessageSource()));

    model.addAttribute("ces", calendarEvents);

    model.addAttribute("pageTitle", getMessage("calendar.title"));
    model.addAttribute("page", "calendar");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "calendar/calendar";
  }
}
