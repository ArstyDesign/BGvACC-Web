package com.bgvacc.web.vatsim.members;

import com.aarshinkov.datetimecalculator.domain.Time;
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
public class VatsimMemberStatistics implements Serializable {

  @JsonProperty("id")
  private Long id;

  @JsonProperty("atc")
  private Double atc;

  @JsonProperty("pilot")
  private Double pilot;

  @JsonProperty("s1")
  private Double s1;

  @JsonProperty("s2")
  private Double s2;

  @JsonProperty("s3")
  private Double s3;

  @JsonProperty("c1")
  private Double c1;

  @JsonProperty("c2")
  private Double c2;

  @JsonProperty("c3")
  private Double c3;

  @JsonProperty("i1")
  private Double i1;

  @JsonProperty("i2")
  private Double i2;

  @JsonProperty("i3")
  private Double i3;

  @JsonProperty("sup")
  private Double sup;

  @JsonProperty("adm")
  private Double adm;

  public Time getAtcTime() {
    Double atcTime = this.atc * 3600;
    return new Time(atcTime.longValue());
  }

  public Time getPilotTime() {
    Double pilotTime = this.pilot * 3600;
    return new Time(pilotTime.longValue());
  }

  public Time getS1Time() {
    Double s1Time = this.s1 * 3600;
    return new Time(s1Time.longValue());
  }

  public Time getS2Time() {
    Double s2Time = this.s2 * 3600;
    return new Time(s2Time.longValue());
  }

  public Time getS3Time() {
    Double s3Time = this.s3 * 3600;
    return new Time(s3Time.longValue());
  }

  public Time getC1Time() {
    Double c1Time = this.c1 * 3600;
    return new Time(c1Time.longValue());
  }

  public Time getC2Time() {
    Double c2Time = this.c2 * 3600;
    return new Time(c2Time.longValue());
  }

  public Time getC3Time() {
    Double c3Time = this.c3 * 3600;
    return new Time(c3Time.longValue());
  }

  public Time getI1Time() {
    Double i1Time = this.i1 * 3600;
    return new Time(i1Time.longValue());
  }

  public Time getI2Time() {
    Double i2Time = this.i2 * 3600;
    return new Time(i2Time.longValue());
  }

  public Time getI3Time() {
    Double i3Time = this.i3 * 3600;
    return new Time(i3Time.longValue());
  }

  public Time getSupTime() {
    Double supTime = this.sup * 3600;
    return new Time(supTime.longValue());
  }

  public Time getAdmTime() {
    Double admTime = this.adm * 3600;
    return new Time(admTime.longValue());
  }
  
}
