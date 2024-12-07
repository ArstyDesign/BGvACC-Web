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
public class ATCCanControlPositions implements Serializable {

  private Boolean canControlAnyControl = false;
  private Boolean canControlAnySofia = false;
  private Boolean canControlAnyVarna = false;
  private Boolean canControlAnyBurgas = false;
  private Boolean canControlAnyOthers = false;

}
