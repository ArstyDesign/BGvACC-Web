package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class VatsimMembersController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private CoreApi coreApi;

  @GetMapping("/members/{cid}")
  public String getMember(@PathVariable("cid") String cid, Model model) {

    model.addAttribute("cid", cid);

    VatsimMemberDetails memberDetails = coreApi.getMemberDetails(cid);

    if (memberDetails != null) {
      model.addAttribute("pageTitle", cid);
      model.addAttribute("memberDetails", memberDetails);
      model.addAttribute("memberStatistics", coreApi.getMemberStatistics(cid));
    }

    return "vatsim/members/member";
  }

  @GetMapping("/members/solo-validations")
  public String getMembersSoloValidations(Model model) {
    
    model.addAttribute("pageTitle", "Solo validations");
    
    model.addAttribute("soloValidations", coreApi.getMemberSoloValidations());

    return "vatsim/members/solo-validations";
  }
}
