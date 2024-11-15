package com.bgvacc.web.responses.events.reports;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventTypeReportResponse implements Serializable {

  private List<EventYearlyReportResponse> report;
  private Integer total;

  public void setEvents(List<EventYearlyReportResponse> report) {
    this.report = report;

    this.total = 0;

    for (EventYearlyReportResponse eyrr : report) {
      this.total += eyrr.getCount();
    }
  }

  public Integer getTotal() {

    if (this.total == null || this.total < 0) {
      this.total = 0;
    }

    return this.total;
  }

  public Double getMonthlyAverageCount() {
    return total / 12.00;
  }
}
