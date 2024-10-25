package com.bgvacc.web.responses.events;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
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
public class EventSlotResponse implements Serializable {

  private String slotId;
  private Timestamp startTime;
  private Timestamp endTime;
  private EventUserResponse user;
  private Boolean isApproved;

  private List<EventUserApplicationResponse> userEventApplications;
}
