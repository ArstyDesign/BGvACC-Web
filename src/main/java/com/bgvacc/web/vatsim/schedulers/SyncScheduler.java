package com.bgvacc.web.vatsim.schedulers;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.vatsim.memory.Memory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class SyncScheduler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private CoreApi coreApi;

  @Scheduled(fixedRate = 30000)
  public void syncLiveATCs() {
    log.debug("Syncing live ATCs...");

    Memory memory = Memory.getInstance();
    memory.clearAndAddATCs(coreApi.getAllOnlineControllers());
  }
}
