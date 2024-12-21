package com.bgvacc.web.services;

import com.bgvacc.web.responses.paging.PaginationResponse;
import com.bgvacc.web.responses.sessions.*;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface ControllerOnlineLogService {

  PaginationResponse<ControllerOnlineLogResponse> getControllerSessions(int page, int limit, String cid);

  List<ControllerOnlineLogResponse> getControllerLastOnlineSessions(String cid, int numberOfConnections, boolean shouldIncludeNonCompleted);

  List<ControllerOnlineLogResponse> getControllerOnlineSessions(String cid, int numberOfConnections);

  List<NotCompletedControllerSession> getNotCompletedControllerSessions();

  List<ControllerOnlineLogResponse> getAllControllerSessions(String cid, int numberOfConnections);

  NewlyOpenedControllerSession openNewControllerSession(VatsimATC onlineAtc);

  ClosedControllerSession endControllerSessionWithId(String controllerOnlineLogId, String callsign);

  ControllersOnlineReportResponse getControllersOnlinePastWeekReport();

  Long getTotalATCSessionsForUser(String cid);
}
