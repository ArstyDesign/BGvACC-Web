package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.services.EventService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HomeController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

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

    model.addAttribute("pageTitle", getMessage("home.title"));
    model.addAttribute("page", "home");

    return "home";
  }
}
