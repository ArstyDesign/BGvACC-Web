package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
public class EventSlotResponse implements Serializable {

  private String slotId;
  private ZonedDateTime startAt;
  private Timestamp startTime;
  private ZonedDateTime endAt;
  private Timestamp endTime;
  private EventUserResponse user;
  private Boolean isApproved;

  private List<EventUserApplicationResponse> userEventApplications;

  public String getFormattedStartDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    startAt = startTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
    
    return startAt.format(formatter);
  }

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
    ZonedDateTime utcDateTime = startAt.withZoneSameInstant(ZoneOffset.UTC);
    this.startTime = Timestamp.from(utcDateTime.toInstant());
  }

  public String getFormattedEndDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    endAt = endTime.toLocalDateTime().atZone(ZoneId.of("UTC"));
    return endAt.format(formatter);
  }

  public void setEndAt(ZonedDateTime startAt) {
    this.endAt = startAt;
    ZonedDateTime utcDateTime = startAt.withZoneSameInstant(ZoneOffset.UTC);
    this.endTime = Timestamp.from(utcDateTime.toInstant());
  }
}
