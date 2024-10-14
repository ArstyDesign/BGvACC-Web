package com.bgvacc.web.controllers;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import com.bgvacc.web.vatsim.events.VatsimEvents;
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

  private final EventApi eventApi;

  @GetMapping(value = "/")
  public String home(Model model) {

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();
    VatsimEvents vatsimEventsPart = new VatsimEvents();

    if (vatsimEvents != null) {

      vatsimEventsPart.setSuccess(vatsimEvents.isSuccess());

      List<VatsimEventData> data = new ArrayList<>();

      final int MAX_EVENTS_TO_SHOW = 3;
      int eventsToShow = 0;

      if (vatsimEvents.getData() != null) {
        if (vatsimEvents.getData().size() > MAX_EVENTS_TO_SHOW) {
          eventsToShow = MAX_EVENTS_TO_SHOW;
        } else {
          eventsToShow = vatsimEvents.getData().size();
        }
      }

      for (int i = 0; i < eventsToShow; i++) {
        VatsimEventData ved = vatsimEvents.getData().get(i);
        data.add(ved);
      }

      vatsimEventsPart.setData(data);
    }

    model.addAttribute("events", vatsimEventsPart);

    model.addAttribute("pageTitle", getMessage("home.title"));
    model.addAttribute("page", "home");

    return "home";
  }
}
