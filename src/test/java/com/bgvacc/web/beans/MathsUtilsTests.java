package com.bgvacc.web.beans;

import com.bgvacc.web.utils.MathsUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
public class MathsUtilsTests {
  
  private MathsUtils mathsUtils;

  @BeforeEach
  public void init() {
    mathsUtils = new MathsUtils();
  }
  
  @Test
  public void test() {
    int result = mathsUtils.lcmOfArray(1, 2, 3);
    
    Assertions.assertEquals(6, result);
    
    result = mathsUtils.lcmOfArray(2, 4);
    
    Assertions.assertEquals(4, result);
  }
}
