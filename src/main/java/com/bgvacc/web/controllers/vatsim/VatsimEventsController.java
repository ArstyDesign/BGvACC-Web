package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.vatsim.events.VatsimAirports;
import com.bgvacc.web.vatsim.events.VatsimData;
import com.bgvacc.web.vatsim.events.VatsimEvent;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class VatsimEventsController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private EventApi eventApi;

  @GetMapping("/events")
  public String getEventsInBulgaria(Model model) {

    VatsimEvents events = eventApi.getVatsimEventsByDivision("EUD");

    VatsimEvents bulgariaEvents = new VatsimEvents(new ArrayList<>());

    for (VatsimData vatsimData : events.getData()) {
      for (VatsimAirports airport : vatsimData.getAirports()) {
        if (airport.getIcao().equalsIgnoreCase("LBSF")
                || airport.getIcao().equalsIgnoreCase("LBBG")
                || airport.getIcao().equalsIgnoreCase("LBWN")
                || airport.getIcao().equalsIgnoreCase("LBPD")
                || airport.getIcao().equalsIgnoreCase("LBGO")) {
          bulgariaEvents.getData().add(vatsimData);
        }
      }
    }

    model.addAttribute("bulgariaEvents", bulgariaEvents);

    model.addAttribute("pageTitle", "VATSIM Events");
    model.addAttribute("page", "events");

    return "vatsim/events/events";
  }

  @GetMapping("/events/{eventId}")
  public String getEvent(@PathVariable("eventId") String eventId, Model model) {

    VatsimEvent vatsimEvent = eventApi.getVatsimEvent(eventId);

    model.addAttribute("event", vatsimEvent);

    model.addAttribute("page", "events");

    return "vatsim/events/event";
  }
}
