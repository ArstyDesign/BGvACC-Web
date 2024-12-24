package com.bgvacc.web.responses.atc;

import com.bgvacc.web.domains.UTCDateTime;
import com.bgvacc.web.utils.Names;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
public class ATCReservationResponse implements Serializable {

  private String reservationId;
  private String reservationType;
  private String position;
  private String userCid;
  private Names userNames;
  private String traineeCid;
  private Names traineeNames;
  private UTCDateTime fromTime;
  private UTCDateTime toTime;
  private UTCDateTime createdAt;
}
