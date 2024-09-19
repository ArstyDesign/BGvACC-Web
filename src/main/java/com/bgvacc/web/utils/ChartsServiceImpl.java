package com.bgvacc.web.utils;

import com.bgvacc.web.vatsim.charts.AirportChart;
import com.bgvacc.web.vatsim.charts.AirportCharts;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
public class ChartsServiceImpl implements ChartsService {

  @Override
  public AirportCharts getAirportCharts(String airportIcao) {

    if (airportIcao.equalsIgnoreCase("lbsf")) {
      return getLBSFCharts();
    } else if (airportIcao.equalsIgnoreCase("lbwn")) {
      return getLBWNCharts();
    } else if (airportIcao.equalsIgnoreCase("lbbg")) {
      return getLBBGCharts();
    } else if (airportIcao.equalsIgnoreCase("lbgo")) {
      return getLBGOCharts();
    } else if (airportIcao.equalsIgnoreCase("lbpd")) {
      return getLBPDCharts();
    } else if (airportIcao.equalsIgnoreCase("lbht")) {
      return getLBHTCharts();
    } else if (airportIcao.equalsIgnoreCase("lbls")) {
      return getLBLSCharts();
    } else if (airportIcao.equalsIgnoreCase("lbrs")) {
      return getLBRSCharts();
    }
    return null;
  }

  private AirportCharts getLBSFCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbsf");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище София - текстова част / Sofia Aerodrome - Textual data", "Sofia Aerodrome - Textual data", "LB_AD_2_LBSF_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> taxi = new ArrayList<>();
    taxi.add(new AirportChart("Aerodrome Chart - ICAO (SOFIA ADC)", "Aerodrome Chart", "LB_AD_2_LBSF_41_1_2_en.pdf"));
    taxi.add(new AirportChart("Aircraft Parking/Docking Chart - ICAO (SOFIA PDC - 1)", "Aircraft Parking/Docking Chart 1", "LB_AD_2_LBSF_43_1_2_en.pdf"));
    taxi.add(new AirportChart("Aircraft Parking/Docking Chart - ICAO (SOFIA PDC - 1)", "Aircraft Parking/Docking Chart 2", "LB_AD_2_LBSF_43_3_4_en.pdf"));

    charts.setTaxi(taxi);

    Map<String, List<AirportChart>> sid = new LinkedHashMap<>();

