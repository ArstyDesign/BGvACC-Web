package com.bgvacc.web.controllers;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.services.DatabaseService;
import com.bgvacc.web.services.EventService;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class TestController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final DatabaseService databaseService;

  private final EventApi eventApi;

  private final EventService eventService;

  @GetMapping("/test/db")
  public String testDatabase(Model model) {

    boolean result = databaseService.isDatabaseConnected();

    model.addAttribute("isDbConnected", result);

    return "test/database";
  }

  @GetMapping("/test/events-sync")
  @ResponseBody
  public String testEventsSync() {

    log.info("Syncing events...");

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();

    eventService.synchroniseVatsimEventsToDatabase(vatsimEvents.getData());

    return "Testing events sync";
  }

//  @GetMapping("/test/addController")
//  public String testAddController(Model model) {
//    log.debug("Controller added...");
//
//    Memory memory = Memory.getInstance();
//    memory.addATC(new VatsimATC(1664545L, "LBSR_CTR", "1", 5, "Emil", "Ivanov"));
//    memory.addATC(new VatsimATC(1779345L, "LBSF_TWR", "1", 2, "Theo", "Delmas"));
//    memory.addATC(new VatsimATC(1720051L, "LBSF_APP", "1", 4, "Atanas", "Arshinkov"));
//    memory.addATC(new VatsimATC(1773453L, "LBBG_TWR", "1", 3, "Kristian", "Hristov"));
//    memory.isAdded = true;
//    return "redirect:/";
//  }
//
//  @GetMapping("/test/removeController")
//  public String testRemoveController(Model model) {
//    log.debug("Controller removed...");
//
//    Memory memory = Memory.getInstance();
//    memory.isAdded = false;
//    return "redirect:/";
//  }
}
