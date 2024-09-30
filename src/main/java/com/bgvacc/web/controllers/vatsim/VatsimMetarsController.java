package com.bgvacc.web.controllers.vatsim;

import com.aarshinkov.MetarDecoder;
import com.aarshinkov.domain.Metar;
import com.bgvacc.web.api.MetarApi;
import com.bgvacc.web.domains.AirportRwyMetar;
import com.bgvacc.web.domains.Runway;
import com.bgvacc.web.vatsim.metars.VatsimMetar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

  @GetMapping(value = "/v2/metar/{icao}", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getMetarV2AtIcao(@PathVariable("icao") String icao, Model model) {

    List<VatsimMetar> metarAtAirport = metarApi.getMetarAtAirport(icao);

    MetarDecoder metarDecoder = new MetarDecoder();

    VatsimMetar vm = metarAtAirport.get(0);

    Metar metar = metarDecoder.decodeMetar(vm.getId() + " " + vm.getMetar());

    AirportRwyMetar arm = new AirportRwyMetar();
    Runway lowerRunway = getKnownRunway(icao);

    arm.setMetar(metar);
    arm.setRunway(lowerRunway);

    model.addAttribute("arm", arm);

    return "fragments/fragments :: #metarCard";
  }

  private Runway getKnownRunway(String icao) {

    Runway rwy = new Runway();

    switch (icao.toUpperCase()) {
      case "LBSF":
        rwy.setLowerRwyNumber(9);
        rwy.setLowerRwyBearing(89);
        return rwy;
      case "LBBG":
        rwy.setLowerRwyNumber(4);
        rwy.setLowerRwyBearing(39);
        return rwy;
      case "LBWN":
        rwy.setLowerRwyNumber(9);
        rwy.setLowerRwyBearing(91);
        return rwy;
      case "LBPD":
        rwy.setLowerRwyNumber(12);
        rwy.setLowerRwyBearing(122);
        return rwy;
      case "LBGO":
        rwy.setLowerRwyNumber(9);
        rwy.setLowerRwyBearing(92);
        return rwy;
    }

    return null;
  }
}
