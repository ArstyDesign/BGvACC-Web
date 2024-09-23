package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.memory.Memory;
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
public class TestController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

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
