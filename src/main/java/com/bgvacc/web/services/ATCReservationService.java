package com.bgvacc.web.services;

import com.bgvacc.web.models.atcreservations.CreateATCReservationModel;
import com.bgvacc.web.responses.atc.ATCReservationResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface ATCReservationService {

  List<ATCReservationResponse> getAllFutureATCReservations();
  
  boolean createNewATCReservation(CreateATCReservationModel carm);
  
  boolean isPositionFreeForTimeSlot(String position, LocalDateTime startTime, LocalDateTime endTime);
  
  boolean isThereAnEventForTimeSlot(LocalDateTime startTime, LocalDateTime endTime);
  
  boolean hasUserReservedAnotherPositionForTime(String userCid, LocalDateTime startTime, LocalDateTime endTime);
}
