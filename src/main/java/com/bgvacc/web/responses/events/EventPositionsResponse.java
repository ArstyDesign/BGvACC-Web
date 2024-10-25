package com.bgvacc.web.responses.events;

import java.io.Serializable;
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
public class EventPositionsResponse implements Serializable {

  private String eventPositionId;
  private Long eventId;
  private String positionId;
  private String positionName;
  private Integer minimumRating;
  private Boolean isApproved;

  private List<EventSlotResponse> slots;
}
