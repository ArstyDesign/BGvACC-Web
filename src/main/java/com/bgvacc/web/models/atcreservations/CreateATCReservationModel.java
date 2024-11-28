package com.bgvacc.web.models.atcreservations;

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
public class CreateATCReservationModel implements Serializable {
  
  private String startTime;
  private String endTime;
  private String type;
  private String position;
  private String userCid;
  private String traineeCid;

}
