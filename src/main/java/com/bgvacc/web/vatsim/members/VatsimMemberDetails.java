package com.bgvacc.web.vatsim.members;

import com.bgvacc.web.vatsim.utils.VatsimRatingUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
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
public class VatsimMemberDetails implements Serializable {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("rating")
  private Integer rating;

  @JsonProperty("pilotrating")
  private Integer pilotRating;

  @JsonProperty("militaryrating")
  private Integer militaryRating;

  @JsonProperty("susp_date")
  private Timestamp suspensionDate;
  
  @JsonProperty("reg_date")
  private Timestamp registrationDate;

  @JsonProperty("region_id")
  private String regionId;

  @JsonProperty("division_id")
  private String divisionId;

  @JsonProperty("subdivision_id")
  private String subdivisionId;

  @JsonProperty("lastratingchange")
  private Timestamp lastRatingChange;

  public String getAtcRatingSymbol() {
    return VatsimRatingUtils.getATCRatingSymbol(this.rating);
  }

  public String getAtcRatingName() {
    return VatsimRatingUtils.getATCRatingName(this.rating);
  }

  public String getPilotRatingSymbol() {
    return VatsimRatingUtils.getPilotRatingSymbol(this.pilotRating);
  }

  public String getPilotRatingName() {
    return VatsimRatingUtils.getPilotRatingName(this.pilotRating);
  }

  public String getMilitaryRatingSymbol() {
    return VatsimRatingUtils.getMilitaryRatingSymbol(this.militaryRating);
  }

  public String getMilitaryRatingName() {
    return VatsimRatingUtils.getMilitaryRatingName(this.militaryRating);
  }
}
