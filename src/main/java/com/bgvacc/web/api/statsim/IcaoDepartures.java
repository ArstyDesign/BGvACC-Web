package com.bgvacc.web.api.statsim;

import com.bgvacc.web.api.Api;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.responses.statsim.FlightInfoResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class IcaoDepartures extends Api {

  public List<FlightInfoResponse> getDepartingFlightsForAirportAndTime(String airportIcao, String startDateTime, String endDateTime) {
    String url = "https://api.statsim.net/api/Flights/IcaoOrigin?icao=" + airportIcao + "&from=" + startDateTime + "&to=" + endDateTime;
    return doRequestList(Methods.GET, url, null, FlightInfoResponse.class);
  }
  
  public List<FlightInfoResponse> getArrivingFlightsForAirportAndTime(String airportIcao, String startDateTime, String endDateTime) {
    String url = "https://api.statsim.net/api/Flights/IcaoDestination?icao=" + airportIcao + "&from=" + startDateTime + "&to=" + endDateTime;
    return doRequestList(Methods.GET, url, null, FlightInfoResponse.class);
  }
}
