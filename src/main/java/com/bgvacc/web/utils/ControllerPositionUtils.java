package com.bgvacc.web.utils;

import com.bgvacc.web.vatsim.atc.VatsimATCInfo;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class ControllerPositionUtils {

  public static VatsimATCInfo getPositionFrequency(String callsign) {

    switch (callsign.toUpperCase()) {
      case "LBSR_CTR":
        return new VatsimATCInfo(callsign, "Sofia Control", "131.225");
      case "LBSR_V_CTR":
        return new VatsimATCInfo(callsign, "Sofia Control", "134.700");
      case "LBSF_TWR":
      case "LBSF_T_TWR":
      case "LBSF_M_TWR":
      case "LBSF_X_TWR":
        return new VatsimATCInfo(callsign, "Sofia Tower", "118.100");
      case "LBSF_APP":
      case "LBSF_T_APP":
      case "LBSF_M_APP":
      case "LBSF_X_APP":
        return new VatsimATCInfo(callsign, "Sofia Approach", "123.700");
      case "LBBG_TWR":
        return new VatsimATCInfo(callsign, "Burgas Tower", "118.000");
      case "LBBG_APP":
        return new VatsimATCInfo(callsign, "Burgas Approach", "125.100");
      case "LBWN_TWR":
        return new VatsimATCInfo(callsign, "Varna Tower", "119.500");
      case "LBWN_APP":
        return new VatsimATCInfo(callsign, "Varna Approach", "124.600");
      case "LBPD_TWR":
        return new VatsimATCInfo(callsign, "Plovdiv Tower", "133.600");
      case "LBGO_TWR":
        return new VatsimATCInfo(callsign, "Gorna Tower", "133.500");
    }

    return null;
  }
}
