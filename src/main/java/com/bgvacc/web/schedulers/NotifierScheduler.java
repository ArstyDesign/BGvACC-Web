package com.bgvacc.web.schedulers;

import com.bgvacc.web.api.discord.DiscordNotifyApi;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.responses.events.UpcomingEventsResponse;
import com.bgvacc.web.responses.sessions.ControllersOnlineReportResponse;
import com.bgvacc.web.services.ControllerOnlineLogService;
import com.bgvacc.web.services.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class NotifierScheduler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final EventService eventService;

  private final ControllerOnlineLogService controllerOnlineLogService;
  
  private final DiscordNotifyApi discordNotifyApi;

//  @Scheduled(fixedRate = 15000)
//  @Scheduled(cron = "0 0,30 * * * *", zone = "UTC")
  public void notifyForUpcomingEvents() throws JsonProcessingException {
    log.debug("Notify for new events");

    UpcomingEventsResponse upcomingEvents = eventService.getUpcomingEventsAfterDays(5);

    if (upcomingEvents != null) {
      if (upcomingEvents.getUpcomingEvents() != null) {
        log.debug("Upcoming events not null");
        if (!upcomingEvents.getUpcomingEvents().isEmpty()) {
          discordNotifyApi.notifyForUpcomingEvents(upcomingEvents);
        }
      }
    }
  }
  
//  @Scheduled(fixedRate = 15000)
  public void sendLastWeekControllersLogReport() {
    
    ControllersOnlineReportResponse report = controllerOnlineLogService.getControllersOnlinePastWeekReport();
    
    
  }
}
