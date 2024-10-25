package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.EventPositionsResponse;
import com.bgvacc.web.responses.events.EventResponse;
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

  List<EventResponse> getEvents(String sql);

  EventResponse getEvent(Long eventId);

  List<EventPositionsResponse> getEventPositions(Long eventId);

  void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data);
}
