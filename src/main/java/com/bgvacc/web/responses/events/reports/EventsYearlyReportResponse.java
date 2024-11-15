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
  private EventTypeReportResponse events;
  private EventTypeReportResponse cpts;
  private EventTypeReportResponse vasops;

  public Integer getMonthWithMostEvents() {
    return getMonthWithMostEventType(events);
  }

  public Integer getMonthWithMostCpts() {
    return getMonthWithMostEventType(cpts);
  }

  public Integer getMonthWithMostVasops() {
    return getMonthWithMostEventType(vasops);
  }

  private Integer getMonthWithMostEventType(EventTypeReportResponse eventType) {
    if (eventType == null) {
      return -1;
    }

    Integer currentMonth = -1;
    Integer currentMaximum = 0;

    for (EventYearlyReportResponse eyrr : eventType.getReport()) {
      if (eyrr.getCount() >= currentMaximum) {
        currentMaximum = eyrr.getCount();
        currentMonth = eyrr.getMonth();
      }
    }

    return currentMonth;
  }

  public Integer getAllEventsCount() {
    return events.getTotal() + cpts.getTotal() + vasops.getTotal();
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public void setEvents(EventTypeReportResponse events) {
    this.events = events;
  }

  public void setCpts(EventTypeReportResponse cpts) {
    this.cpts = cpts;
  }

  public void setVasops(EventTypeReportResponse vasops) {
    this.vasops = vasops;
  }
}
