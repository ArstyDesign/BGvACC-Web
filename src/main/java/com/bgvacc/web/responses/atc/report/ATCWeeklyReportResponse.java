package com.bgvacc.web.responses.atc.report;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class ATCWeeklyReportResponse implements Serializable {

  private List<ATCWeeklyReportItem> atcTower = new ArrayList<>();
  private List<ATCWeeklyReportItem> atcApproach = new ArrayList<>();
  private List<ATCWeeklyReportItem> atcControl = new ArrayList<>();
}
