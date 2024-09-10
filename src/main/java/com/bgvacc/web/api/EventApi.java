package com.bgvacc.web.api;

import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.events.VatsimEvent;
import com.bgvacc.web.vatsim.events.VatsimEvents;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class EventApi extends Api {

  public VatsimEvents getVatsimEventsByDivision(String division) {
    String url = "https://my.vatsim.net/api/v2/events/view/division/" + division;
    return doRequest(Methods.GET, url, null, VatsimEvents.class);
  }

  public VatsimEvent getVatsimEvent(String eventId) {
    String url = "https://my.vatsim.net/api/v2/events/view/" + eventId;
    return doRequest(Methods.GET, url, null, VatsimEvent.class);
  }
}
