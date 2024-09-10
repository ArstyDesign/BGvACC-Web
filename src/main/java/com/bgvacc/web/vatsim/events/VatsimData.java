package com.bgvacc.web.vatsim.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class VatsimData implements Serializable {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("name")
  private String name;

  @JsonProperty("link")
  private String link;

  @JsonProperty("airports")
  private List<VatsimAirports> airports;

  @JsonProperty("start_time")
  private String startTime;

  @JsonProperty("end_time")
  private String endTime;

  @JsonProperty("short_description")
  private String shortDescription;

  @JsonProperty("description")
  private String description;

  @JsonProperty("banner")
  private String banner;

  public String getStartTimeFormatted() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy, HH:mm", Locale.getDefault());
//    return LocalDateTime.parse(this.startTime, inputFormatter);
    LocalDateTime dateTime = LocalDateTime.parse(this.startTime, inputFormatter);
    return outputFormatter.format(dateTime) + "z";
  }

  public String getEndTimeFormatted() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy, HH:mm", Locale.getDefault());
//    return LocalDateTime.parse(this.startTime, inputFormatter);
    LocalDateTime dateTime = LocalDateTime.parse(this.endTime, inputFormatter);
    return outputFormatter.format(dateTime) + "z";
  }

  public String getTimeFrame() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter1 = DateTimeFormatter.ofPattern("MMMM dd, yyyy, HH:mm", Locale.getDefault());
    DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
    LocalDateTime fromTime = LocalDateTime.parse(this.startTime, inputFormatter);
    LocalDateTime toTime = LocalDateTime.parse(this.endTime, inputFormatter);

    String result = "";

    if ((fromTime.getYear() == toTime.getYear()) && (fromTime.getMonth() == toTime.getMonth()) && (fromTime.getDayOfMonth() == toTime.getDayOfMonth())) {
      result = fromTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + ", " + outputFormatter1.format(fromTime) + " to " + outputFormatter2.format(toTime) + "z";
    } else {
      result = outputFormatter1.format(fromTime) + " to " + outputFormatter1.format(toTime) + "z";
    }
    return result;
  }

//  public LocalDateTime getStartTimeInLocalDateTime() {
//    return this.startTime.toLocalDateTime();
//  }
//
//  public LocalDateTime getEndTimeInLocalDateTime() {
//    return this.endTime.toLocalDateTime();
//  }
}
