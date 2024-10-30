package com.bgvacc.web.responses.sessions;

import com.aarshinkov.datetimecalculator.domain.Time;
import java.io.Serializable;
import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@ToString
public class ControllersOnlineReportResponse implements Serializable {

  private List<ControllersOnlineReportItemResponse> towerPositions;
  private List<ControllersOnlineReportItemResponse> approachPositions;
  private List<ControllersOnlineReportItemResponse> controlPositions;
  private int cidColumnMaxWidth;
  private int namesColumnMaxWidth;

  public Time getTotalTimeControlled() {
    return new Time(getTotalSecondsControlled());
  }

  public Long getTotalSecondsControlled() {

    Long totalSecondsControlled = 0L;

    if (towerPositions != null) {
      for (ControllersOnlineReportItemResponse tp : towerPositions) {
        totalSecondsControlled += tp.getSecondsControlled();
      }
    }

    if (approachPositions != null) {
      for (ControllersOnlineReportItemResponse tp : approachPositions) {
        totalSecondsControlled += tp.getSecondsControlled();
      }
    }

    if (controlPositions != null) {
      for (ControllersOnlineReportItemResponse tp : controlPositions) {
        totalSecondsControlled += tp.getSecondsControlled();
      }
    }

    return totalSecondsControlled;
  }

  public List<ControllersOnlineReportItemResponse> getTowerPositions() {
    return towerPositions;
  }

  public void setTowerPositions(List<ControllersOnlineReportItemResponse> towerPositions) {
    this.towerPositions = towerPositions;
  }

  public List<ControllersOnlineReportItemResponse> getApproachPositions() {
    return approachPositions;
  }

  public void setApproachPositions(List<ControllersOnlineReportItemResponse> approachPositions) {
    this.approachPositions = approachPositions;
  }

  public List<ControllersOnlineReportItemResponse> getControlPositions() {
    return controlPositions;
  }

  public void setControlPositions(List<ControllersOnlineReportItemResponse> controlPositions) {
    this.controlPositions = controlPositions;
  }

  public int getCidColumnMaxWidth() {
    return cidColumnMaxWidth;
  }

  public void setCidColumnMaxWidth(int cidColumnMaxWidth) {
    this.cidColumnMaxWidth = cidColumnMaxWidth;
  }

  public int getNamesColumnMaxWidth() {
    return namesColumnMaxWidth;
  }

  public void setNamesColumnMaxWidth(int namesColumnMaxWidth) {
    this.namesColumnMaxWidth = namesColumnMaxWidth;
  }
}
