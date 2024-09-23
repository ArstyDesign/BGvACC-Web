package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.utils.ChartsService;
import com.bgvacc.web.vatsim.charts.AirportCharts;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PilotsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private ChartsService chartsService;

  @GetMapping("/pilots/airspace")
  public String getBulgarianAirspace(Model model) {

    model.addAttribute("pageTitle", getMessage("pilots.airspace.title"));
    model.addAttribute("page", "pilots");
    model.addAttribute("subpage", "airspace");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots.airspace", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "pilots/airspace";
  }

  @GetMapping("/pilots/bulgarian-sceneries")
  public String getBulgarianSceneries(Model model) {

    model.addAttribute("pageTitle", getMessage("pilots.bgsceneries.title"));
    model.addAttribute("page", "pilots");
    model.addAttribute("subpage", "bulgarian-sceneries");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots.sceneries", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "pilots/bulgarian-sceneries";
  }

  @GetMapping("/pilots/charts")
  public String getCharts(Model model) {

    model.addAttribute("pageTitle", getMessage("pilots.charts.title"));
    model.addAttribute("page", "pilots");
    model.addAttribute("subpage", "charts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots.charts", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "pilots/charts";
  }

  @GetMapping("/pilots/charts/{airportIcao}")
  public String getAirportCharts(@PathVariable("airportIcao") String airportIcao, Model model) {

    AirportCharts charts = chartsService.getAirportCharts(airportIcao);
    model.addAttribute("charts", charts);

    String pageTitle = getMessage("pilots.charts.view." + airportIcao.toLowerCase() + ".title");

    if (pageTitle != null && !pageTitle.trim().isEmpty()) {
      model.addAttribute("pageTitle", pageTitle);
      model.addAttribute("title", pageTitle);
    } else {
      model.addAttribute("title", getMessage("pilots.charts.view.defaulttitle"));
    }

    model.addAttribute("page", "pilots");
    model.addAttribute("subpage", "charts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.pilots.charts", null, LocaleContextHolder.getLocale()), "/pilots/charts"));
    breadcrumbs.add(new Breadcrumb(pageTitle, null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "pilots/charts-view";
  }
}
