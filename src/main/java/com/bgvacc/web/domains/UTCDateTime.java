package com.bgvacc.web.domains;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.ToString;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
@ToString
public class UTCDateTime implements Serializable {

  private Timestamp time;
  private ZonedDateTime zoneTime;

  public UTCDateTime(Timestamp time) {
    ZonedDateTime zonedDateTime = time.toLocalDateTime().atZone(ZoneId.of("UTC"));
    ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    this.zoneTime = zonedDateTime;
    this.time = Timestamp.from(utcDateTime.toInstant());
  }

  public Timestamp getTime() {
    return time;
  }

  public ZonedDateTime getZoneTime() {
    return zoneTime;
  }

  public String getFormattedDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return zoneTime.format(formatter);
  }
}
