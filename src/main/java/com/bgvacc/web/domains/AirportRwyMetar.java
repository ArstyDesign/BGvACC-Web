package com.bgvacc.web.domains;

import com.aarshinkov.domain.Metar;
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
public class AirportRwyMetar implements Serializable {
  
  private Metar metar;
  private Runway runway;

}
