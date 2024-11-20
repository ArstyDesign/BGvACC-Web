package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.atc.PositionResponse;
import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserATCAuthorizedPositionsService {
  
  List<PositionResponse> getAllPositions();
  
  List<PositionResponse> getUnauthorizedPositionsForUser(String userCid);
  
  boolean addUserATCPosition(String cid, String position);
  
  boolean removeUserATCPosition(String cid, String position);

  List<UserATCAuthorizedPositionResponse> getAllATCCIDsWithAuthorizedPosition(String position);

  List<UserATCAuthorizedPositionResponse> getUserATCAuthorizedPositions(String userCid);
}
