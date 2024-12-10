package com.bgvacc.web.services;

import com.bgvacc.web.responses.users.atc.PositionResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
public interface PositionService {
  
  List<PositionResponse> getPositionsExcept(String... excludedPositions);
  
  List<PositionResponse> getPositionsNotAssignedForEvent(Long eventId);

}
