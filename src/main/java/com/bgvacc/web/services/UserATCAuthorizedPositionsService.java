package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.atc.UserATCAuthorizedPositionResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface UserATCAuthorizedPositionsService {

  List<UserATCAuthorizedPositionResponse> getAllATCCIDsWithAuthorizedPosition(String position);

  List<UserATCAuthorizedPositionResponse> getUserATCAuthorizedPositions(String userCid);
}
