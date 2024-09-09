package com.aarshinkov.template.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class HomeController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping(value = "/")
  public String home() {
    return "home";
  }
}
