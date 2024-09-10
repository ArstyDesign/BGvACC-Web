package com.bgvacc.web.vatsim.members;

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
public class VatsimMemberSoloValidation implements Serializable {
  
  @JsonProperty("id")
  private Integer id;
  
  @JsonProperty("user_cid")
  private Integer userCid;
  
  @JsonProperty("instructor_cid")
  private Integer instructorCid;
  
  @JsonProperty("position")
  private String position;
  
  @JsonProperty("expiry")
  private Timestamp expiry;
  
  @JsonProperty("max_days")
  private Integer maxDays;
  
  @JsonProperty("facility")
  private Integer facility;
  
  @JsonProperty("created_at")
  private Timestamp createdAt;
  
  @JsonProperty("updated_at")
  private Timestamp updatedAt;

}
