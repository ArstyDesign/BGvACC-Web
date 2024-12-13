package com.bgvacc.web.utils;

/**
 *
 * @author Atanas Yordanov Arshinkove
 * @since 1.0.0
 */
public class MathsUtils {

  public int gcd(int a, int b) {
    while (b != 0) {
      int temp = b;
      b = a % b;
      a = temp;
    }
    return a;
  }

  // Метод за намиране на LCM (най-малко общо кратно) на неограничен брой числа
  public int lcmOfArray(int... numbers) {
    
    if (numbers.length == 0) {
      return 0;
    }
    
    int lcm = numbers[0]; // Започваме с първото число от масива
    for (int i = 1; i < numbers.length; i++) {
      lcm = lcm(lcm, numbers[i]); // Изчисляваме LCM на текущия резултат с следващото число
    }
    return lcm;
  }

  // Метод за намиране на LCM на два числа
  public int lcm(int a, int b) {
    return Math.abs(a * b) / gcd(a, b);
  }
}
