package com.bgvacc.web.vatsim.atc;

import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
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

  public String getAtcRatingSymbol() {
    return VatsimRatingUtils.getATCRatingSymbol(this.rating);
  }

  public String getAtcRatingName() {
    return VatsimRatingUtils.getATCRatingName(this.rating);
  }

  public String getFullName() {
    return (lastName != null) ? firstName + ' ' + lastName : firstName;
  }
}
