package com.bgvacc.web.utils;

import com.bgvacc.web.vatsim.charts.AirportCharts;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface ChartsService {

  AirportCharts getAirportCharts(String airpotIcao);

}
