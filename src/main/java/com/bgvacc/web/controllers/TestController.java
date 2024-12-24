package com.bgvacc.web.controllers;

import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.api.discord.DiscordNotifyApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.atc.report.ATCWeeklyReportItem;
import com.bgvacc.web.responses.atc.report.ATCWeeklyReportResponse;
import com.bgvacc.web.responses.events.reports.EventsYearlyReportResponse;
import com.bgvacc.web.responses.sessions.ControllersOnlineReportResponse;
import com.bgvacc.web.services.*;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

  private final ControllerOnlineLogService controllerOnlineLogService;

  private final ATCReservationService atcReservationService;

  private final DiscordNotifyApi discordNotifyApi;

  @GetMapping("/test/atc/report")
  @ResponseBody
  public void generateWeeklyATCReport() throws JsonProcessingException {

    List<ATCWeeklyReportItem> items = new ArrayList<>();
    items.add(new ATCWeeklyReportItem("John", "LBSF_TWR", "20 h 25 m"));
    items.add(new ATCWeeklyReportItem("Janet", "LBSF_M_APP", "5 h 12 m"));
    items.add(new ATCWeeklyReportItem("Alexandros", "LBSR_CTR", "0 h 32 m"));
    items.add(new ATCWeeklyReportItem("Constantinos", "LBWN_APP", "210 h 54 m"));
    items.add(new ATCWeeklyReportItem("Petar", "LBBG_APP", "54 h 3 m"));
    items.add(new ATCWeeklyReportItem("Nikolaos", "LBPD_TWR", "3 h 33 m"));
    items.add(new ATCWeeklyReportItem("Atanas Arshinkov", "LBPD_TWR", "6 h 00 m"));
    items.add(new ATCWeeklyReportItem("Tsvetan Mitov", "LBSF_APP", "26 h 23 m"));
    items.add(new ATCWeeklyReportItem("Kristian Hristov", "LBSF_TWR", "5 h 36 m"));
    items.add(new ATCWeeklyReportItem("Martin Martinov", "LBBG_TWR", "0 h 56 m"));
    items.add(new ATCWeeklyReportItem("Ivan Avramov", "LBWN_TWR", "2 h 20 m"));

    ATCWeeklyReportResponse response = new ATCWeeklyReportResponse();
    response.setAtcTower(items);

    discordNotifyApi.generateWeeklyATCReport(response);
  }

  @GetMapping("/test/atc/reservations")
  @ResponseBody
  public List<ATCReservationResponse> getATCReservations() {
    return atcReservationService.getAllFutureATCReservations();
  }

  @GetMapping("/test/events/yearly-report")
  @ResponseBody
  public EventsYearlyReportResponse getEventsYearlyReportForYear(@RequestParam(name = "year", required = false, defaultValue = "2024") Integer year) {

//    year = (year == null) ? LocalDate.now().getYear() : year;
    return eventService.getEventsYearlyReportForYear(year);
  }

  @GetMapping("/test/db")
  public String testDatabase(Model model) {

    boolean result = databaseService.isDatabaseConnected();

    model.addAttribute("isDbConnected", result);

    return "test/database";
  }

  @GetMapping("/test/controllers-online-week-report")
  @ResponseBody
  public ControllersOnlineReportResponse testGetControllersOnlinePastWeekReport() {
    return controllerOnlineLogService.getControllersOnlinePastWeekReport();
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
