package com.bgvacc.web.responses.statsim;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
public class FlightInfoResponse implements Serializable {

  @JsonProperty("id")
  private int id;

  @JsonProperty("vatsimid")
  private String vatsimId;

  @JsonProperty("callsign")
  private String callsign;

  @JsonProperty("departure")
  private String departure;

  @JsonProperty("destination")
  private String destination;

  @JsonProperty("aircraft")
  private String aircraft;

  @JsonProperty("altitude")
  private String altitude;

  @JsonProperty("route")
  private String route;

  @JsonProperty("departed")
  private Timestamp departed;

  @JsonProperty("arrived")
  private Timestamp arrived;

  @JsonProperty("loggedOn")
  private String loggedOn;

  public String getAircraft() {

    if (this.aircraft != null) {
      String[] split = this.aircraft.split("/");
      return split[0];
    }

    return null;
  }

  public String getFormattedDepartedDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    ZonedDateTime departedAt = departed.toInstant().atZone(ZoneId.of("UTC"));

    return departedAt.format(formatter);
  }

  public String getFormattedArrivedDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    ZonedDateTime arrivedAt = arrived.toInstant().atZone(ZoneId.of("UTC"));

    return arrivedAt.format(formatter);
  }
}
