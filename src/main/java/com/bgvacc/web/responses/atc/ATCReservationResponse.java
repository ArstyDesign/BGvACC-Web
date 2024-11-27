package com.bgvacc.web.responses.atc;

import com.bgvacc.web.utils.Names;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneOffset;
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
public class ATCReservationResponse implements Serializable {

  private String reservationId;
  private String reservationType;
  private String position;
  private String userCid;
  private Names userNames;
  private String traineeCid;
  private Names traineeNames;
  private ZonedDateTime fromTime;
  private Timestamp fromTimeTimestamp;
  private ZonedDateTime toTime;
  private Timestamp toTimeTimestamp;
  private ZonedDateTime createdAt;
  private Timestamp createdAtTimestamp;

  public void setFromTime(ZonedDateTime fromTime) {
    this.fromTime = fromTime;
    ZonedDateTime utcDateTime = fromTime.withZoneSameInstant(ZoneOffset.UTC);
    this.fromTimeTimestamp = Timestamp.from(utcDateTime.toInstant());
  }

  public void setEndAt(ZonedDateTime toTime) {
    this.toTime = toTime;
    ZonedDateTime utcDateTime = toTime.withZoneSameInstant(ZoneOffset.UTC);
    this.toTimeTimestamp = Timestamp.from(utcDateTime.toInstant());
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
    ZonedDateTime utcDateTime = createdAt.withZoneSameInstant(ZoneOffset.UTC);
    this.createdAtTimestamp = Timestamp.from(utcDateTime.toInstant());
  }
}
