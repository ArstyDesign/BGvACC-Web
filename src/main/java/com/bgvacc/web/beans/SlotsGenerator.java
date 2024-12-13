package com.bgvacc.web.beans;

import com.bgvacc.web.domains.Slot;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class SlotsGenerator {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public List<Slot> generateSlots(LocalDateTime start, LocalDateTime end, int slotsCount) {

    List<Slot> slots = new ArrayList<>();

    Duration duration = Duration.between(start, end);

    long durationInMinutes = duration.toMinutes();

    long oneSlotDurationInMinutes = durationInMinutes / slotsCount;
    long remainingMinutes = durationInMinutes % slotsCount;

    LocalDateTime startTime = start;
    LocalDateTime endTime;

    for (int i = 0; i < slotsCount; i++) {
      
      endTime = startTime.plusMinutes(oneSlotDurationInMinutes);

      if (i == slotsCount - 1) {
        endTime = endTime.plusMinutes(remainingMinutes);
      }

      Slot slot = new Slot(startTime, endTime);
      slots.add(slot);

      startTime = endTime;
    }

    return slots;
  }
  
  public String test(int slotsCount) {
    return "Hello: " + slotsCount;
  }
}
