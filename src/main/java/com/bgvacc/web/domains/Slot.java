package com.bgvacc.web.domains;

import java.io.Serializable;
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
public class Slot implements Serializable {

  private LocalDateTime start;
  private LocalDateTime end;

}
