package com.bgvacc.web.api;

import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.metars.VatsimMetar;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class MetarApi extends Api {

  public List<VatsimMetar> getMetarAtAirport(String icao) {

    final String url = "https://metar.vatsim.net/" + icao + "?format=json";

    return doRequestList(Methods.GET, url, null, VatsimMetar.class);
  }
}
