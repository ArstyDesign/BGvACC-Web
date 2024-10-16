package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface EventService {

  List<EventResponse> getEvents();

  void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data);
}
