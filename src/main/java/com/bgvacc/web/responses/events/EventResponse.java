package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
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
  private ZonedDateTime endAt;
  private Timestamp createdAt;
  private Timestamp updatedAt;

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
