package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneOffset;
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
public class EventResponse implements Serializable {

  private Long eventId;
  private String name;
  private String type;
  private String description;
  private String shortDescription;
  private String imageUrl;
  private ZonedDateTime startAt;
  private Timestamp startAtTimestamp;
  private ZonedDateTime endAt;
  private Timestamp endAtTimestamp;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
    ZonedDateTime utcDateTime = startAt.withZoneSameInstant(ZoneOffset.UTC);
    this.startAtTimestamp = Timestamp.from(utcDateTime.toInstant());

    System.out.println("Original ZonedDateTime: " + startAt);  // Показва оригиналната дата и час с локална часова зона
    System.out.println("UTC ZonedDateTime: " + utcDateTime);    // Показва преобразуваната стойност в UTC
    System.out.println("Timestamp (UTC): " + this.startAtTimestamp);
    System.out.println("Timestamp interpreted as UTC: " + this.startAtTimestamp.toInstant());
  }

  public void setEndAt(ZonedDateTime endAt) {
    this.endAt = endAt;
    ZonedDateTime utcDateTime = endAt.withZoneSameInstant(ZoneOffset.UTC);
    this.endAtTimestamp = Timestamp.from(utcDateTime.toInstant());
  }

  public boolean isCpt() {
    return type.equalsIgnoreCase("cpt");
  }

  public boolean isEvent() {
    return type.equalsIgnoreCase("event");
  }

  public boolean isVasops() {
    return type.equalsIgnoreCase("vasops");
  }
}
