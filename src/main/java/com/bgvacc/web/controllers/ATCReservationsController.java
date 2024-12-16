package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.ATCCanControlPositions;
import com.bgvacc.web.models.atcreservations.CreateATCReservationModel;
import com.bgvacc.web.responses.mentortrainees.MentorTraineeResponse;
import com.bgvacc.web.responses.users.UserResponse;
import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
import com.bgvacc.web.services.*;
import static com.bgvacc.web.utils.AppConstants.ATC_RESERVATION_MAX_HOURS;
import com.bgvacc.web.utils.Breadcrumb;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class ATCReservationsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MentorTraineeService mentorTraineeService;

  private final ATCReservationService atcReservationService;

  private final UserATCAuthorizedPositionsService userATCAuthorizedPositionsService;

  private final UserService userService;

  @GetMapping("/atc-reservations/{reservationId}")
  public String viewATCReservation(@PathVariable("reservationId") String reservationId, Model model) {

    model.addAttribute("page", "atc-reservations");

    return "atc-reservations/atc-reservation";
  }

  @GetMapping("/atc-reservations/create")
  @PreAuthorize("@securityChecks.hasUserAuthorizedPositions(authentication.principal.cid)")
  public String prepareNewATCReservation(Model model, HttpServletRequest request) {

    if (!userATCAuthorizedPositionsService.hasUserAuthorizedPositions(getLoggedUserCid(request))) {
      log.debug("User does not have any authorized positions");
      return "redirect:/calendar";
    }

    List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));
    model.addAttribute("mentorTrainees", mentorTrainees);

    List<UserATCAuthorizedPositionResponse> authorizedPositions = userATCAuthorizedPositionsService.getUserATCAuthorizedPositions(getLoggedUserCid(request));
    model.addAttribute("authorizedPositions", authorizedPositions);

    ATCCanControlPositions canControl = getCanControlPositions(authorizedPositions);
    model.addAttribute("canControl", canControl);

    CreateATCReservationModel carm = new CreateATCReservationModel();
    carm.setType("n");
    model.addAttribute("carm", carm);

    model.addAttribute("pageTitle", getMessage("calendar.reservation.create.title"));
    model.addAttribute("page", "atc-reservations");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar", null, LocaleContextHolder.getLocale()), "/calendar"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar.makeatcreservation", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "atc-reservations/create-atc-reservation";
  }

  @GetMapping("/atc-reservations/search-trainee/{traineeCid}")
  @ResponseBody
  public String searchTrainee(@PathVariable("traineeCid") String traineeCid) {

    UserResponse trainee = userService.getUser(traineeCid);

    if (trainee == null) {
      return null;
    }

    return trainee.getNames().getFullName() + " - " + trainee.getCid();
  }

  @PostMapping("/atc-reservations/create")
  public String createNewATCReservation(@ModelAttribute("carm") CreateATCReservationModel carm, BindingResult bindingResult, Model model, HttpServletRequest request) {

    log.debug(carm.toString());

    if (bindingResult.hasErrors()) {

      List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));

      model.addAttribute("mentorTrainees", mentorTrainees);

      model.addAttribute("pageTitle", getMessage("calendar.reservation.create.title"));
      model.addAttribute("page", "atc-reservations");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar", null, LocaleContextHolder.getLocale()), "/calendar"));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar.makeatcreservation", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "atc-reservations/create-atc-reservation";
    }

    String loggedUserCid = getLoggedUserCid(request);
    carm.setUserCid(loggedUserCid);

    boolean hasErrors = false;
    String error = null;

    if (carm.getType() != null && carm.getType().equals("t")) {
      if (carm.getTraineeCid().trim().isEmpty()) {
        hasErrors = true;
        error = "Trainee CID empty";

        bindingResult.rejectValue("traineeCid", "calendar.reservation.create.error.traineecidempty");
      }

      if (!userService.doUserExist(carm.getTraineeCid().trim())) {
        hasErrors = true;
        error = "User with this CID does not exist";

        bindingResult.rejectValue("traineeCid", "calendar.reservation.create.error.traineedoesnotexist");
      }
    }

    if (!hasErrors) {
      if (!userATCAuthorizedPositionsService.isPositionAuthorizedForUser(carm.getPosition(), carm.getUserCid())) {
        hasErrors = true;
        error = "User is not authorized for that position";

        bindingResult.rejectValue("position", "calendar.reservation.create.error.notavailableposition", new Object[]{carm.getPosition()}, null);
      }
    }

    if (!hasErrors) {
      if (carm.getStartTime().isAfter(carm.getEndTime())) {
        hasErrors = true;
        error = "Start time is after end time";

        bindingResult.rejectValue("startTime", "calendar.reservation.create.error.starttimeafterendtime");
      }
    }

    if (!hasErrors) {
      int minimumMinutes = 60;

      if (!isDifferenceAtLeastMinutes(carm.getStartTime(), carm.getEndTime(), minimumMinutes)) {
        hasErrors = true;
        error = "Reservation is less than " + minimumMinutes;

        bindingResult.rejectValue("endTime", "calendar.reservation.create.error.atleast", new Object[]{minimumMinutes}, null);
      }
    }

    if (!hasErrors) {
      int maxMinutes = ATC_RESERVATION_MAX_HOURS * 60;

      if (!isDifferenceLessThanMinutes(carm.getStartTime(), carm.getEndTime(), maxMinutes)) {
        hasErrors = true;
        error = "Reservation is more than maximum time of " + maxMinutes + " minutes";

        bindingResult.rejectValue("startTime", "calendar.reservation.create.error.timelonger", new Object[]{ATC_RESERVATION_MAX_HOURS}, null);
        bindingResult.rejectValue("endTime", "calendar.reservation.create.error.timelonger", new Object[]{ATC_RESERVATION_MAX_HOURS}, null);
      }
    }

    if (!hasErrors) {
      if (!atcReservationService.isPositionFreeForTimeSlot(carm.getPosition(), carm.getStartTime(), carm.getEndTime())) {
        hasErrors = true;
        error = "Position taken";

        bindingResult.rejectValue("startTime", "calendar.reservation.create.error.positiontaken");
        bindingResult.rejectValue("endTime", "calendar.reservation.create.error.positiontaken");
      }
    }

    if (!hasErrors) {
      if (atcReservationService.hasUserReservedAnotherPositionForTime(loggedUserCid, carm.getStartTime(), carm.getEndTime())) {
        hasErrors = true;
        // TO BE SHOWN
        error = "User reserved another position for this time";
      }
    }

    if (!hasErrors) {

      if (carm.getType().equals("t")) {

        if (carm.getTraineeCid().equals(carm.getUserCid())) {
          hasErrors = true;
          error = "You cannot train yourself.";

          bindingResult.rejectValue("traineeCid", "calendar.reservation.create.error.traineecidmatchusercid");
        }
      }
    }

    if (hasErrors) {

      log.error("Last error: '" + error + "'");

      model.addAttribute("hasErrors", hasErrors);
      model.addAttribute("error", error);

      List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));
      model.addAttribute("mentorTrainees", mentorTrainees);

      List<UserATCAuthorizedPositionResponse> authorizedPositions = userATCAuthorizedPositionsService.getUserATCAuthorizedPositions(getLoggedUserCid(request));
      model.addAttribute("authorizedPositions", authorizedPositions);

      ATCCanControlPositions canControl = getCanControlPositions(authorizedPositions);
      model.addAttribute("canControl", canControl);

      model.addAttribute("pageTitle", getMessage("calendar.reservation.create.title"));
      model.addAttribute("page", "atc-reservations");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar", null, LocaleContextHolder.getLocale()), "/calendar"));
      breadcrumbs.add(new Breadcrumb(getMessage("menu.calendar.makeatcreservation", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "atc-reservations/create-atc-reservation";
    }

    log.info("All checks passed. Creating an ATC reservation");
    boolean isCreated = atcReservationService.createNewATCReservation(carm);

    return "redirect:/calendar";
  }

  private ATCCanControlPositions getCanControlPositions(List<UserATCAuthorizedPositionResponse> authorizedPositions) {

    ATCCanControlPositions canControl = new ATCCanControlPositions();

    for (UserATCAuthorizedPositionResponse ap : authorizedPositions) {
      if (ap.getPosition().contains("CTR")) {
        canControl.setCanControlAnyControl(true);
      }

      if (ap.getPosition().contains("LBSF")) {
        canControl.setCanControlAnySofia(true);
      }

      if (ap.getPosition().contains("LBWN")) {
        canControl.setCanControlAnyVarna(true);
      }

      if (ap.getPosition().contains("LBBG")) {
        canControl.setCanControlAnyBurgas(true);
      }

      if (ap.getPosition().contains("LBPD") || ap.getPosition().contains("LBGO")) {
        canControl.setCanControlAnyOthers(true);
      }
    }

    return canControl;
  }

  public boolean isDifferenceAtLeastMinutes(LocalDateTime start, LocalDateTime end, int minimumMinutes) {
    Duration duration = Duration.between(start, end);
    return duration.toMinutes() >= minimumMinutes;
  }

  public boolean isDifferenceLessThanMinutes(LocalDateTime start, LocalDateTime end, int minutes) {
    Duration duration = Duration.between(start, end);
    return duration.toMinutes() <= minutes;
  }
}
