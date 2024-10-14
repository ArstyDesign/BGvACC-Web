package com.bgvacc.web.services;

import com.bgvacc.web.responses.sessions.NotCompletedControllerSession;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface ControllerOnlineLogService {

  List<NotCompletedControllerSession> getNotCompletedControllerSessions();

  boolean endControllerSessionWithId(String controllerOnlineLogId);

}
