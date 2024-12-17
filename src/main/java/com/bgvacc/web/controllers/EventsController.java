package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.events.EventPositionResponse;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.responses.events.EventSlotResponse;
import com.bgvacc.web.services.EventService;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.utils.MathsUtils;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class EventsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final MathsUtils mathsUtils;

  @GetMapping("/events")
  public String getEventsInBulgaria(Model model) {

    List<EventResponse> events = eventService.getUpcomingEvents();

    model.addAttribute("events", events);

    model.addAttribute("pageTitle", getMessage("events.title"));
    model.addAttribute("page", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.events", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "vatsim/events/events";
  }

  @GetMapping("/events/{eventId}")
  public String getEvent(@PathVariable("eventId") Long eventId, Model model, HttpServletRequest request) {

    EventResponse event = eventService.getEvent(eventId);
    model.addAttribute("event", event);

    List<EventPositionResponse> eventPositions = eventService.getEventPositions(eventId);

    String loggedUserCid = getLoggedUserCid(request);

    if (loggedUserCid != null) {

      boolean isUserApprovedForAnyPosition = isUserApprovedForAnyPosition(eventPositions, loggedUserCid);

      if (!isUserApprovedForAnyPosition) {
        for (EventPositionResponse ep : eventPositions) {
          boolean canUserApply = eventService.canUserApplyForPosition(loggedUserCid, ep.getEventPositionId());
          ep.setCanUserApplyForPosition(canUserApply);

          if (canUserApply) {
            for (EventSlotResponse slot : ep.getSlots()) {
              slot.setHasUserAlreadyApplied(eventService.hasUserAlreadyAppliedForSlot(loggedUserCid, slot.getSlotId()));
            }
          }
        }
      }
    }

    boolean doAllPositionsHaveSlots = true;
    int mostSlots = 1;

    int[] slotsCount;

    if (!eventPositions.isEmpty()) {

      slotsCount = new int[eventPositions.size()];

      for (int i = 0; i < eventPositions.size(); i++) {
        slotsCount[i] = eventPositions.get(i).getSlots().size();
      }

      doAllPositionsHaveSlots = true;

      for (int i : slotsCount) {
        if (i == 0) {
          doAllPositionsHaveSlots = false;
        }
      }

      if (doAllPositionsHaveSlots) {
        mostSlots = mathsUtils.lcmOfArray(slotsCount);
      }
    } else {
      doAllPositionsHaveSlots = false;
    }

    model.addAttribute("doAllPositionsHaveSlots", doAllPositionsHaveSlots);

    model.addAttribute("mostSlots", mostSlots);
    model.addAttribute("eventPositions", eventPositions);

    String url = request.getRequestURL().toString(); // Основният URL
    String queryString = request.getQueryString();   // Параметри от заявката

    // Ако има параметри, добавяме ги към URL
    if (queryString != null) {
      url += "?" + queryString;
    }

    // Добавяме целия URL в модела
    model.addAttribute("fullUrl", url);

    model.addAttribute("pageTitle", getMessage("event.title", event.getName()));
    model.addAttribute("page", "events");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.events", null, LocaleContextHolder.getLocale()), "/events"));
    breadcrumbs.add(new Breadcrumb(event.getName(), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "vatsim/events/event";
  }

  @PostMapping("/events/{eventId}/slot/{slotId}/apply-for-controlling")
  public String applyForControlling(@PathVariable("eventId") Long eventId, @PathVariable("slotId") String slotId, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

    boolean isApplied = eventService.applyUserForEventSlot(getLoggedUserCid(request), slotId, false, null);

    if (isApplied) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("event.roster.applyforslot.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("event.roster.applyforslot.modal.error"));
    }

    return "redirect:/events/" + eventId;
  }

  private boolean isUserApprovedForAnyPosition(List<EventPositionResponse> eventPositions, String loggedUserCid) {

    for (EventPositionResponse ep : eventPositions) {
      for (EventSlotResponse slot : ep.getSlots()) {

        if (slot.getUser() != null && slot.getUser().getCid() != null) {
          if (loggedUserCid.equals(slot.getUser().getCid())) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
