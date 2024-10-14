package com.bgvacc.web.domains;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class Runway implements Serializable {

  private Integer lowerRwyNumber;
  private Integer lowerRwyBearing;

  public String getLowerRwyNumberLeading() {

    if (this.lowerRwyNumber > 0 && this.lowerRwyNumber < 10) {
      return "0" + this.lowerRwyNumber;
    }

    return String.valueOf(this.lowerRwyNumber);
  }
}
