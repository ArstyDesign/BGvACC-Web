package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class UtilsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/api/time")
  @ResponseBody
  public Map<String, String> getCurrentZuluTime() {

    Instant nowUtc = Instant.now();
    LocalDateTime localDateTimeUtc = LocalDateTime.ofInstant(nowUtc, ZoneOffset.UTC);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String currentTime = localDateTimeUtc.format(formatter);

    Map<String, String> response = new HashMap<>();
    response.put("time", currentTime);

    return response;
  }

  @GetMapping("/api/date")
  @ResponseBody
  public Map<String, String> getCurrentDate() {

    Instant nowUtc = Instant.now();
    LocalDateTime localDateTimeUtc = LocalDateTime.ofInstant(nowUtc, ZoneOffset.UTC);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String currentDate = localDateTimeUtc.format(formatter);

    Map<String, String> response = new HashMap<>();
    response.put("currentDate", currentDate);

    return response;
  }

  @GetMapping("/api/date/{date}/next")
  @ResponseBody
  public Map<String, String> getDateAfterDate(@PathVariable("date") String date) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    LocalDate parsedDate = LocalDate.parse(date, formatter);

    LocalDate nextDateLocal = parsedDate.plusDays(1);

    String nextDate = nextDateLocal.format(formatter);

    Map<String, String> response = new HashMap<>();
    response.put("nextDate", nextDate);

    return response;
  }

  @GetMapping("/api/date/{date}/previous")
  @ResponseBody
  public Map<String, String> getDateBeforeDate(@PathVariable("date") String date) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    LocalDate parsedDate = LocalDate.parse(date, formatter);

    LocalDate previousLocal = parsedDate.minusDays(1);

    String previousDate = previousLocal.format(formatter);

    Map<String, String> response = new HashMap<>();
    response.put("previousDate", previousDate);

    return response;
  }
}
