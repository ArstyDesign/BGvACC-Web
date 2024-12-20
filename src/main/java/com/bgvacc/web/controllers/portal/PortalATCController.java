package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
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
public class PortalATCController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/portal/atc/event-participations")
  public String getATCEventParticipations(Model model) {

    return "portal/atc/event-participations";
  }

  @GetMapping("/portal/atc/history")
  public String getATCControllHistory(Model model) {

    return "portal/atc/history";
  }
}
