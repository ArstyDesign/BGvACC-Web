package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.api.CoreApi;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.portal.trainings.CreateTrainingNoteModel;
import com.bgvacc.web.requests.portal.trainings.CreateTrainingNoteRequest;
import com.bgvacc.web.responses.trainings.CreateTrainingNoteResponse;
import com.bgvacc.web.security.LoggedUser;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidation;
import com.bgvacc.web.vatsim.members.VatsimMemberSoloValidations;
import com.bgvacc.web.vatsim.vateud.ControllerNotes;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class TrainingsController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final VatEudCoreApi vatEudCoreApi;

  private final CoreApi coreApi;

//  @RequestMapping(value = "/portal/trainings/notes", method = {RequestMethod.GET, RequestMethod.POST})
  @GetMapping("/portal/trainings/notes")
  public String getTrainingNotes(Model model) {

    model.addAttribute("pageTitle", getMessage("portal.trainings.notes.title"));
    model.addAttribute("page", "trainings");
    model.addAttribute("subpage", "notes");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.title", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.notes", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/trainings/notes";
  }

  @PostMapping("/portal/trainings/notes")
  public String getTrainingNotes(@RequestParam(name = "cid", defaultValue = "-1") Long cid, Model model) {

    log.debug("CID: " + cid);
    if (cid == null || cid == -1) {
      return "redirect:/portal/trainings/notes";
    }

    return "redirect:/portal/trainings/notes/" + cid;
  }

  @GetMapping("/portal/trainings/notes/{cid}")
  public String getTrainingNotesForUser(@PathVariable("cid") Long cid, Model model, RedirectAttributes redirectAttributes) {

    model.addAttribute("cid", cid);

    VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(cid);

    if (memberDetails == null) {

      redirectAttributes.addFlashAttribute("error", getMessage("portal.trainings.notes.cidinvalid", "" + cid));

      return "redirect:/portal/trainings/notes";
    }
    model.addAttribute("member", memberDetails);

    ControllerNotes memberNotes = vatEudCoreApi.getMemberNotes(cid);

    Collections.reverse(memberNotes.getNotes());

    for (ControllerNotes.ControllerNote note : memberNotes.getNotes()) {
      VatEudUser instructorDetails = vatEudCoreApi.getMemberDetails(note.getInstructorCid());

      Names instuctorName = Names.builder().firstName(instructorDetails.getData().getFirstName()).lastName(instructorDetails.getData().getLastName()).build();
      note.setInstructorName(instuctorName);
    }

    model.addAttribute("notes", memberNotes);

    model.addAttribute("pageTitle", getMessage("portal.trainings.notes.title.member", memberDetails.getData().getFullName(), memberDetails.getData().getCid()));
    model.addAttribute("page", "trainings");
    model.addAttribute("subpage", "notes");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.title", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.notes", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/trainings/notes";
  }

  @PostMapping("/portal/trainings/notes/{cid}")
  public String createTrainingNoteForUser(@PathVariable("cid") Long cid, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

    LoggedUser loggedUser = getLoggedUser(request);

    log.debug("Instructor CID: " + loggedUser.getCid());

    CreateTrainingNoteModel ctnm = new CreateTrainingNoteModel();
    ctnm.setUserCid(cid);
    ctnm.setInstructorCid(Long.valueOf(loggedUser.getCid()));
    ctnm.setPosition("LBSF_TWR");
    ctnm.setSessionType(0); // Sweatbox
    ctnm.setNote("Testing");

    CreateTrainingNoteResponse createdTrainingNote = vatEudCoreApi.createTrainingNote(ctnm);

    log.debug(createdTrainingNote.toString());

    return "redirect:/portal/trainings/notes/" + cid;
  }

  @GetMapping("/portal/trainings/solo-validations")
  public String getSoloValidations(Model model) {

    int sofiaControlSolos = 0;
    int sofiaApproachSolos = 0;
    int sofiaTowerSolos = 0;

    VatsimMemberSoloValidations memberSoloValidations = coreApi.getMemberSoloValidations();

    Map<Long, VatEudUser> mentors = new HashMap<>();

    for (VatsimMemberSoloValidation vmsv : memberSoloValidations.getData()) {
//      Calendar cal = Calendar.getInstance();
//      cal.setTime(new Timestamp(System.currentTimeMillis()));
//      cal.add(Calendar.DAY_OF_WEEK, 2);
//      vmsv.setExpiry(new Timestamp(cal.getTime().getTime()));
      VatEudUser memberDetails = vatEudCoreApi.getMemberDetails(vmsv.getUserCid());
      VatEudUser mentorDetails;

      if (!mentors.containsKey(vmsv.getInstructorCid())) {
        mentorDetails = vatEudCoreApi.getMemberDetails(vmsv.getInstructorCid());
        mentors.put(vmsv.getInstructorCid(), mentorDetails);
      } else {
        mentorDetails = mentors.get(vmsv.getInstructorCid());
      }

      vmsv.setFirstName(memberDetails.getData().getFirstName());
      vmsv.setLastName(memberDetails.getData().getLastName());

      vmsv.setInstructorFirstName(mentorDetails.getData().getFirstName());
      vmsv.setInstructorLastName(mentorDetails.getData().getLastName());

      if (vmsv.getPosition().equalsIgnoreCase("LBSR_CTR")) {
        sofiaControlSolos++;
      } else if (vmsv.getPosition().equalsIgnoreCase("LBSF_APP")) {
        sofiaApproachSolos++;
      } else {
        sofiaTowerSolos++;
      }
    }

    Collections.sort(memberSoloValidations.getData(), Comparator.comparing(VatsimMemberSoloValidation::getSoloRemainingDays).thenComparing(Comparator.comparing(VatsimMemberSoloValidation::getFullName)));

    model.addAttribute("soloValidations", memberSoloValidations);
    model.addAttribute("lbsrCtrSolos", sofiaControlSolos);
    model.addAttribute("lbsfAppSolos", sofiaApproachSolos);
    model.addAttribute("lbsfTwrSolos", sofiaTowerSolos);

    model.addAttribute("pageTitle", getMessage("portal.trainings.solo.title"));
    model.addAttribute("page", "trainings");
    model.addAttribute("subpage", "solo-validations");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.title", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.trainings.solo", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/trainings/solo-validations";
  }
}
