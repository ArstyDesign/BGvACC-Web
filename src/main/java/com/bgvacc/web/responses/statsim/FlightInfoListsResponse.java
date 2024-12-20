package com.bgvacc.web.responses.statsim;

import java.io.Serializable;
import java.util.*;
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
public class FlightInfoListsResponse implements Serializable {

  private String icao;
  private List<FlightInfoResponse> departures = new ArrayList<>();
  private List<FlightInfoResponse> arrivals = new ArrayList<>();

  public void setDepartures(List<FlightInfoResponse> departures) {
    // Сортиране по departed
    if (departures != null) {
      departures.sort(Comparator.comparing(FlightInfoResponse::getDeparted, Comparator.nullsLast(Comparator.naturalOrder())));
    }
    this.departures = departures;
  }

  public void setArrivals(List<FlightInfoResponse> arrivals) {
    // Сортиране по arrived
    if (arrivals != null) {
      arrivals.sort(Comparator.comparing(FlightInfoResponse::getArrived, Comparator.nullsLast(Comparator.naturalOrder())));
    }
    this.arrivals = arrivals;
  }

  public Integer getTotalMovements() {

    int result = 0;

    if (this.departures != null && !this.departures.isEmpty()) {
      result += this.departures.size();
    }

    if (this.arrivals != null && !this.arrivals.isEmpty()) {
      result += this.arrivals.size();
    }

    return result;
  }
}
