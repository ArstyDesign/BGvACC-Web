package com.bgvacc.web.vatsim.memory;

import com.bgvacc.web.vatsim.atc.VatsimATC;
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
  
  public boolean isAdded = false;

  private Memory() {
    this.onlineATCList = new ArrayList<>();
  }

  public static synchronized Memory getInstance() {
    if (instance == null) {
      instance = new Memory();
    }
    return instance;
  }

  public void addATC(VatsimATC atc) {
    onlineATCList.add(atc);
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
  }

  public void clearAndAddATCs(List<VatsimATC> onlineATCList) {
    this.clearATCs();
    this.onlineATCList.addAll(onlineATCList);
  }

  public VatsimATC getATC(int index) {
    if (index >= 0 && index < onlineATCList.size()) {
      return onlineATCList.get(index);
    }
    return null;
  }

  public void removeATC(int index) {
    if (index >= 0 && index < onlineATCList.size()) {
      onlineATCList.remove(index);
    }
  }

  public List<VatsimATC> getAllOnlineATCsList() {
    return new ArrayList<>(onlineATCList);
  }

  public void clearATCs() {
    onlineATCList.clear();
  }

  public int getSize() {
    return onlineATCList.size();
  }
}
