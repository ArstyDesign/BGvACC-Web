package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.*;
import com.bgvacc.web.responses.events.reports.EventsYearlyReportResponse;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface EventService {

  List<EventResponse> getAllEvents();

  List<EventResponse> getPastEvents();

  List<EventResponse> getUpcomingEvents();

  List<EventResponse> getEventsBeforeAfterNow(String sql);

  UpcomingEventsResponse getUpcomingEventsAfterDays(Integer days);

  EventResponse getEvent(Long eventId);

  List<EventPositionsResponse> getEventPositions(Long eventId);
  
  boolean addEventPosition(Long eventId, String position, Integer minimumRating);

  void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data);

  Long getTotalUserEventApplications(String cid);

  EventsYearlyReportResponse getEventsYearlyReportForYear(Integer year);
}
