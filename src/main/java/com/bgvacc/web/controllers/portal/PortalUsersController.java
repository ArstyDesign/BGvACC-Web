package com.bgvacc.web.controllers.portal;

import com.aarshinkov.datetimecalculator.domain.Time;
import com.bgvacc.web.api.vateud.VatEudCoreApi;
import com.bgvacc.web.base.Base;
import com.bgvacc.web.filters.UserFilter;
import com.bgvacc.web.models.portal.users.UserCreateModel;
import com.bgvacc.web.models.portal.users.UserSearchModel;
import com.bgvacc.web.responses.paging.PaginationResponse;
import com.bgvacc.web.responses.sessions.ControllerOnlineLogResponse;
import com.bgvacc.web.responses.users.*;
import com.bgvacc.web.responses.users.atc.PositionResponse;
import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
import com.bgvacc.web.services.*;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.MESSAGE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_ERROR_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.TITLE_SUCCESS_PLACEHOLDER;
import static com.bgvacc.web.utils.AppConstants.USER_LAST_CONNECTIONS_COUNT;
import static com.bgvacc.web.utils.AppConstants.USER_LAST_PARTICIPATION_EVENTS_COUNT;
import com.bgvacc.web.utils.Breadcrumb;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.ArrayList;
import java.util.List;
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
public class PortalUsersController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserService userService;

  private final ControllerOnlineLogService controllerOnlineLogService;

  private final VatEudCoreApi vatEudCoreApi;

  private final UserATCAuthorizedPositionsService userATCAuthorizedPositionsService;

  private final MailService mailService;

  private final int DEFAULT_PAGE_LIMIT = 10;

  @GetMapping("/portal/users")
  public String getUsers(@ModelAttribute("filter") UserFilter filter,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
                         HttpServletRequest request, Model model) {

    if (page <= 0) {
      return "redirect:/portal/users?page=1" + filter.getPagingParams();
    }

    if (limit <= 0) {
      return "redirect:/portal/users?limit=" + DEFAULT_PAGE_LIMIT + filter.getPagingParams();
    }

    model.addAttribute("limit", limit);

    PaginationResponse<UserResponse> users = userService.getUsers(page, limit, filter);

    List<SavedSearchUser> userSavedUserSearches = userService.getUserSavedUserSearches(getLoggedUserCid(request));
    
    for (SavedSearchUser usus : userSavedUserSearches) {
      for (UserResponse user : users.getItems()) {
        if (usus.getCid().equals(user.getCid())) {
          user.setIsSavedSearch(true);
        }
      }
    }
    
    model.addAttribute("users", users);

    model.addAttribute("allRoles", userService.getAllUserRoles());

    model.addAttribute("pageTitle", getMessage("portal.users.users.title"));
    model.addAttribute("page", "users");
    model.addAttribute("subpage", "users");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/users/users";
  }

  @GetMapping("/portal/users/{cid}")
  public String getUser(@PathVariable("cid") String cid, Model model) {

    UserResponse user = userService.getUser(cid);

    model.addAttribute("user", user);

    List<RoleResponse> unassignedRoles = userService.getUserUnassignedRoles(cid);

    model.addAttribute("unassignedRoles", unassignedRoles);

    model.addAttribute("lastNEventsParticipationCount", USER_LAST_PARTICIPATION_EVENTS_COUNT);

    List<ControllerOnlineLogResponse> controllerOnlineSessions = controllerOnlineLogService.getControllerOnlineSessions(cid, USER_LAST_CONNECTIONS_COUNT);

    model.addAttribute("controllerOnlineSessions", controllerOnlineSessions);

    List<ControllerOnlineLogResponse> completedSessions = controllerOnlineLogService.getControllerLastOnlineSessions(cid, USER_LAST_CONNECTIONS_COUNT, false);

    model.addAttribute("lastNConnectionsCount", USER_LAST_CONNECTIONS_COUNT);

    if (completedSessions != null && completedSessions.size() < USER_LAST_CONNECTIONS_COUNT && !completedSessions.isEmpty()) {
      model.addAttribute("lastNConnectionsCount", completedSessions.size());
    }

    long completedSessionsTotalTime = 0;

    for (ControllerOnlineLogResponse cs : completedSessions) {
      completedSessionsTotalTime += cs.getTotalSeconds();
    }

    Time totalTime = new Time(completedSessionsTotalTime);

    model.addAttribute("completedSessions", completedSessions);
    model.addAttribute("totalTime", totalTime);

    List<UserATCAuthorizedPositionResponse> userATCAuthorizedPositions = userATCAuthorizedPositionsService.getUserATCAuthorizedPositions(cid);
    model.addAttribute("userATCAuthorizedPositions", userATCAuthorizedPositions);

    List<PositionResponse> unauthorizedPositions = userATCAuthorizedPositionsService.getUnauthorizedPositionsForUser(cid);
    model.addAttribute("unauthorizedPositions", unauthorizedPositions);

    model.addAttribute("pageTitle", user.getNames().getFullName() + " - " + user.getCid());
    model.addAttribute("page", "users");
    model.addAttribute("subpage", "users");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), "/portal/users"));
    breadcrumbs.add(new Breadcrumb(user.getNames().getFullName(), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/users/user";
  }

  @PostMapping("/portal/users/{cid}/roles/add")
  public String addUserRole(@PathVariable("cid") String cid, @RequestParam("role") String role, RedirectAttributes redirectAttributes) {

    log.debug("CID: " + cid + ", Role: " + role);

    boolean isAdded = userService.addUserRole(cid, role);

    if (isAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.user.roles.add.role.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.user.roles.add.role.error"));
    }

    return "redirect:/portal/users/" + cid;
  }

  @PostMapping("/portal/users/{cid}/roles/delete")
  public String deleteUserRole(@PathVariable("cid") String cid, @RequestParam("role") String role, RedirectAttributes redirectAttributes) {

    log.debug("CID: " + cid + ", Role: " + role);

    boolean isRemoved = userService.removeUserRole(cid, role);

    if (isRemoved) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.user.roles.deleterole.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.user.roles.deleterole.error"));
    }

    return "redirect:/portal/users/" + cid;
  }

  @PostMapping("/portal/users/{cid}/position/add")
  public String addUserATCPosition(@PathVariable("cid") String cid, @RequestParam("position") String position, RedirectAttributes redirectAttributes) {

    log.debug("CID: " + cid + ", Position: " + position);

    boolean isAdded;

    if (position.equalsIgnoreCase("all")) {
      isAdded = userATCAuthorizedPositionsService.addAllUserATCPositions(cid);
    } else {
      isAdded = userATCAuthorizedPositionsService.addUserATCPosition(cid, position);
    }

    if (isAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.add.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.add.error"));
    }

    return "redirect:/portal/users/" + cid;
  }

  @PostMapping("/portal/users/{cid}/position/delete")
  public String deleteUserATCPosition(@PathVariable("cid") String cid, @RequestParam("position") String position, RedirectAttributes redirectAttributes) {

    log.debug("CID: " + cid + ", Position: " + position);

    boolean isRemoved = userATCAuthorizedPositionsService.removeUserATCPosition(cid, position);

    if (isRemoved) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.delete.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.delete.error"));
    }

    return "redirect:/portal/users/" + cid;
  }

  @PostMapping("/portal/users/{cid}/position/delete/all")
  public String deleteAllUserATCPosition(@PathVariable("cid") String cid, RedirectAttributes redirectAttributes) {

    boolean areRemoved = userATCAuthorizedPositionsService.removeAllUserATCPositions(cid);

    if (areRemoved) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.delete.all.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.user.authorizedpositions.delete.all.error"));
    }

    return "redirect:/portal/users/" + cid;
  }

  @GetMapping("/portal/users/new")
  public String prepareCreateUser(Model model) {

    model.addAttribute("pageTitle", getMessage("portal.users.create.title"));
    model.addAttribute("page", "users");
    model.addAttribute("subpage", "createUser");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), "/portal/users"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.users.create.title", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/users/createUser";
  }

  @GetMapping(value = "/portal/users/new", params = "search")
  public String searchVatsimUser(@RequestParam(name = "search") String search, Model model) {

    if (search == null || search.isEmpty()) {
      return "redirect:/portal/users/new";
    }

    log.debug("Search: " + search);
    model.addAttribute("searchedCid", search);

    Long cid;
    try {
      cid = Long.valueOf(search);
    } catch (NumberFormatException e) {
      model.addAttribute("error", getMessage("portal.users.create.error.cidnotvalid"));

      model.addAttribute("pageTitle", getMessage("portal.users.create.title"));
      model.addAttribute("page", "users");
      model.addAttribute("subpage", "createUser");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), "/portal/users"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.users.create.title", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "portal/users/createUser";
    }

    VatEudUser member = vatEudCoreApi.getMemberDetails(cid);

    if (member == null) {
      model.addAttribute("error", getMessage("portal.users.create.error.usernotfound", search));
    }

    model.addAttribute("member", member);

    model.addAttribute("pageTitle", getMessage("portal.users.create.title"));
    model.addAttribute("page", "users");
    model.addAttribute("subpage", "createUser");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), "/portal/users"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.users.create.title", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/users/createUser";
  }

  @PostMapping("/portal/users/new")
  public String createUser(@RequestParam("cid") String cid, RedirectAttributes redirectAttributes, Model model) {

    boolean doUserExist = userService.doUserExist(cid);

    VatEudUser member = vatEudCoreApi.getMemberDetails(Long.valueOf(cid));

    if (doUserExist) {

      model.addAttribute("error", getMessage("portal.users.create.error.userexists"));

      model.addAttribute("searchedCid", cid);
      model.addAttribute("member", member);

      model.addAttribute("pageTitle", getMessage("portal.users.create.title"));
      model.addAttribute("page", "users");
      model.addAttribute("subpage", "createUser");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.users.users", null, LocaleContextHolder.getLocale()), "/portal/users"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.users.create.title", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "portal/users/createUser";
    }

    log.info("Creating user with CID '" + cid + "'");

    Names names = Names.builder().firstName(member.getData().getFirstName()).lastName(member.getData().getLastName()).build();

    UserCreateModel ucm = new UserCreateModel();
    ucm.setCid(member.getData().getCid());
    ucm.setEmail(member.getData().getEmail());
    ucm.setEmailVatsim(member.getData().getEmail());
    ucm.setFirstName(member.getData().getFirstName());
    ucm.setLastName(member.getData().getLastName());
    ucm.setCurrentRating(member.getData().getRating());

    String generatedUserPassword = userService.createUser(ucm);

    boolean isSent = mailService.sendUserCreatedMail(names, member.getData().getCid(), member.getData().getEmail(), generatedUserPassword);

    if (isSent) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.create.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.create.error"));
    }

    return "redirect:/portal/users";
  }

  @PostMapping("/portal/users/{cidToBeSavedAsSavedSearch}/saved-search/add")
  public String saveCidToSavedSearches(@PathVariable("cidToBeSavedAsSavedSearch") String cidToBeSavedAsSavedSearch, RedirectAttributes redirectAttributes, HttpServletRequest request) {

    boolean isAdded = userService.addSavedUserSearch(getLoggedUserCid(request), cidToBeSavedAsSavedSearch);

    if (isAdded) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.users.saveassavedsearch.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.users.saveassavedsearch.modal.error"));
    }

    return "redirect:/portal/users";
  }

  @PostMapping("/portal/users/{cidToRemoveFromSavedSearch}/saved-search/remove")
  public String removeCidFromSavedSearches(@PathVariable("cidToRemoveFromSavedSearch") String cidToRemoveFromSavedSearch, RedirectAttributes redirectAttributes, HttpServletRequest request) {

    boolean isRemoved = userService.removeUserFromSavedSearches(getLoggedUserCid(request), cidToRemoveFromSavedSearch);

    if (isRemoved) {
      redirectAttributes.addFlashAttribute(TITLE_SUCCESS_PLACEHOLDER, getMessage("operation.success"));
      redirectAttributes.addFlashAttribute(MESSAGE_SUCCESS_PLACEHOLDER, getMessage("portal.users.users.removefromsavedseach.modal.success"));
    } else {
      redirectAttributes.addFlashAttribute(TITLE_ERROR_PLACEHOLDER, getMessage("operation.error"));
      redirectAttributes.addFlashAttribute(MESSAGE_ERROR_PLACEHOLDER, getMessage("portal.users.users.removefromsavedseach.modal.error"));
    }

    return "redirect:/portal/users";
  }

  @GetMapping("/users/{cid}")
  @ResponseBody
  public UserResponse getUserByCid(@PathVariable("cid") String cid) {
    return userService.getUser(cid);
  }
}