    List<AirportChart> sidRwy27 = new ArrayList<>();
    sidRwy27.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RNAV RWY 27)", "SID RNAV RWY 27", "LB_AD_2_LBSF_53_7_8_9_10_en.pdf"));
    sidRwy27.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RWY 27)", "SID RWY 27", "LB_AD_2_LBSF_53_11_12_en.pdf"));
    sid.put("27", sidRwy27);

    List<AirportChart> sidRwy09 = new ArrayList<>();
    sidRwy09.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RNAV RWY 09)", "SID RNAV RWY 09", "LB_AD_2_LBSF_53_1_2_3_4_en.pdf"));
    sidRwy09.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RWY 09)", "SID RWY 09", "LB_AD_2_LBSF_53_5_6_en.pdf"));
    sid.put("09", sidRwy09);

    charts.setSid(sid);

    Map<String, List<AirportChart>> star = new LinkedHashMap<>();

    List<AirportChart> starRwy27 = new ArrayList<>();
    starRwy27.add(new AirportChart("Standard Arrival Chart - Instrument (STAR) - ICAO (SOFIA RNAV RWY 27)", "STAR RNAV RWY 27", "LB_AD_2_LBSF_55_7_8_9_10_en.pdf"));
    starRwy27.add(new AirportChart("Standard Arrival Chart - Instrument (STAR) - ICAO (SOFIA RNAV RWY 27) - NISVA 3K", "STAR RNAV RWY 27 - NISVA3K", "LB_AD_2_LBSF_55_11_12_en.pdf"));
    star.put("27", starRwy27);

    List<AirportChart> starRwy09 = new ArrayList<>();
    starRwy09.add(new AirportChart("Standard Arrival Chart - Instrument (STAR) - ICAO (SOFIA RNAV RWY 09)", "STAR RNAV RWY 09", "LB_AD_2_LBSF_55_1_2_3_4_5_6_en.pdf"));
    star.put("09", starRwy09);

    charts.setStar(star);

    Map<String, List<AirportChart>> app = new LinkedHashMap<>();

    List<AirportChart> appRwy27 = new ArrayList<>();
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA ILS z CAT II & III or LOC z RWY 27)", "ILS Z CAT II & III or LOC Z RWY 27", "LB_AD_2_LBSF_57_11_12_en.pdf"));
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA ILS y CAT II & III or LOC y RWY 27)", "ILS Y CAT II & III or LOC Y RWY 27", "LB_AD_2_LBSF_57_13_14_en.pdf"));
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA RNP RWY 27)", "RNP RWY 27", "LB_AD_2_LBSF_57_15_16_17_18_en.pdf"));
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA VOR RWY 27)", "VOR RWY 27", "LB_AD_2_LBSF_57_19_20_en.pdf"));
    app.put("27", appRwy27);

    List<AirportChart> appRwy09 = new ArrayList<>();
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA ILS z or LOC z RWY 09)", "ILS Z or LOC Z RWY 09", "LB_AD_2_LBSF_57_1_2_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA ILS y or LOC y RWY 09)", "ILS Y or LOC Y RWY 09", "LB_AD_2_LBSF_57_3_4_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA RNP RWY 09)", "RNP RWY 09", "LB_AD_2_LBSF_57_5_6_7_8_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (SOFIA VOR RWY 09)", "VOR RWY 09", "LB_AD_2_LBSF_57_9_10_en.pdf"));
    app.put("09", appRwy09);

    charts.setApp(app);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (SOFIA VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_2_LBSF_59_1_2_3_4_5_6_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    List<AirportChart> others = new ArrayList<>();

    others.add(new AirportChart("Waypoint list SOFIA", "Waypoints list", "LB_AD_2_LBSF_61_1_2_en.pdf"));
    others.add(new AirportChart("Aerodrome Obstacle Chart - ICAO Type A - (Operating Limitations) (SOFIA RWY 09/27)", "Aerodrome Obstacle Chart", "LB_AD_2_LBSF_47_1_en.pdf"));
    others.add(new AirportChart("Precision Approach Terrain Chart - ICAO (SOFIA RWY 27)", "Precision Approach Terrain Chart", "LB_AD_2_LBSF_49_1_2_en.pdf"));
    others.add(new AirportChart("ATC Surveillance Minimum Altitude Chart - ICAO", "ATC Surveillance Minimum Altitude Chart", "LB_AD_2_LBSF_51_1_2_3_4_en.pdf"));

    charts.setOthers(others);

    return charts;
  }

  private AirportCharts getLBWNCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbwn");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Варна - текстова част / Varna Aerodrome - Textual data", "Varna Aerodrome - Textual data", "LB_AD_2_LBWN_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> taxi = new ArrayList<>();
    taxi.add(new AirportChart("Aerodrome Chart - ICAO (VARNA ADC)", "Aerodrome Chart", "LB_AD_2_LBWN_41_1_2_en.pdf"));
    taxi.add(new AirportChart("Aircraft Parking/Docking Chart - ICAO (VARNA PDC)", "Aircraft Parking/Docking Chart", "LB_AD_2_LBWN_43_1_2_en.pdf"));

    charts.setTaxi(taxi);

    Map<String, List<AirportChart>> sid = new LinkedHashMap<>();

    List<AirportChart> sidRwy09 = new ArrayList<>();
    sidRwy09.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (VARNA RNAV RWY 09)", "SID RNAV RWY 09", "LB_AD_2_LBWN_53_1_2_en.pdf"));
    sid.put("09", sidRwy09);

    List<AirportChart> sidRwy27 = new ArrayList<>();
    sidRwy27.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (VARNA RNAV RWY 27)", "SID RNAV RWY 27", "LB_AD_2_LBWN_53_3_4_en.pdf"));
    sid.put("27", sidRwy27);

    charts.setSid(sid);

    Map<String, List<AirportChart>> star = new LinkedHashMap<>();

    List<AirportChart> starRwy09 = new ArrayList<>();
    starRwy09.add(new AirportChart("Standard Arrival Chart -Instrument (STAR) - ICAO (VARNA RNAV RWY 09)", "STAR RNAV RWY 09", "LB_AD_2_LBWN_55_1_2_3_4_en.pdf"));
    star.put("09", starRwy09);

    List<AirportChart> starRwy27 = new ArrayList<>();
    starRwy27.add(new AirportChart("Standard Arrival Chart -Instrument (STAR) - ICAO (VARNA RNAV RWY 27)", "STAR RNAV RWY 27", "LB_AD_2_LBWN_55_5_6_7_8_en.pdf"));
    star.put("27", starRwy27);

    charts.setStar(star);

    Map<String, List<AirportChart>> app = new LinkedHashMap<>();

    List<AirportChart> appRwy09 = new ArrayList<>();
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA ILS z or LOC z RWY 09)", "ILS Z or LOC Z RWY 09", "LB_AD_2_LBWN_57_1_2_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA ILS y or LOC y RWY 09)", "ILS Y or LOC Y RWY 09", "LB_AD_2_LBWN_57_3_4_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA RNP RWY 09)", "RNP RWY 09", "LB_AD_2_LBWN_57_5_6_7_8_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA VOR RWY 09)", "VOR RWY 09", "LB_AD_2_LBWN_57_9_10_en.pdf"));
    app.put("09", appRwy09);

    List<AirportChart> appRwy27 = new ArrayList<>();
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA RNP RWY 27)", "RNP RWY 27", "LB_AD_2_LBWN_57_11_12_13_14_en.pdf"));
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (VARNA VOR RWY 27)", "VOR RWY 27", "LB_AD_2_LBWN_57_15_16_en.pdf"));
    app.put("27", appRwy27);

    charts.setApp(app);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (VARNA VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_2_LBWN_59_1_2_3_4_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    List<AirportChart> others = new ArrayList<>();

    others.add(new AirportChart("Waypoint list VARNA", "Waypoints list", "LB_AD_2_LBWN_61_1_2_en.pdf"));
    others.add(new AirportChart("Aerodrome Obstacle Chart - ICAO Type A (Operating Limitations) (VARNA RWY 09/27)", "Aerodrome Obstacle Chart", "LB_AD_2_LBWN_47_1_2_en.pdf"));
    others.add(new AirportChart("ATC Surveillance Minimum Altitude Chart - ICAO", "ATC Surveillance Minimum Altitude Chart", "LB_AD_2_LBWN_51_1_2_en.pdf"));

    charts.setOthers(others);

    return charts;
  }

  private AirportCharts getLBBGCharts() {
    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbbg");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Бургас - текстова част / Burgas Aerodrome - Textual data", "Burgas Aerodrome - Textual data", "LB_AD_2_LBBG_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> taxi = new ArrayList<>();
    taxi.add(new AirportChart("Aerodrome Chart - ICAO (BURGAS)", "Aerodrome Chart", "LB_AD_2_LBBG_41_1_2_en.pdf"));
    taxi.add(new AirportChart("Aircraft Parking/Docking Chart - ICAO (BURGAS PDC)", "Aircraft Parking/Docking Chart", "LB_AD_2_LBBG_43_1_2_en.pdf"));

    charts.setTaxi(taxi);

    Map<String, List<AirportChart>> sid = new LinkedHashMap<>();

    List<AirportChart> sidRwy04 = new ArrayList<>();
    sidRwy04.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (BURGAS RNAV RWY 04)", "SID RNAV RWY 04", "LB_AD_2_LBBG_53_1_2_3_4_en.pdf"));
    sid.put("04", sidRwy04);

    List<AirportChart> sidRwy22 = new ArrayList<>();
    sidRwy22.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (BURGAS RNAV RWY 22 West)", "SID RNAV RWY 22 West", "LB_AD_2_LBBG_53_5_6_en.pdf"));
    sidRwy22.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (BURGAS RNAV RWY 22 East)", "SID RNAV RWY 22 East", "LB_AD_2_LBBG_53_7_8_en.pdf"));
    sid.put("22", sidRwy22);

    charts.setSid(sid);

    Map<String, List<AirportChart>> star = new LinkedHashMap<>();

    List<AirportChart> starRwy04 = new ArrayList<>();
    starRwy04.add(new AirportChart("Standard Arrival Chart - Instrument (STAR) - ICAO (BURGAS RNAV RWY 04)", "STAR RNAV RWY 04", "LB_AD_2_LBBG_55_1_2_3_4_en.pdf"));
    star.put("04", starRwy04);

    List<AirportChart> starRwy22 = new ArrayList<>();
    starRwy22.add(new AirportChart("Standard Arrival Chart - Instrument (STAR) - ICAO (BURGAS RNAV RWY 22)", "STAR RNAV RWY 22", "LB_AD_2_LBBG_55_5_6_7_8_en.pdf"));
    star.put("22", starRwy22);

    charts.setStar(star);

    Map<String, List<AirportChart>> app = new LinkedHashMap<>();

    List<AirportChart> appRwy04 = new ArrayList<>();
    appRwy04.add(new AirportChart("Instrument Approach Chart - ICAO (BURGAS RNP RWY 04)", "RNP RWY 04", "LB_AD_2_LBBG_57_1_2_3_4_en.pdf"));
    appRwy04.add(new AirportChart("Instrument Approach Chart - ICAO (BURGAS VOR RWY 04)", "VOR RWY 04", "LB_AD_2_LBBG_57_5_6_en.pdf"));
    app.put("04", appRwy04);

    List<AirportChart> appRwy22 = new ArrayList<>();
    appRwy22.add(new AirportChart("Instrument Approach Chart - ICAO (BURGAS ILS or LOC RWY 22)", "ILS or LOC RWY 22", "LB_AD_2_LBBG_57_7_8_en.pdf"));
    appRwy22.add(new AirportChart("Instrument Approach Chart - ICAO (BURGAS RNP RWY 22)", "RNP RWY 22", "LB_AD_2_LBBG_57_9_10_11_12_en.pdf"));
    appRwy22.add(new AirportChart("Instrument Approach Chart - ICAO (BURGAS VOR RWY 22)", "VOR RWY 22", "LB_AD_2_LBBG_57_13_14_en.pdf"));
    app.put("22", appRwy22);

    charts.setApp(app);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (BURGAS VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_2_LBBG_59_1_2_3_4_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    List<AirportChart> others = new ArrayList<>();

    others.add(new AirportChart("Waypoint list BURGAS", "Waypoints list", "LB_AD_2_LBBG_61_1_2_en.pdf"));
    others.add(new AirportChart("Aerodrome Obstacle Chart -ICAO Type A (Operating Limitations) (BURGAS RWY 04/22)", "Aerodrome Obstacle Chart", "LB_AD_2_LBBG_47_1_2_en.pdf"));
    others.add(new AirportChart("ATC Surveillance Minimum Altitude Chart - ICAO", "ATC Surveillance Minimum Altitude Chart", "LB_AD_2_LBBG_51_1_2_en.pdf"));

    charts.setOthers(others);

    return charts;
  }

  private AirportCharts getLBPDCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbpd");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Пловдив - текстова част / Plovdiv Aerodrome - Textual data", "Plovdiv Aerodrome - Textual data", "LB_AD_2_LBPD_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> taxi = new ArrayList<>();
    taxi.add(new AirportChart("Aerodrome Chart - ICAO (PLOVDIV ADC)", "Aerodrome Chart", "LB_AD_2_LBPD_41_1_2_en.pdf"));
    taxi.add(new AirportChart("Aircraft Parking/Docking Chart - ICAO (PLOVDIV PDC)", "Aircraft Parking/Docking Chart", "LB_AD_2_LBPD_43_1_2_en.pdf"));

    charts.setTaxi(taxi);

    Map<String, List<AirportChart>> sid = new LinkedHashMap<>();

    List<AirportChart> sidRwy12 = new ArrayList<>();
    sidRwy12.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (PLOVDIV RNAV RWY 12)", "SID RNAV RWY 12", "LB_AD_2_LBPD_53_1_2_en.pdf"));
    sidRwy12.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (PLOVDIV VOR RWY 12)", "SID VOR RWY 12", "LB_AD_2_LBPD_53_3_4_en.pdf"));
    sid.put("12", sidRwy12);

    List<AirportChart> sidRwy30 = new ArrayList<>();
    sidRwy30.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RNAV RWY 09)", "SID RNAV RWY 09", "LB_AD_2_LBSF_53_1_2_3_4_en.pdf"));
    sidRwy30.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (SOFIA RWY 09)", "SID RWY 09", "LB_AD_2_LBSF_53_5_6_en.pdf"));
    sid.put("30", sidRwy30);

    charts.setSid(sid);

    Map<String, List<AirportChart>> app = new LinkedHashMap<>();

    List<AirportChart> appRwy12 = new ArrayList<>();
    appRwy12.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV RNP z RWY 12)", "RNP Z RWY 12", "LB_AD_2_LBPD_57_1_2_3_4_en.pdf"));
    appRwy12.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV RNP y RWY 12)", "RNP Y RWY 12", "LB_AD_2_LBPD_57_5_6_7_8_en.pdf"));
    appRwy12.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV VOR RWY 12)", "VOR RWY 27", "LB_AD_2_LBPD_57_9_10_en.pdf"));
    appRwy12.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV NDB RWY 12)", "NDB RWY 27", "LB_AD_2_LBPD_57_11_12_en.pdf"));
    app.put("12", appRwy12);

    List<AirportChart> appRwy30 = new ArrayList<>();
    appRwy30.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV ILS x or LOC x RWY 30)", "ILS X or LOC X RWY 30", "LB_AD_2_LBPD_57_13_14_en.pdf"));
    appRwy30.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV ILS w or LOC w RWY 30)", "ILS W or LOC W RWY 09", "LB_AD_2_LBPD_57_15_16_en.pdf"));
    appRwy30.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV RNP RWY 30)", "RNP RWY 30", "LB_AD_2_LBPD_57_17_18_19_20_en.pdf"));
    appRwy30.add(new AirportChart("Instrument Approach Chart - ICAO (PLOVDIV VOR RWY 30)", "VOR RWY 30", "LB_AD_2_LBPD_57_21_22_en.pdf"));
    app.put("30", appRwy30);

    charts.setApp(app);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (PLOVDIV VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_2_LBPD_59_1_2_3_4_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    List<AirportChart> others = new ArrayList<>();

    others.add(new AirportChart("Waypoint list PLOVDIV", "Waypoints list", "LB_AD_2_LBPD_61_1_2_en.pdf"));
    others.add(new AirportChart("Aerodrome Obstacle Chart - ICAO Type A (Operating Limitations) (PLOVDIV RWY 12/30)", "Aerodrome Obstacle Chart", "LB_AD_2_LBPD_47_1_en.pdf"));

    charts.setOthers(others);

    return charts;
  }

  private AirportCharts getLBGOCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbgo");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Горна Оряховица - текстова част / Gorna Oryahovitsa Aerodrome - Textual data", "Gorna Oryahovitsa Aerodrome - Textual data", "LB_AD_2_LBGO_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> taxi = new ArrayList<>();
    taxi.add(new AirportChart("Aerodrome Chart - ICAO (GORNA ORYAHOVITSA)", "Aerodrome Chart", "LB_AD_2_LBGO_41_1_2_en.pdf"));

    charts.setTaxi(taxi);

    Map<String, List<AirportChart>> sid = new LinkedHashMap<>();

    List<AirportChart> sidRwy09 = new ArrayList<>();
    sidRwy09.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (GORNA ORYAHOVITSA RNAV RWY 09)", "SID RNAV RWY 09", "LB_AD_2_LBGO_53_1_2_en.pdf"));
    sid.put("09", sidRwy09);

    List<AirportChart> sidRwy27 = new ArrayList<>();
    sidRwy27.add(new AirportChart("Standard Departure Chart - Instrument (SID) - ICAO (GORNA ORYAHOVITSA RNAV RWY 27)", "SID RNAV RWY 27", "LB_AD_2_LBGO_53_3_4_en.pdf"));
    sid.put("27", sidRwy27);

    charts.setSid(sid);

    Map<String, List<AirportChart>> app = new LinkedHashMap<>();

    List<AirportChart> appRwy09 = new ArrayList<>();
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (GORNA ORYAHOVITSA RNP RWY 09)", "RNP RWY 09", "LB_AD_2_LBGO_57_1_2_3_4_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (GORNA ORYAHOVITSA VOR RWY 09)", "VOR RWY 09", "LB_AD_2_LBGO_57_5_6_en.pdf"));
    appRwy09.add(new AirportChart("Instrument Approach Chart - ICAO (GORNA ORYAHOVITSA NDB RWY 09)", "NDB RWY 09", "LB_AD_2_LBGO_57_7_8_en.pdf"));
    app.put("09", appRwy09);

    List<AirportChart> appRwy27 = new ArrayList<>();
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (GORNA ORYAHOVITSA RNP RWY 27)", "RNP RWY 27", "LB_AD_2_LBGO_57_9_10_11_12_en.pdf"));
    appRwy27.add(new AirportChart("Instrument Approach Chart - ICAO (GORNA ORYAHOVITSA VOR RWY 27)", "VOR RWY 27", "LB_AD_2_LBGO_57_13_14_en.pdf"));
    app.put("27", appRwy27);

    charts.setApp(app);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (GORNA ORYAHOVITSA VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_2_LBGO_59_1_2_3_4_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    List<AirportChart> others = new ArrayList<>();

    others.add(new AirportChart("Waypoint list GORNA ORYAHOVITSA", "Waypoints list", "LB_AD_2_LBGO_61_1_2_en.pdf"));

    charts.setOthers(others);

    return charts;
  }

  private AirportCharts getLBHTCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbht");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Ихтиман - текстова част / Ihtiman Aerodrome - Textual data", "Ihtiman Aerodrome - Textual data", "LB_AD_4_LBHT_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (IHTIMAN VFR)", "Visual Approach - VFR Routes", "LB_AD_4_LBHT_59_1_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    return charts;
  }

  private AirportCharts getLBLSCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbls");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Лесново - текстова част / Lesnovo Aerodrome - Textual data", "Lesnovo Aerodrome - Textual data", "LB_AD_4_LBLS_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (LESNOVO VFR ROUTES)", "Visual Approach - VFR Routes", "LB_AD_4_LBLS_59_1_2_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    return charts;
  }

  private AirportCharts getLBRSCharts() {

    AirportCharts charts = new AirportCharts();

    charts.setDirectory("lbrs");

    List<AirportChart> general = new ArrayList<>();
    general.add(new AirportChart("Летище Русе - текстова част / Ruse Aerodrome - Textual data", "Ruse Aerodrome - Textual data", "LB_AD_4_LBRS_en.pdf"));

    charts.setGeneral(general);

    List<AirportChart> visualApproachCharts = new ArrayList<>();
    visualApproachCharts.add(new AirportChart("Visual Approach Chart - ICAO (RUSE VFR)", "Visual Approach - VFR Routes", "LB_AD_4_LBRS_59_1_en.pdf"));

    charts.setVisualApp(visualApproachCharts);

    return charts;
  }
}
