package com.bgvacc.web.utils;

import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.events.EventResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class Utils {

  public static String getIpAddress(HttpServletRequest request) {

    String hostName = request.getHeader("X-FORWARDED-FOR");

    if (hostName == null || "".equals(hostName)) {
      hostName = request.getRemoteAddr();
    }

    return hostName;
  }

  public static String convertToHtml(String input) {
    // Заместване на новите редове с <br>
    return input.replace("\n", "<br>")
            .replace("\r", "") // Игнорираме \r
            .replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
  }

  public static List<CalendarEvent> convertATCReservationsToCalendarEvents(List<ATCReservationResponse> atcReservations) {
    return convertATCReservationsToCalendarEvents(atcReservations, "atc-reservations");
  }

  public static List<CalendarEvent> convertATCReservationsToCalendarEvents(List<ATCReservationResponse> atcReservations, String urlPrefix) {

    List<CalendarEvent> calendarEvents = new ArrayList<>();

    for (ATCReservationResponse atcr : atcReservations) {

      CalendarEvent ce = new CalendarEvent();

      ce.setId(urlPrefix + "/" + atcr.getReservationId());

      if (atcr.getReservationType().equalsIgnoreCase("training")) {
        // Training session
        ce.setTitle(atcr.getPosition() + " Training session, trainee " + atcr.getTraineeNames().getFullName() + " (" + atcr.getTraineeCid() + ")");

        ce.setBackgroundColor("#ff9300");
        ce.setBorderColor("#ff9300");
      } else {
        // Normal
        ce.setTitle(atcr.getPosition() + " online");

        ce.setBackgroundColor("#410037");
        ce.setBorderColor("#410037");
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      String startDate = atcr.getFromTime().format(formatter);
      String endDate = atcr.getToTime().format(formatter);

      ce.setStart(startDate);
      ce.setEnd(endDate);

      ce.setUrl(urlPrefix + "/" + atcr.getReservationId());
      calendarEvents.add(ce);
    }

    return calendarEvents;
  }

  public static List<CalendarEvent> convertEventsToCalendarEvents(List<EventResponse> events) {
    return convertEventsToCalendarEvents(events, null);
  }

  public static List<CalendarEvent> convertEventsToCalendarEvents(List<EventResponse> events, String urlPrefix) {

    List<CalendarEvent> calendarEvents = new ArrayList<>();

    for (EventResponse e : events) {

      CalendarEvent ce = new CalendarEvent();

      ce.setId(String.valueOf(e.getEventId()));
      ce.setTitle(e.getName());

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      String startDate = e.getStartAt().format(formatter);
      String endDate = e.getEndAt().format(formatter);

      ce.setStart(startDate);
      ce.setEnd(endDate);

      if (urlPrefix == null) {
        ce.setUrl(ce.getId());
      } else {
        ce.setUrl(urlPrefix + "/" + ce.getId());
      }

//      String eventColor = "var(--bs-primary)";
      String eventColor = "#3788d8";
//      String cptColor = "var(--bs-success)";
      String cptColor = "#008000";

      ce.setBackgroundColor(eventColor);
      ce.setBorderColor(eventColor);

      if (e.isCpt()) {
        ce.setBackgroundColor(cptColor);
        ce.setBorderColor(cptColor);
      }

      if (e.isEvent()) {
        ce.setBackgroundColor(eventColor);
        ce.setBorderColor(eventColor);
      }

      calendarEvents.add(ce);
    }

    return calendarEvents;
  }
}
