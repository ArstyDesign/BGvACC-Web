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
  private AirportDefaults airportDefaults = new AirportDefaults();

  public String getActiveRunway() {

    if (!metar.getWindFromDirection().equalsIgnoreCase("vrb")) {

      if (metar.getWindStrength() <= airportDefaults.getWindStrengthMinimum() && (airportDefaults.getDefaultRunway() != null && !airportDefaults.getDefaultRunway().isEmpty())) {
        return airportDefaults.getDefaultRunway();
      }

      Integer windFromDegrees;

      if (metar.getWindFromDirection().charAt(0) == '0') {
        windFromDegrees = Integer.valueOf(metar.getWindFromDirection().substring(1));
        if (windFromDegrees == 0) {
          windFromDegrees = 360;
        }
      } else {
        windFromDegrees = Integer.valueOf(metar.getWindFromDirection());
      }

      Integer turningPoint = (runway.getLowerRwyNumber() * 10) + 90;

      if (windFromDegrees <= turningPoint) {
        return runway.getLowerRwyNumberLeading();
      }

      Integer opositeRunway = runway.getLowerRwyNumber() + 18;
      return opositeRunway + "";
    }

    return airportDefaults.getDefaultRunway();
  }
}
