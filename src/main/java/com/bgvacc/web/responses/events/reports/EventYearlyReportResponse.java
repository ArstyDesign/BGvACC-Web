package com.bgvacc.web.responses.events.reports;

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
public class EventYearlyReportResponse implements Serializable {
  
  private Integer month;
  private String type;
  private Integer count;

}
