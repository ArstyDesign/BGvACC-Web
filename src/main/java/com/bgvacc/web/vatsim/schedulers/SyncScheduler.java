package com.bgvacc.web.vatsim.schedulers;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.vatsim.atc.VatsimATC;
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

  @Scheduled(fixedRate = 20000)
  public void syncLiveATCs() {
    log.debug("Syncing live ATCs...");

    Memory memory = Memory.getInstance();
    memory.clearAndAddATCs(coreApi.getAllOnlineControllers());

    // TODO testing
//    if (memory.isAdded) {
//      memory.addATC(new VatsimATC(1664545L, "LBSR_CTR", "1", 5, "Emil", "Ivanov"));
//      memory.addATC(new VatsimATC(1779345L, "LBSF_TWR", "1", 2, "Theo", "Delmas"));
//      memory.addATC(new VatsimATC(1720051L, "LBSF_APP", "1", 4, "Atanas", "Arshinkov"));
//      memory.addATC(new VatsimATC(1773453L, "LBBG_TWR", "1", 3, "Kristian", "Hristov"));
//    }
  }
}
