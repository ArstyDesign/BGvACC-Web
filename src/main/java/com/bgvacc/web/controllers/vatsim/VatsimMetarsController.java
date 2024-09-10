package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.MetarApi;
import com.bgvacc.web.vatsim.metars.VatsimMetar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class VatsimMetarsController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private MetarApi metarApi;

  @GetMapping(value = "/metar/{icao}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<VatsimMetar> getMetarAtIcao(@PathVariable("icao") String icao) {
    List<VatsimMetar> metarAtAirport = metarApi.getMetarAtAirport(icao);
    return metarAtAirport;
  }
}
