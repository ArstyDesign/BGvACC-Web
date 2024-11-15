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
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventsYearlyReportResponse implements Serializable {

  private Integer year;
  private List<EventYearlyReportResponse> events;
  private Integer totalEvents;
  private List<EventYearlyReportResponse> cpts;
  private Integer totalCpts;
  private List<EventYearlyReportResponse> vasops;
  private Integer totalVasOps;

  public Integer getAllEventsCount() {

    if (totalEvents == null || totalEvents < 0) {
      totalEvents = 0;
    }

    if (totalCpts == null || totalCpts < 0) {
      totalCpts = 0;
    }

    if (totalVasOps == null || totalVasOps < 0) {
      totalVasOps = 0;
    }

    return totalEvents + totalCpts + totalVasOps;
  }

  public Double getAverageEventsCount() {

    return getTotalEvents() / 12.00;
  }

  public Double getAverageCptsCount() {

    return getTotalCpts() / 12.00;
  }

  public Double getAverageVasOpsCount() {

    return getTotalVasOps() / 12.00;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public void setEvents(List<EventYearlyReportResponse> events) {
    this.events = events;

    this.totalEvents = 0;

    for (EventYearlyReportResponse eyrr : events) {
      this.totalEvents += eyrr.getCount();
    }
  }

  public void setCpts(List<EventYearlyReportResponse> cpts) {
    this.cpts = cpts;

    this.totalCpts = 0;

    for (EventYearlyReportResponse eyrr : cpts) {
      this.totalCpts += eyrr.getCount();
    }
  }

  public void setVasops(List<EventYearlyReportResponse> vasops) {
    this.vasops = vasops;

    this.totalVasOps = 0;

    for (EventYearlyReportResponse eyrr : vasops) {
      this.totalVasOps += eyrr.getCount();
    }
  }
}
