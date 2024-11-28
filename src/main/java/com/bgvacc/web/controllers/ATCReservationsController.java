package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.atcreservations.CreateATCReservationModel;
import com.bgvacc.web.responses.mentortrainees.MentorTraineeResponse;
import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
import com.bgvacc.web.services.*;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  @GetMapping("/atc-reservations/{reservationId}")
  public String viewATCReservation(@PathVariable("reservationId") String reservationId, Model model) {

    model.addAttribute("page", "atc-reservations");

    return "atc-reservations/atc-reservation";
  }

  @GetMapping("/atc-reservations/create")
  public String prepareNewATCReservation(Model model, HttpServletRequest request) {

    if (!userATCAuthorizedPositionsService.hasUserAuthorizedPositions(getLoggedUserCid(request))) {
      log.debug("User does not have any authorized positions");
      return "redirect:/calendar";
    }

//    if ()
    List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));

    model.addAttribute("mentorTrainees", mentorTrainees);

    List<UserATCAuthorizedPositionResponse> authorizedPositions = userATCAuthorizedPositionsService.getUserATCAuthorizedPositions(getLoggedUserCid(request));

    model.addAttribute("authorizedPositions", authorizedPositions);

    CreateATCReservationModel carm = new CreateATCReservationModel();
    model.addAttribute("carm", carm);

    model.addAttribute("pageTitle", "Calendar");
    model.addAttribute("page", "atc-reservations");

    return "atc-reservations/create-atc-reservation";
  }

  @PostMapping("/atc-reservations/create")
  public String createNewATCReservation(@ModelAttribute("carm") CreateATCReservationModel carm, BindingResult bindingResult, Model model, HttpServletRequest request) {

    log.debug(carm.toString());

    if (bindingResult.hasErrors()) {

      List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));

      model.addAttribute("mentorTrainees", mentorTrainees);

      model.addAttribute("pageTitle", "Calendar");
      model.addAttribute("page", "atc-reservations");

      return "atc-reservations/create-atc-reservation";
    }

    String loggedUserCid = getLoggedUserCid(request);
    carm.setUserCid(loggedUserCid);

    boolean isCreated = atcReservationService.createNewATCReservation(carm);
    
    return "redirect:/calendar";
  }
}
