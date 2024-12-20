package com.bgvacc.web.responses.events;

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
public class EventIcao implements Serializable {

  private String eventIcaoId;
  private String icao;
}
