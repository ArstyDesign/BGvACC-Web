package com.bgvacc.web.vatsim.utils;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class VatsimRatingUtils {

  public static String getATCRatingSymbol(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case -1:
        return "INA";
      case 0:
        return "SUS";
      case 1:
        return "OBS";
      case 2:
        return "S1";
      case 3:
        return "S2";
      case 4:
        return "S3";
      case 5:
        return "C1";
      case 6:
        return "C2";
      case 7:
        return "C3";
      case 8:
        return "I1";
      case 9:
        return "I2";
      case 10:
        return "I3";
      case 11:
        return "SUP";
      case 12:
        return "ADM";
      default:
        return null;
    }
  }

  public static String getATCRatingName(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case -1:
        return "Inactive";
      case 0:
        return "Suspended";
      case 1:
        return "Pilot/Observer";
      case 2:
        return "Tower Trainee";
      case 3:
        return "Tower Controller";
      case 4:
        return "TMA Controller";
      case 5:
        return "Enroute Controller";
      case 6:
        return "Senior Controller";
      case 7:
        return "Senior Controller";
      case 8:
        return "Instructor";
      case 9:
        return "Senior Instructor";
      case 10:
        return "Senior Instructor";
      case 11:
        return "Supervisor";
      case 12:
        return "Administrator";
      default:
        return null;
    }
  }

  public static String getPilotRatingSymbol(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case 0:
        return "P0";
      case 1:
        return "PPL";
      case 3:
        return "IR";
      case 7:
        return "CMEL";
      case 15:
        return "ATPL";
      case 31:
        return "FI";
      case 63:
        return "FE";
      default:
        return null;
    }
  }

  public static String getPilotRatingName(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case 0:
        return "No Pilot Rating";
      case 1:
        return "Private Pilot License";
      case 3:
        return "Instrument Rating";
      case 7:
        return "Commercial Multi-Engine License";
      case 15:
        return "Air Transport Pilot License";
      case 31:
        return "Flight Instructor";
      case 63:
        return "Flight Examiner";
      default:
        return null;
    }
  }

  public static String getMilitaryRatingSymbol(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case 0:
        return "M0";
      case 1:
        return "M1";
      case 3:
        return "M2";
      case 7:
        return "M3";
      case 15:
        return "M4";
      default:
        return null;
    }
  }

  public static String getMilitaryRatingName(Integer rating) {

    if (rating == null) {
      return null;
    }

    switch (rating) {
      case 0:
        return "No Military Rating";
      case 1:
        return "Military Pilot License";
      case 3:
        return "Military Instrument Rating";
      case 7:
        return "Military Multi-Engine Rating";
      case 15:
        return "Military Mission Ready Pilot";
      default:
        return null;
    }
  }
}
