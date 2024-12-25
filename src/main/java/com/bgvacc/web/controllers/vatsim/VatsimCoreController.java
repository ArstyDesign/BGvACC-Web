package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.vateud.VatEudCoreApi;
import static com.bgvacc.web.utils.ControllerPositionUtils.getPositionFrequency;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.atc.VatsimATCInfo;
import com.bgvacc.web.vatsim.memory.Memory;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class VatsimCoreController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private VatEudCoreApi vatEudCoreApi;

  @GetMapping("/atc/status/{atc}")
  public String getCallsignStatusCard(@PathVariable("atc") String atc, Model model) {

    final String callsign = atc.replace("-", "_").toUpperCase();

    VatsimATC position = getOnlinePosition(callsign);
//    VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmsv.getUserCid());

    VatsimATCInfo atcInfo = getPositionFrequency(callsign);

    model.addAttribute("atcInfo", atcInfo);
    model.addAttribute("position", position);

    return "atc/controller-status :: #atcStatus";
  }

  @GetMapping("/atc/bg-online/count")
  @ResponseBody
  public Long getOnlineBGControllersCount() {
    Memory memory = Memory.getInstance();
    return Long.valueOf(memory.getOnlineATCListSize(true));
  }

  private VatsimATC getOnlinePosition(String position) {

    for (VatsimATC onlineBulgarianATC : getAllVatsimBGOnlineATCs()) {
      if (onlineBulgarianATC.getCallsign().equalsIgnoreCase(position)) {

        VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(onlineBulgarianATC.getId());

        if (memberDetails != null) {
          onlineBulgarianATC.setFirstName(memberDetails.getData().getFirstName());
          onlineBulgarianATC.setLastName(memberDetails.getData().getLastName());
        }
        return onlineBulgarianATC;
      }
    }

    return null;
  }

  public List<VatsimATC> getAllVatsimBGOnlineATCs() {
    Memory memory = Memory.getInstance();
    return memory.getAllOnlineATCsList(true);
  }

  private List<VatsimATC> getAllVatsimOnlineATCs() {

    Memory memory = Memory.getInstance();

    return memory.getAllOnlineATCsList();
  }

//  @GetMapping("/atc/bg-online")
//  @ResponseBody
//  private List<VatsimATC> getAllVatsimBulgarianOnlineATCs() {
//
//    return getAllVatsimBGOnlineATCs();
//  }
}
