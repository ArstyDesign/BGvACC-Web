package com.bgvacc.web.responses.sessions;

import com.aarshinkov.datetimecalculator.domain.Time;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@ToString
public class ControllersOnlineReportResponse implements Serializable {

  private List<ControllersOnlineReportItemResponse> towerPositions = new ArrayList<>();
  private List<ControllersOnlineReportItemResponse> approachPositions = new ArrayList<>();
  private List<ControllersOnlineReportItemResponse> controlPositions = new ArrayList<>();

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
  
  public Time getTotalTimeControlledTower() {
    return new Time(getTotalSecondsControlledTower());
  }

  public Long getTotalSecondsControlledTower() {

    Long totalSecondsControlledTower = 0L;

    if (towerPositions != null) {
      for (ControllersOnlineReportItemResponse tp : towerPositions) {
        totalSecondsControlledTower += tp.getSecondsControlled();
      }
    }

    return totalSecondsControlledTower;
  }
  
  public Time getTotalTimeControlledApproach() {
    return new Time(getTotalSecondsControlledApproach());
  }

  public Long getTotalSecondsControlledApproach() {

    Long totalSecondsControlledApproach = 0L;

    if (approachPositions != null) {
      for (ControllersOnlineReportItemResponse ap : approachPositions) {
        totalSecondsControlledApproach += ap.getSecondsControlled();
      }
    }

    return totalSecondsControlledApproach;
  }
  
  public Time getTotalTimeControlledControl() {
    return new Time(getTotalSecondsControlledControl());
  }

  public Long getTotalSecondsControlledControl() {

    Long totalSecondsControlledControl = 0L;

    if (controlPositions != null) {
      for (ControllersOnlineReportItemResponse cp : controlPositions) {
        totalSecondsControlledControl += cp.getSecondsControlled();
      }
    }

    return totalSecondsControlledControl;
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
}
