package com.bgvacc.web.services.system;

import com.bgvacc.web.responses.system.airports.*;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface AirportService {

  List<AirportResponse> getAirports();

  AirportResponse getAirport(String airportId);
  
  AirportDetailsResponse getAirportDetails(String airportId);

  List<AirportRunwayResponse> getAirportRunways(String airportId);

  boolean addRunwayToAirport(String airportId, int runwayNumber, String description);
}
