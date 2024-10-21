package com.bgvacc.web.services;

import com.bgvacc.web.responses.sessions.ControllerOnlineLogResponse;
import com.bgvacc.web.responses.sessions.NotCompletedControllerSession;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface ControllerOnlineLogService {

  List<ControllerOnlineLogResponse> getControllerLastOnlineSessions(String cid, int numberOfConnections, boolean shouldIncludeNonCompleted);
  
  List<ControllerOnlineLogResponse> getControllerOnlineSessions(String cid, int numberOfConnections);

  List<NotCompletedControllerSession> getNotCompletedControllerSessions();

  boolean openNewControllerSession(VatsimATC onlineAtc);

  boolean endControllerSessionWithId(String controllerOnlineLogId);

}
