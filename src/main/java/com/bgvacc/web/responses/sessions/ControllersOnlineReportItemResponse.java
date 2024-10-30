package com.bgvacc.web.responses.sessions;

import com.aarshinkov.datetimecalculator.domain.Time;
import com.bgvacc.web.utils.Names;
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
public class ControllersOnlineReportItemResponse implements Serializable {

  private String cid;
  private Names controllerName;
  private String position;
  private Long secondsControlled;

  public Time getTotalTimeControlled() {

    if (secondsControlled == null) {
      secondsControlled = 0L;
    }

    return new Time(secondsControlled);
  }
}
