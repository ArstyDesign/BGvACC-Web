package com.bgvacc.web.controllers.vatsim;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.vatsim.members.VatsimMemberDetails;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidation;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidations;
import com.bgvacc.web.vatsim.members.VatsimMemberTrainingStaff;
import com.bgvacc.web.vatsim.roster.VatEudRoster;
import com.bgvacc.web.vatsim.roster.VatEudRoster.VatEudRosterData.VatEudRosterController;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.controller;

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

  @Autowired
  private VatEudCoreApi vatEudCoreApi;

  @GetMapping("/members/{cid}")
  public String getMember(@PathVariable("cid") Long cid, Model model) {

    model.addAttribute("cid", cid);

    VatsimMemberDetails memberDetails = coreApi.getMemberDetails(cid);
    VatEudUser vatEudUserDetails = vatEudCoreApi.getMemberDetails(cid);

    if (memberDetails != null) {
      model.addAttribute("pageTitle", vatEudUserDetails.getData().getFullName() + " - " + cid);
      model.addAttribute("memberDetails", memberDetails);
      model.addAttribute("vatEudUserDetails", vatEudUserDetails);
      model.addAttribute("memberStatistics", coreApi.getMemberStatistics(cid));
    }

    return "vatsim/members/member";
  }

  @GetMapping("/members/staff")
  public String getStaff(Model model) {

    model.addAttribute("pageTitle", "Staff");

    VatEudRoster vatEudRoster = coreApi.getRoster();

    for (VatEudRoster.VatEudRosterData.VatEudRosterStaff vers : vatEudRoster.getData().getStaff().get(0)) {

      if (vers.getCid() != null) {
        VatEudUser md = vatEudCoreApi.getMemberDetails(vers.getCid());
        vers.setFirstName(md.getData().getFirstName());
        vers.setLastName(md.getData().getLastName());
      }
    }

    List<VatEudRoster.VatEudRosterData.VatEudRosterController> controllers = new ArrayList<>();

    // TODO uncomment
//    for (Long controllerCid : vatEudRoster.getData().getControllersIds()) {
//      VatEudUser md = vatEudCoreApi.getMemberDetails(controllerCid);
//
//      if (md != null) {
//
//        VatEudRosterController controller = new VatEudRosterController();
//        controller.setCid(controllerCid);
//        controller.setFirstName(md.getData().getFirstName());
//        controller.setLastName(md.getData().getLastName());
//
//        controllers.add(controller);
//      }
//    }
    vatEudRoster.getData().setControllers(controllers);

    model.addAttribute("roster", vatEudRoster);

    return "vatsim/members/staff";
  }

  @GetMapping("/members/solo-validations")
  public String getMembersSoloValidations(Model model) {

    model.addAttribute("pageTitle", "Solo validations");

    VatsimMemberSoloValidations memberSoloValidations = coreApi.getMemberSoloValidations();

    for (VatsimMemberSoloValidation vmsv : memberSoloValidations.getData()) {
      VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmsv.getUserCid());
      VatEudUser mentorDetails = vatEudCoreApi.getMemberDetails(vmsv.getInstructorCid());

      vmsv.setFirstName(memberDetails.getData().getFirstName());
      vmsv.setLastName(memberDetails.getData().getLastName());

      vmsv.setInstructorFirstName(mentorDetails.getData().getFirstName());
      vmsv.setInstructorLastName(mentorDetails.getData().getLastName());
    }

    model.addAttribute("soloValidations", memberSoloValidations);

    return "vatsim/members/solo-validations";
  }

  @GetMapping("/members/training-staff")
  public String getTrainingStaff(Model model) {

    VatsimMemberTrainingStaff trainingStaff = vatEudCoreApi.getTrainingStaff();

    for (VatsimMemberTrainingStaff.VatsimMemberTrainingStaffData vmtsd : trainingStaff.getData()) {
      VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmtsd.getCid());

      vmtsd.setFirstName(memberDetails.getData().getFirstName());
      vmtsd.setLastName(memberDetails.getData().getLastName());
      vmtsd.setRating(memberDetails.getData().getRating());
    }

    model.addAttribute("trainingStaff", trainingStaff);

//    for (VatsimMemberSoloValidation vmsv : trainingStaff.getData()) {
//      VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmsv.getUserCid());
//    }
    model.addAttribute("pageTitle", "Training staff");

    return "vatsim/members/training-staff";
  }
}
