package com.bgvacc.web.services;

import com.bgvacc.web.responses.events.*;
import com.bgvacc.web.responses.events.reports.EventsYearlyReportResponse;
import com.bgvacc.web.responses.users.UserEventApplicationResponse;
import com.bgvacc.web.responses.users.UserEventApplicationsCountResponse;
import com.bgvacc.web.vatsim.events.VatsimEventData;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface EventService {

  List<EventResponse> getAllEvents();

  List<EventResponse> getPastEvents();

  List<EventResponse> getUpcomingEvents();

  List<EventResponse> getEventsBeforeAfterNow(String sql);

  UpcomingEventsResponse getUpcomingEventsAfterDays(Integer days);

  EventResponse getEvent(Long eventId);

  List<EventPositionResponse> getEventPositions(Long eventId);
  
  String getPositionFromEventPositionId(String eventPositionId);
  
  boolean addAirportToEvent(Long eventId, String airport);
  
  boolean removeAirportFromEvent(Long eventId, String airport);

  boolean addEventPosition(Long eventId, String position, Integer minimumRating, boolean canTraineesApply);

  boolean removeEventPosition(Long eventId, String eventPositionId);

  boolean addSlotsToPosition(Long eventId, String eventPositionId, Integer slotsCount);

  void synchroniseVatsimEventsToDatabase(List<VatsimEventData> data);

  Long getTotalUserEventApplications(String cid);
  
  UserEventApplicationsCountResponse getUserEventApplicationsCount(String cid);
  
  List<UserEventApplicationResponse> getUserEventApplications(String cid);

  EventsYearlyReportResponse getEventsYearlyReportForYear(Integer year);
  
  EventSlotResponse getEventSlot(String slotId);

  boolean canUserApplyForPosition(String cid, String eventPositionId);

  boolean hasUserAlreadyAppliedForSlot(String cid, String slotId);

  boolean applyUserForEventSlot(String cid, String slotId, boolean isAddedByStaff, String addedByStaffCid);

  boolean approveSlotApplication(Long eventId, String slotId, String applicationId);

  boolean rejectSlotApplication(String slotId, String applicationId, String rejectReason);
}
