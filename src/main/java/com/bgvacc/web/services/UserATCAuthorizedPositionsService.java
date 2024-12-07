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
  
  boolean isPositionAuthorizedForUser(String position, String userCid);
  
  boolean hasUserAuthorizedPositions(String userCid);
  
  boolean addUserATCPosition(String cid, String position);
  
  boolean addAllUserATCPositions(String cid);
  
  boolean removeUserATCPosition(String cid, String position);
  
  boolean removeAllUserATCPositions(String cid);

  List<UserATCAuthorizedPositionResponse> getAllATCCIDsWithAuthorizedPosition(String position);

  List<UserATCAuthorizedPositionResponse> getUserATCAuthorizedPositions(String userCid);
}
