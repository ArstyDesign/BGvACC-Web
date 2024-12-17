package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.domains.HasPositions;
import com.bgvacc.web.responses.events.*;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.responses.users.atc.PositionResponse;
import com.bgvacc.web.services.*;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.utils.ControllerPositionUtils;
import static com.bgvacc.web.utils.Utils.convertEventsToCalendarEvents;
import com.bgvacc.web.vatsim.atc.VatsimATCInfo;
import java.sql.Timestamp;
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
public class PortalEventsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final PositionService positionService;

  private final MailService mailService;

  private final UserService userService;

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

    List<EventPositionResponse> eventPositions = eventService.getEventPositions(eventId);
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
  public String addPositionToEvent(@PathVariable("eventId") Long eventId, @RequestParam("position") String position, @RequestParam("minimumRating") Integer minimumRating, @RequestParam(name = "canTraineesApply", defaultValue = "false") boolean canTraineesApply, RedirectAttributes redirectAttributes) {

    log.debug("Adding position '" + position + "' to event with ID '" + eventId + "'");

    boolean isEventPositionAdded = eventService.addEventPosition(eventId, position, minimumRating, canTraineesApply);

    if (isEventPositionAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.roster.addposition.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.roster.addposition.modal.error"));
    }

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/positions/remove/{eventPositionId}")
  public String removePositionFromEvent(@PathVariable("eventId") Long eventId, @PathVariable("eventPositionId") String eventPositionId, RedirectAttributes redirectAttributes) {

    log.debug("Removing event position with ID '" + eventPositionId + "' from event with ID '" + eventId + "'.");

    boolean isEventPositionRemoved = eventService.removeEventPosition(eventId, eventPositionId);

    if (isEventPositionRemoved) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.roster.removeposition.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.roster.removeposition.modal.error"));
    }

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/positions/{eventPositionId}/slots/add")
  public String addSlotsForPosition(@PathVariable("eventId") Long eventId, @PathVariable("eventPositionId") String eventPositionId, @RequestParam("slotsCount") Integer slotsCount, RedirectAttributes redirectAttributes) {

    log.debug("Adding slots for position with ID '" + eventPositionId + "' for event with ID '" + eventId + "'.");

    boolean areSlotsAdded = eventService.addSlotsToPosition(eventId, eventPositionId, slotsCount);

    if (areSlotsAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));

      if (slotsCount == 1) {
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.roster.addpositionslots.modal.success.1"));
      } else {
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.roster.addpositionslots.modal.success.n"));
      }
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));

      if (slotsCount == 1) {
        redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.roster.addpositionslots.modal.error.1"));
      } else {
        redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.roster.addpositionslots.modal.error.n"));
      }
    }

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/slots/{slotId}/add-manually")
  public String addControllerManually(@PathVariable("eventId") Long eventId, @PathVariable("slotId") String slotId, @RequestParam("cid") String cid, HttpServletRequest request, RedirectAttributes redirectAttributes) {

    log.debug("Adding controller with CID '" + cid + "' manually to slot with ID '" + slotId + "'.");

    boolean isUserAdded = eventService.applyUserForEventSlot(cid, slotId, true, getLoggedUserCid(request));

    if (isUserAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.addcontrollermanually.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.addcontrollermanually.modal.error"));
    }

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/slots/{slotId}/application/{applicationId}/approve")
  public String approveSlotApplication(@PathVariable("eventId") Long eventId, @PathVariable("slotId") String slotId, @PathVariable("applicationId") String applicationId, RedirectAttributes redirectAttributes) {

    boolean isSlotApplicationApproved = eventService.approveSlotApplication(eventId, slotId, applicationId);

    if (isSlotApplicationApproved) {

      EventResponse event = eventService.getEvent(eventId);
      EventSlotResponse slot = eventService.getEventSlot(slotId);
      UserResponse user = userService.getUser(slot.getUser().getCid());
      String position = eventService.getPositionFromEventPositionId(slot.getEventPositionId());

      VatsimATCInfo positionInfo = ControllerPositionUtils.getPositionFrequency(position);

      boolean isSent = mailService.sendEventControllerApplicationApprovedMail(user.getNames(), user.getCid(), user.getEmail(), eventId, event.getName(), positionInfo.getCallsign() + " - " + positionInfo.getName(), slot.getFormattedStartDateTime("dd.MM.yyyy"), slot.getFormattedStartDateTime("HH:mm") + " - " + slot.getFormattedEndDateTime("HH:mm"));

      if (isSent) {
        redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
        redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.approve.modal.success"));
      } else {
        redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
        redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.approve.modal.error"));
      }
    }

    return "redirect:/portal/events/" + eventId;
  }

  @PostMapping("/portal/events/{eventId}/slots/{slotId}/application/{applicationId}/reject")
  public String rejectSlotApplication(@PathVariable("eventId") Long eventId, @PathVariable("slotId") String slotId, @PathVariable("applicationId") String applicationId, @RequestParam("rejectReason") String rejectReason, RedirectAttributes redirectAttributes) {

    boolean isSlotApplicationRejected = eventService.rejectSlotApplication(slotId, applicationId, rejectReason);

    if (isSlotApplicationRejected) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.events.event.reject.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.events.event.reject.modal.error"));
    }

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
