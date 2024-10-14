package com.bgvacc.web.vatsim.memory;

import com.bgvacc.web.domains.Controllers;
import static com.bgvacc.web.utils.AppConstants.BULGARIAN_PREFIX;
import com.bgvacc.web.vatsim.atc.VatsimATC;
import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class Memory {

  private static Memory instance;

  private final List<VatsimATC> onlineATCList;
  private final List<VatsimATC> onlineBGATCList;
  private Controllers controllers;

  public boolean isAdded = false;

  private Memory() {
    this.onlineATCList = new ArrayList<>();
    this.onlineBGATCList = new ArrayList<>();
//    this.controllers = new Controllers();
  }

  public static synchronized Memory getInstance() {
    if (instance == null) {
      instance = new Memory();
    }
    return instance;
  }

  public void addATC(VatsimATC atc) {
    onlineATCList.add(atc);
    if (atc.getCallsign().startsWith(BULGARIAN_PREFIX)) {
      this.onlineBGATCList.add(atc);
    }
  }

  public void addATCs(List<VatsimATC> onlineATCList, boolean shouldClearFirst) {
    if (shouldClearFirst) {
      clearAndAddATCs(onlineATCList);
    } else {
      addATCs(onlineATCList);
    }
  }

  public void addATCs(List<VatsimATC> onlineATCList) {

    this.onlineATCList.addAll(onlineATCList);

    for (VatsimATC vatsimATC : onlineATCList) {
      if (vatsimATC.getCallsign().startsWith(BULGARIAN_PREFIX)) {
        this.onlineBGATCList.add(vatsimATC);
      }
    }
  }

  public void clearAndAddATCs(List<VatsimATC> onlineATCList) {
    this.clearATCs();

    for (VatsimATC vatsimATC : onlineATCList) {
      if (vatsimATC.getCallsign().startsWith(BULGARIAN_PREFIX)) {
        this.onlineBGATCList.add(vatsimATC);
      }
    }
    this.onlineATCList.addAll(onlineATCList);
  }

  public VatsimATC getATC(int index, boolean isOnlyBulgarianATCs) {
    if (isOnlyBulgarianATCs) {
      if (index >= 0 && index < onlineBGATCList.size()) {
        return onlineBGATCList.get(index);
      }
    } else {
      if (index >= 0 && index < onlineATCList.size()) {
        return onlineATCList.get(index);
      }
    }
    return null;
  }

  public VatsimATC getATC(int index) {
    return getATC(index, false);
  }

  public void removeATC(int index, boolean isOnlyBulgarianATCs) {

    if (isOnlyBulgarianATCs) {
      if (index >= 0 && index < onlineBGATCList.size()) {
        onlineBGATCList.remove(index);
      }
    } else {
      if (index >= 0 && index < onlineATCList.size()) {
        onlineATCList.remove(index);
      }
    }
  }

  public void removeATC(int index) {
    removeATC(index, false);
  }

  public List<VatsimATC> getAllOnlineATCsList(boolean isOnlyBulgarianATCs) {

    if (isOnlyBulgarianATCs) {
      return new ArrayList<>(onlineBGATCList);
    }
    return new ArrayList<>(onlineATCList);
  }

  public List<VatsimATC> getAllOnlineATCsList() {
    return getAllOnlineATCsList(false);
  }

  public void clearATCs() {
    onlineATCList.clear();
    onlineBGATCList.clear();
  }

  public int getOnlineATCListSize() {
    return getOnlineATCListSize(false);
  }

  public int getOnlineATCListSize(boolean isOnlyBulgarianATCs) {
    if (isOnlyBulgarianATCs) {
      return onlineBGATCList.size();
    }
    return onlineATCList.size();
  }

  // -----------------------------------------------------
  public Controllers getControllers() {

    if (controllers == null) {
      return new Controllers();
    }

    return controllers;
  }

  public boolean areControllersNull() {
    return controllers == null;
  }

  public void addController(VatEudUser controller, Integer rating) {

    if (controllers == null) {
      controllers = new Controllers();
    }

    switch (rating) {
//        case -1:
//          // INA
//          break;
//        case 0:
//          // SUS
//          break;
//        case 1:
//          // OBS
//          break;
      case 2:
        // S1
        controllers.getControllersS1().add(controller);
        break;
      case 3:
        // S2
        controllers.getControllersS2().add(controller);
        break;
      case 4:
        // S3
        controllers.getControllersS3().add(controller);
        break;
      case 5:
        // C1
        controllers.getControllersC1().add(controller);
        break;
      case 6:
        // C2
        controllers.getControllersC2().add(controller);
        break;
      case 7:
        // C3
        controllers.getControllersC3().add(controller);
        break;
      case 8:
        // I1
        controllers.getControllersI1().add(controller);
        break;
      case 9:
        // I2
        controllers.getControllersI2().add(controller);
        break;
      case 10:
        // I3
        controllers.getControllersI3().add(controller);
        break;
//        case 11:
//          // SUP
//          break;
//        case 12:
//          // ADM
//          break;
      default:
        // INA
        break;
    }
  }

  public void clearControllers() {

    if (controllers == null) {
      controllers = new Controllers();
      return;
    }
    controllers.clearAllControllers();
  }
}
