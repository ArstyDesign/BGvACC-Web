package com.bgvacc.web.responses.users;

import com.bgvacc.web.domains.UTCDateTime;
import com.bgvacc.web.utils.Names;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEventApplicationResponse implements Serializable {

  private String applicationId;
  private String userCid;
  private Names userNames;
  private String position;
  private Boolean status;
  private Timestamp appliedAt;
  private Boolean isAddedByStaff;
  private Staff staff;
  private Slot slot;
  private Event event;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public class Staff {

    private String cid;
    private Names names;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public class Slot {

    private UTCDateTime startTime;
    private UTCDateTime endTime;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @ToString
  public class Event {

    private String eventId;
    private String name;
    private String type;
    private String imageUrl;
    private UTCDateTime startAt;
    private UTCDateTime endAt;
    private Timestamp createdAt;
  }
}
