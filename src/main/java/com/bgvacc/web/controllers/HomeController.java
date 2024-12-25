package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.services.ATCReservationService;
import com.bgvacc.web.services.EventService;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HomeController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final ATCReservationService atcReservationService;

  @GetMapping(value = "/")
  public String home(Model model) {

    List<EventResponse> upcomingEvents = eventService.getUpcomingEvents();
    List<EventResponse> upcomingEventsPart = new ArrayList<>();

    if (upcomingEvents != null) {

      final int MAX_EVENTS_TO_SHOW = 3;
      int eventsToShow;

      if (upcomingEvents.size() > MAX_EVENTS_TO_SHOW) {
        eventsToShow = MAX_EVENTS_TO_SHOW;
      } else {
        eventsToShow = upcomingEvents.size();
      }

      for (int i = 0; i < eventsToShow; i++) {
        EventResponse ved = upcomingEvents.get(i);
        upcomingEventsPart.add(ved);
      }
    }

    model.addAttribute("upcomingEvents", upcomingEventsPart);

    Instant nowUtc = Instant.now();
    LocalDateTime localDateTimeUtc = LocalDateTime.ofInstant(nowUtc, ZoneOffset.UTC);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String currentDate = localDateTimeUtc.format(formatter);

    model.addAttribute("currentDate", currentDate);

    List<ATCReservationResponse> bookings = atcReservationService.getAllReservationsForDate(LocalDate.parse(currentDate, formatter));
    model.addAttribute("bookings", bookings);

    model.addAttribute("pageTitle", getMessage("home.title"));
    model.addAttribute("page", "home");

    return "home";
  }
  
  @GetMapping("/atc/reservations/{date}")
  public String getATCReservations(@PathVariable("date") String date, Model model) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<ATCReservationResponse> bookings = atcReservationService.getAllReservationsForDate(LocalDate.parse(date, formatter));

    model.addAttribute("bookings", bookings);

    return "components/booking-components :: #booking";
  }

  @GetMapping("/lbsf-charts")
  public String redirectSofiaChartsUrl() {
    return "redirect:/pilots/charts/lbsf";
  }

  @GetMapping("/lbwn-charts")
  public String redirectVarnaChartsUrl() {
    return "redirect:/pilots/charts/lbwn";
  }

  @GetMapping("/lbbg-charts")
  public String redirectBurgasChartsUrl() {
    return "redirect:/pilots/charts/lbbg";
  }

  @GetMapping("/lbpd-charts")
  public String redirectPlovdivChartsUrl() {
    return "redirect:/pilots/charts/lbpd";
  }

  @GetMapping("/lbgo-charts")
  public String redirectGornaChartsUrl() {
    return "redirect:/pilots/charts/lbgo";
  }
}
