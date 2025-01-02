package com.bgvacc.web.responses.system.airports;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AirportDetailsResponse extends AirportResponse implements Serializable {

  private String airportDetailId;
  private String icao;
  private String iata;
  private Boolean isMajor = false;
  private int elevation;
  private int transitionAltitude;
  private int transitionLevel;
  private int msa;
  private double latitude;
  private double longitude;
}
