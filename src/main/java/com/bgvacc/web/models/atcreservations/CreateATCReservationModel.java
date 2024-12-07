package com.bgvacc.web.models.atcreservations;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

  @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
  private LocalDateTime startTime;
  @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
  private LocalDateTime endTime;
  private String type;
  @NotBlank
  private String position;
  private String userCid;
  private String traineeCid;

}
