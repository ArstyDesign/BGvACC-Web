package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.*;
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
public class EventResponse implements Serializable {

  private Long eventId;
  private String name;
  private String type;
  private Integer priority;
  private Integer cptRatingNumber;
  private String cptRatingSymbol;
  private String cptExaminee;
  private String description;
  private String shortDescription;
  private String vatsimEventUrl;
  private String vateudEventUrl;
  private String imageUrl;
  private ZonedDateTime startAt;
  private Timestamp startAtTimestamp;
  private ZonedDateTime endAt;
  private Timestamp endAtTimestamp;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private List<EventIcao> icaos;

  public boolean isPast() {

    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    return now.isAfter(endAt);
  }

  public boolean isCurrentlyActive() {

    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
    boolean resultAfter = now.isAfter(startAt);
    boolean resultBefore = now.isBefore(endAt);

    return resultAfter && resultBefore;
  }

  public String getFormattedStartDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return startAt.format(formatter);
  }

  public String getFormattedEndDateTime(String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return endAt.format(formatter);
  }

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
    ZonedDateTime utcDateTime = startAt.withZoneSameInstant(ZoneOffset.UTC);
    this.startAtTimestamp = Timestamp.from(utcDateTime.toInstant());
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

  public String getHtmlDescription() {
    Parser parser = Parser.builder().build();
    HtmlRenderer renderer = HtmlRenderer.builder().build();
    Node document = parser.parse(this.description);
    return renderer.render(document);
  }
}
