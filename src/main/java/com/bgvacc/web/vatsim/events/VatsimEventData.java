package com.bgvacc.web.vatsim.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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
public class VatsimEventData implements Serializable {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("image_url")
  private String imageUrl;

  @JsonProperty("start")
  private String start;

  @JsonProperty("end")
  private String end;

//  @JsonProperty("routing")
//  private String routing;
  @JsonProperty("airports")
  private VatsimEventAirport airports;

//  @JsonProperty("forum_event_id")
//  private String forumEventId;
  @JsonProperty("user_cid")
  private Long userCid;

  @JsonProperty("edit_cid")
  private Long editCid;

  @JsonProperty("created_at")
  private Timestamp createdAt;

  @JsonProperty("updated_at")
  private Timestamp updatedAt;

  @JsonProperty("short_description")
  private String shortDescription;

  public LocalDateTime getFromDateTime() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    return LocalDateTime.parse(this.start, inputFormatter);
  }

  public LocalDateTime getToDateTime() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    return LocalDateTime.parse(this.end, inputFormatter);
  }

  public String getStartTimeFormatted() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy, HH:mm", Locale.getDefault());
//    return LocalDateTime.parse(this.startTime, inputFormatter);
    LocalDateTime dateTime = LocalDateTime.parse(this.start, inputFormatter);
    return outputFormatter.format(dateTime) + "z";
  }

  public String getEndTimeFormatted() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyy, HH:mm", Locale.getDefault());
//    return LocalDateTime.parse(this.startTime, inputFormatter);
    LocalDateTime dateTime = LocalDateTime.parse(this.end, inputFormatter);
    return outputFormatter.format(dateTime) + "z";
  }

  public String getTimeFrame() {
    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
    DateTimeFormatter outputFormatter1 = DateTimeFormatter.ofPattern("MMMM dd, yyyy, HH:mm", Locale.getDefault());
    DateTimeFormatter outputFormatter2 = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
    LocalDateTime fromTime = LocalDateTime.parse(this.start, inputFormatter);
    LocalDateTime toTime = LocalDateTime.parse(this.end, inputFormatter);

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

  public String getHtmlDescription() {
    Parser parser = Parser.builder().build();
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    Node document = parser.parse(this.description);
    return renderer.render(document);
  }
}
