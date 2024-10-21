package com.bgvacc.web.responses.sessions;

import com.aarshinkov.datetimecalculator.domain.Time;
import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
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
public class ControllerOnlineLogResponse implements Serializable {

  private String controllerOnlineId;
  private String cid;
  private Integer rating;
  private String server;
  private String position;
  private Timestamp sessionStarted;
  private Timestamp sessionEnded;

  public Long getTotalSeconds() {

    LocalDateTime end;

    if (this.sessionEnded == null) {
      end = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
    } else {
      end = this.sessionEnded.toLocalDateTime();
    }

    Duration duration = Duration.between(sessionStarted.toLocalDateTime(), end);

    return duration.getSeconds();
  }

  public Time getCalculatedTimeOnline() {

    LocalDateTime end;

    if (this.sessionEnded == null) {
      end = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
    } else {
      end = this.sessionEnded.toLocalDateTime();
    }

    Duration duration = Duration.between(sessionStarted.toLocalDateTime(), end);
    
    Time time = new Time(duration.getSeconds());

    return time;
  }

  public String getAtcRatingSymbol() {
    return VatsimRatingUtils.getATCRatingSymbol(this.rating);
  }

  public String getAtcRatingName() {
    return VatsimRatingUtils.getATCRatingName(this.rating);
  }
}
