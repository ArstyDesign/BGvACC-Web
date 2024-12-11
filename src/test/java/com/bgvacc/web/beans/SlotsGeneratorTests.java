package com.bgvacc.web.beans;

import com.bgvacc.web.domains.Slot;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class SlotsGeneratorTests {

  private SlotsGenerator slotsGenerator;

  @BeforeEach
  public void init() {
    slotsGenerator = new SlotsGenerator();
  }

  @Test
  public void test_one() {

    LocalDateTime start = LocalDateTime.of(2024, 12, 10, 20, 0);
    LocalDateTime end = LocalDateTime.of(2024, 12, 10, 22, 0);
    int slotsCount = 4;

    List<Slot> slots = slotsGenerator.generateSlots(start, end, slotsCount);

    Assertions.assertNotNull(slots);
    Assertions.assertEquals(4, slots.size());
  }

  @Test
  public void test_two() {

    LocalDateTime start = LocalDateTime.of(2024, 12, 10, 20, 0);
    LocalDateTime end = LocalDateTime.of(2024, 12, 10, 21, 30); // 90 минути
    int slotsCount = 4;

    List<Slot> slots = slotsGenerator.generateSlots(start, end, slotsCount);

    Assertions.assertNotNull(slots);
    Assertions.assertEquals(4, slots.size());
  }
}
