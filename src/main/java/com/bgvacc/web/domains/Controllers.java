package com.bgvacc.web.domains;

import com.bgvacc.web.vatsim.vateud.VatEudUser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Controllers implements Serializable {

  private List<VatEudUser> controllersS1;
  private List<VatEudUser> controllersS2;
  private List<VatEudUser> controllersS3;
  private List<VatEudUser> controllersC1;
  private List<VatEudUser> controllersC2;
  private List<VatEudUser> controllersC3;
  private List<VatEudUser> controllersI1;
  private List<VatEudUser> controllersI2;
  private List<VatEudUser> controllersI3;

  public Long getTotalControllers() {

    Long result = 0L;

    result += Long.valueOf(getControllersS1().size());
    result += Long.valueOf(getControllersS2().size());
    result += Long.valueOf(getControllersS3().size());
    result += Long.valueOf(getControllersC1().size());
    result += Long.valueOf(getControllersC2().size());
    result += Long.valueOf(getControllersC3().size());
    result += Long.valueOf(getControllersI1().size());
    result += Long.valueOf(getControllersI2().size());
    result += Long.valueOf(getControllersI3().size());

    return result;
  }

  public void clearAllControllers() {
    getControllersS1().clear();
    getControllersS2().clear();
    getControllersS3().clear();
    getControllersC1().clear();
    getControllersC2().clear();
    getControllersC3().clear();
    getControllersI1().clear();
    getControllersI2().clear();
    getControllersI3().clear();
  }

  public List<VatEudUser> getControllersS1() {

    if (controllersS1 == null) {
      controllersS1 = new ArrayList<>();
    }
    return controllersS1;
  }

  public List<VatEudUser> getControllersS2() {

    if (controllersS2 == null) {
      controllersS2 = new ArrayList<>();
    }
    return controllersS2;
  }

  public List<VatEudUser> getControllersS3() {

    if (controllersS3 == null) {
      controllersS3 = new ArrayList<>();
    }
    return controllersS3;
  }

  public List<VatEudUser> getControllersC1() {

    if (controllersC1 == null) {
      controllersC1 = new ArrayList<>();
    }
    return controllersC1;
  }

  public List<VatEudUser> getControllersC2() {

    if (controllersC2 == null) {
      controllersC2 = new ArrayList<>();
    }
    return controllersC2;
  }

  public List<VatEudUser> getControllersC3() {

    if (controllersC3 == null) {
      controllersC3 = new ArrayList<>();
    }
    return controllersC3;
  }

  public List<VatEudUser> getControllersI1() {

    if (controllersI1 == null) {
      controllersI1 = new ArrayList<>();
    }
    return controllersI1;
  }

  public List<VatEudUser> getControllersI2() {

    if (controllersI2 == null) {
      controllersI2 = new ArrayList<>();
    }
    return controllersI2;
  }

  public List<VatEudUser> getControllersI3() {

    if (controllersI3 == null) {
      controllersI3 = new ArrayList<>();
    }
    return controllersI3;
  }
}
