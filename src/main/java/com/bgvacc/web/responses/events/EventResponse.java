package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
