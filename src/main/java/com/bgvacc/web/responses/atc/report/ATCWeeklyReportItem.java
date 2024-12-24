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
public class ATCWeeklyReportItem implements Serializable {

  private String name;
  private String callsign;
  private String time;

}
