package com.bgvacc.web.services;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.domains.Controllers;
import com.bgvacc.web.vatsim.memory.Memory;
import com.bgvacc.web.vatsim.roster.VatEudRoster;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class VatsimEudRosterServiceImpl implements VatsimEudRosterService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final CoreApi coreApi;

  private final VatEudCoreApi vatEudCoreApi;

  @Override
  public Controllers getRosterControllers() {

    Memory memory = Memory.getInstance();

    if (memory.areControllersNull()) {
      syncRosterControllers();
    }

    return memory.getControllers();
  }

  @Override
  public void syncRosterControllers() {

    Memory memory = Memory.getInstance();
    memory.clearControllers();

    VatEudRoster roster = coreApi.getRoster();

    for (Long controllerId : roster.getData().getControllersIds()) {

      VatEudUser md = vatEudCoreApi.getMemberDetails(controllerId);

      if (md != null) {
        memory.addController(md, md.getData().getRating());
      }
    }
  }
}
