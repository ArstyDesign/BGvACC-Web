package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class EventUserApplicationResponse implements Serializable {
  
  private String applicationId;
  private EventUserResponse user;
  private String slotId;
  private Boolean status;
  private Timestamp appliedAt;

}
