package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.system.airports.*;
import com.bgvacc.web.services.system.AirportService;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalAirportsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final AirportService airportService;

  @GetMapping("/portal/airports")
  public String getAirports(Model model) {

    List<AirportResponse> airports = airportService.getAirports();

    model.addAttribute("airports", airports);

    model.addAttribute("pageTitle", getMessage("portal.system.airports.title"));
    model.addAttribute("page", "system");
    model.addAttribute("subpage", "airports");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.airports", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/airports/airports";
  }

  @GetMapping("/portal/airports/{airportId}")
  public String getAirport(@PathVariable("airportId") String airportId, Model model) {

    AirportDetailsResponse airport = airportService.getAirportDetails(airportId);
    model.addAttribute("ap", airport);

    List<AirportRunwayResponse> runways = airportService.getAirportRunways(airportId);
    model.addAttribute("runways", runways);

    model.addAttribute("pageTitle", airport.getName());
    model.addAttribute("page", "system");
    model.addAttribute("subpage", "airports");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.airports", null, LocaleContextHolder.getLocale()), "/portal/airports"));
    breadcrumbs.add(new Breadcrumb(airport.getName(), null));
//    breadcrumbs.add(new Breadcrumb(, null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/airports/airport";
  }

  @GetMapping("/portal/airports/{airportId}/charts")
  public String getAirportCharts(@PathVariable("airportId") String airportId, Model model) {

    AirportResponse airport = airportService.getAirport(airportId);

    model.addAttribute("ap", airport);

    model.addAttribute("pageTitle", airport.getName());
    model.addAttribute("page", "system");
    model.addAttribute("subpage", "airports");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.airports", null, LocaleContextHolder.getLocale()), "/portal/airports"));
    breadcrumbs.add(new Breadcrumb(airport.getName(), "/portal/airports/" + airportId));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.airports.charts"), null));
//    breadcrumbs.add(new Breadcrumb(, null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/airports/charts";
  }
}
