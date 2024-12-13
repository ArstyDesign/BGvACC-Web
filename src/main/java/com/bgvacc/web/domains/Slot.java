package com.bgvacc.web.domains;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.*;
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
public class Slot implements Serializable {

  private LocalDateTime start;
  private LocalDateTime end;

  public String getFormattedStartDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    ZoneId zoneId = ZoneId.of("UTC");
    ZonedDateTime startDateTime = start.atZone(zoneId);
    return startDateTime.format(formatter);
  }
  
  public String getFormattedEndDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    ZoneId zoneId = ZoneId.of("UTC");
    ZonedDateTime endDateTime = end.atZone(zoneId);
    return endDateTime.format(formatter);
  }
}
