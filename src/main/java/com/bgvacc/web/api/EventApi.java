package com.bgvacc.web.api;

import static com.bgvacc.web.api.APIConstants.VATEUD_API_KEY_HEADER_KEY;
import com.bgvacc.web.configurations.AuthenticationProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class EventApi extends Api {

  @Autowired
  private AuthenticationProperties authProps;

  public VatsimEvents getVatsimEvents() {
    String url = "https://core.vateud.net/api/facility/events";
    return doRequest(Methods.GET, url, null, VatsimEvents.class, null, VATEUD_API_KEY_HEADER_KEY, authProps.getVatEudApiKey());
  }

//  public VatsimEvents getVatsimEventsByDivision(String division) {
//    String url = "https://my.vatsim.net/api/v2/events/view/division/" + division;
//    return doRequest(Methods.GET, url, null, VatsimEvents.class);
//  }
//
//  public VatsimEvent getVatsimEvent(String eventId) {
//    String url = "https://my.vatsim.net/api/v2/events/view/" + eventId;
//    return doRequest(Methods.GET, url, null, VatsimEvent.class);
//  }
}
