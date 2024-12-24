package com.bgvacc.web.responses.atc.report;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ColumnHeaderResponse implements Serializable {
  
  private int columnMostChars;
  private StringBuilder stringBuilder;

}
