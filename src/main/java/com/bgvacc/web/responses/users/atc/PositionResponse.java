package com.bgvacc.web.responses.users.atc;

import java.io.Serializable;
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
public class PositionResponse implements Serializable {

  private String position;
  private String name;
  private Integer orderPriority;

}
