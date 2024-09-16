package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
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
public class PilotsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/pilots/bulgarian-sceneries")
  public String getBulgarianSceneries(Model model) {

    model.addAttribute("pageTitle", "LBSF FIR Airports Sceneries");

    return "vatsim/pilots/bulgarian-sceneries";
  }
}
