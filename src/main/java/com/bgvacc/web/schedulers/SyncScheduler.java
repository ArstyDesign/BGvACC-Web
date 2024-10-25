package com.bgvacc.web.schedulers;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.EventApi;
import com.bgvacc.web.api.discord.DiscordNotifyApi;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.responses.sessions.*;
import com.bgvacc.web.services.*;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.events.VatsimEvents;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import com.bgvacc.web.vatsim.memory.Memory;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
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
public class SyncScheduler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final CoreApi coreApi;

  private final VatsimEudRosterService vatsimEudRosterService;

  private final EventApi eventApi;

  private final EventService eventService;

  private final ControllerOnlineLogService controllerOnlineLogService;

  private final VatEudCoreApi vatEudCoreApi;

  private final DiscordNotifyApi discordNotifyApi;

//  @Scheduled(fixedRate = 30000)
//  public void syncLiveATCs() {
//
//    log.info("Syncing live ATCs...");
//
//    Memory memory = Memory.getInstance();
//
//    List<VatsimATC> allOnlineControllers = coreApi.getAllOnlineControllers();
//    log.info("Online ATC: " + allOnlineControllers.size());
//
//    memory.clearAndAddATCs(allOnlineControllers);
//
//    // TODO testing
////    if (memory.isAdded) {
////      memory.addATC(new VatsimATC(1664545L, "LBSR_CTR", "1", 5, "Emil", "Ivanov"));
////      memory.addATC(new VatsimATC(1779345L, "LBSF_TWR", "1", 2, "Theo", "Delmas"));
////      memory.addATC(new VatsimATC(1720051L, "LBSF_APP", "1", 4, "Atanas", "Arshinkov"));
////      memory.addATC(new VatsimATC(1773453L, "LBBG_TWR", "1", 3, "Kristian", "Hristov"));
////    }
//  }
  @Scheduled(cron = "15,45 * * * * *", zone = "UTC")
  public void syncLiveATCs() {

    log.info("Syncing live ATCs...");

    Memory memory = Memory.getInstance();

    List<VatsimATC> allOnlineControllers = coreApi.getAllOnlineControllers();

    if (allOnlineControllers == null || allOnlineControllers.isEmpty()) {
      log.info("ATC online list is empty. Skipping...");
      return;
    }

    memory.clearAndAddATCs(allOnlineControllers);

    log.info("Online ATC: " + memory.getOnlineATCListSize());
    log.info("Online Bulgarian ATC: " + memory.getOnlineATCListSize(true));

    checkAndEndClosedSessions();
    checkAndOpenNewSessions();
  }

  private void checkAndEndClosedSessions() {

    log.info("Checking for closed sessions to end...");

    Memory memory = Memory.getInstance();

    List<NotCompletedControllerSession> notCompletedControllerSessions = controllerOnlineLogService.getNotCompletedControllerSessions();

    if (!notCompletedControllerSessions.isEmpty()) {

      List<VatsimATC> allBulgarianOnlineATCsList = memory.getAllOnlineATCsList(true);

      for (NotCompletedControllerSession nccs : notCompletedControllerSessions) {

        boolean isFinished = true;

        for (VatsimATC vatsimATC : allBulgarianOnlineATCsList) {
          if (nccs.getCid().equals(String.valueOf(vatsimATC.getId())) && nccs.getPosition().equalsIgnoreCase(vatsimATC.getCallsign())) {
            isFinished = false;
            break;
          }
        }

        if (isFinished) {
          ClosedControllerSession ccs = controllerOnlineLogService.endControllerSessionWithId(nccs.getControllerOnlineId(), nccs.getPosition());

          if (ccs != null) {
            discordNotifyApi.notifyDiscordForClosedOnlineController(ccs);
          }
        }
      }
    }
  }

  private void checkAndOpenNewSessions() {
    log.info("Checking for new sessions to open...");

    Memory memory = Memory.getInstance();

    List<VatsimATC> allBulgarianOnlineATCsList = memory.getAllOnlineATCsList(true);

    for (VatsimATC vatsimATC : allBulgarianOnlineATCsList) {
      NewlyOpenedControllerSession openNewControllerSession = controllerOnlineLogService.openNewControllerSession(vatsimATC);
//      log.debug("New session: " + openNewControllerSession);

      if (openNewControllerSession != null) {

        VatEudUser member = vatEudCoreApi.getMemberDetails(openNewControllerSession.getCid());
        Names names = Names.builder().firstName(member.getData().getFirstName()).lastName(member.getData().getLastName()).build();
        openNewControllerSession.setControllerNames(names);

        discordNotifyApi.notifyDiscordForNewOnlineController(openNewControllerSession);
      }
    }
  }

  @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
  public void syncRosterControllers() {

    log.info("Syncing roster controllers...");

    vatsimEudRosterService.getRosterControllers();
  }

//  @Scheduled(fixedRate = 60000)
  @Scheduled(cron = "0 0,30 * * * *", zone = "UTC")
  public void syncEvents() {

    log.info("Syncing events...");

    VatsimEvents vatsimEvents = eventApi.getVatsimEvents();

    eventService.synchroniseVatsimEventsToDatabase(vatsimEvents.getData());
  }
}
