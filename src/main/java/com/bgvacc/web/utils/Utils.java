package com.bgvacc.web.utils;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.CalendarEvent;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import com.bgvacc.web.responses.events.EventResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class Utils extends Base {

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

  public static List<CalendarEvent> convertATCReservationsToCalendarEvents(List<ATCReservationResponse> atcReservations, MessageSource messageSource) {
    return convertATCReservationsToCalendarEvents(atcReservations, "atc-reservations", messageSource);
  }

  public static List<CalendarEvent> convertATCReservationsToCalendarEvents(List<ATCReservationResponse> atcReservations, String urlPrefix, MessageSource messageSource) {

    List<CalendarEvent> calendarEvents = new ArrayList<>();

    for (ATCReservationResponse atcr : atcReservations) {

      CalendarEvent ce = new CalendarEvent();

      ce.setId(urlPrefix + "/" + atcr.getReservationId());

      if (atcr.getReservationType().equalsIgnoreCase("training")) {
        // Training session
//        messageSource.getMessage("fsd");

        String title = "";

        try {
          title = messageSource.getMessage("calendar.trainingsession", new Object[]{atcr.getPosition(), atcr.getTraineeNames().getFullName(), atcr.getTraineeCid(), atcr.getUserNames().getFullNameWithShortFirst(), atcr.getUserCid()}, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {

        }
        ce.setTitle(title);

        ce.setBackgroundColor("#ff9300");
        ce.setBorderColor("#ff9300");
      } else {
        // Normal
        ce.setTitle(atcr.getPosition() + " online");

        ce.setBackgroundColor("#410037");
        ce.setBorderColor("#410037");
      }

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      String startDate = atcr.getFromTime().getZoneTime().format(formatter);
      String endDate = atcr.getToTime().getZoneTime().format(formatter);

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
