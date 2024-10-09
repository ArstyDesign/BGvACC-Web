package com.bgvacc.web.domains;

import java.io.Serializable;
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
public class CalendarEvent implements Serializable {

  private String id;
  private String title;
  private String start;
  private String end;
  private String backgroundColor;
  private String borderColor;
}
