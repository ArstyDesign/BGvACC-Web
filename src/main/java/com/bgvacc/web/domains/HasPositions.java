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
public class HasPositions implements Serializable {

  private Boolean hasAnyControl = false;
  private Boolean hasAnySofia = false;
  private Boolean hasAnyVarna = false;
  private Boolean hasAnyBurgas = false;
  private Boolean hasAnyOthers = false;

}
