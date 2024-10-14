package com.bgvacc.web.domains;

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
public class AirportDefaults implements Serializable {

  private Integer windStrengthMinimum = 5;
  private String defaultRunway;
}
