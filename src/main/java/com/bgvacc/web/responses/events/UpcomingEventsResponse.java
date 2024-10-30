package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@ToString
public class UpcomingEventsResponse implements Serializable {

  private List<EventResponse> upcomingEvents;
  private Integer eventsCount;
  private Integer cptCount;

  public UpcomingEventsResponse(List<EventResponse> upcomingEvents) {

    this.eventsCount = 0;
    this.cptCount = 0;

    for (EventResponse ue : upcomingEvents) {
      if (ue.isEvent()) {
        eventsCount++;
      }

      if (ue.isCpt()) {
        cptCount++;
      }
    }

    this.upcomingEvents = upcomingEvents;
  }

  public List<EventResponse> getUpcomingEvents() {
    return upcomingEvents;
  }

  public void setUpcomingEvents(List<EventResponse> upcomingEvents) {

    this.eventsCount = 0;
    this.cptCount = 0;

    for (EventResponse ue : upcomingEvents) {
      if (ue.isEvent()) {
        eventsCount++;
      }

      if (ue.isCpt()) {
        cptCount++;
      }
    }

    this.upcomingEvents = upcomingEvents;
  }

  public Integer getEventsCount() {

    if (eventsCount == null) {
      return 0;
    }

    return eventsCount;
  }

  public Integer getCptCount() {

    if (cptCount == null) {
      return 0;
    }

    return cptCount;
  }
}
