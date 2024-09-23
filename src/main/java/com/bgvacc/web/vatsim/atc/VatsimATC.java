package com.bgvacc.web.vatsim.atc;

import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class VatsimATC implements Serializable {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("callsign")
  private String callsign;

//  @JsonProperty("start")
//  private String start;
  @JsonProperty("server")
  private String server;

  @JsonProperty("rating")
  private Integer rating;

  @JsonIgnore
  private String firstName;

  @JsonIgnore
  private String lastName;

  @JsonIgnore
  private String firstNameBg;

  @JsonIgnore
  private String lastNameBg;

  public VatsimATC() {
  }

  public VatsimATC(Long id, String callsign, String server, Integer rating, String firstName, String lastName) {
    this.id = id;
    this.callsign = callsign;
    this.server = server;
    this.rating = rating;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public VatsimATC(Long id, String callsign, String server, Integer rating, String firstName, String lastName, String firstNameBg, String lastNameBg) {
    this.id = id;
    this.callsign = callsign;
    this.server = server;
    this.rating = rating;
    this.firstName = firstName;
    this.lastName = lastName;
    this.firstNameBg = firstNameBg;
    this.lastNameBg = lastNameBg;
  }

  public String getAtcRatingSymbol() {
    return VatsimRatingUtils.getATCRatingSymbol(this.rating);
  }

  public String getAtcRatingName() {
    return VatsimRatingUtils.getATCRatingName(this.rating);
  }

  public String getFullName() {
    return (lastName != null) ? firstName + ' ' + lastName : firstName;
  }

  public String getFullNameBg() {
    return (lastNameBg != null) ? firstNameBg + ' ' + lastNameBg : firstNameBg;
  }
}
