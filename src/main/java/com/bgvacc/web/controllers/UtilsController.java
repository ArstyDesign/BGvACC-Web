package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
