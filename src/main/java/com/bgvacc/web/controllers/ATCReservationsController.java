package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.mentortrainees.MentorTraineeResponse;
import com.bgvacc.web.services.MentorTraineeService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class ATCReservationsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());
  
  private final MentorTraineeService mentorTraineeService;

  @GetMapping("/atc-reservations/{reservationId}")
  public String viewATCReservation(@PathVariable("reservationId") String reservationId, Model model) {

    model.addAttribute("page", "atc-reservations");

    return "atc-reservations/atc-reservation";
  }
  
  @GetMapping("/atc-reservations/create")
  public String prepareNewATCReservation(Model model, HttpServletRequest request) {
    
//    if ()
    
    List<MentorTraineeResponse> mentorTrainees = mentorTraineeService.getMentorTrainees(getLoggedUserCid(request));
    
    model.addAttribute("mentorTrainees", mentorTrainees);
    
    model.addAttribute("page", "atc-reservations");
    
    return "atc-reservations/create-atc-reservation";
  }
}
