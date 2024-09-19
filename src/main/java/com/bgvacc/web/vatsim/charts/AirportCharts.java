package com.bgvacc.web.vatsim.charts;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class AirportCharts implements Serializable {

  private List<AirportChart> general;
  private List<AirportChart> taxi;
  private Map<String, List<AirportChart>> sid;
  private Map<String, List<AirportChart>> star;
  private Map<String, List<AirportChart>> app;
  private List<AirportChart> visualApp;
  private List<AirportChart> others;

  private String directory;

  public int getTotalChartsCount() {

    int count = 0;

    count += (general != null) ? general.size() : 0;
    count += (taxi != null) ? taxi.size() : 0;

    if (sid != null) {
      for (Map.Entry<String, List<AirportChart>> entry : sid.entrySet()) {
        count += entry.getValue().size();
      }
    }

    if (star != null) {
      for (Map.Entry<String, List<AirportChart>> entry : star.entrySet()) {
        count += entry.getValue().size();
      }
    }

    if (app != null) {
      for (Map.Entry<String, List<AirportChart>> entry : app.entrySet()) {
        count += entry.getValue().size();
      }
    }

    count += (visualApp != null) ? visualApp.size() : 0;
    count += (others != null) ? others.size() : 0;

    return count;
  }
}
