package com.bgvacc.web.utils;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class AirportUtils {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public boolean isManagedAirport(String icao) {

    for (String knownAirport : getKnownAirports()) {
      if (icao.equalsIgnoreCase(knownAirport)) {
        return true;
      }
    }

    return false;
  }

  private List<String> getKnownAirports() {

    List<String> knownAirports = new ArrayList<>();

    knownAirports.add("LBSF");
    knownAirports.add("LBBG");
    knownAirports.add("LBWN");
    knownAirports.add("LBPD");
    knownAirports.add("LBGO");

    return knownAirports;
  }
}
