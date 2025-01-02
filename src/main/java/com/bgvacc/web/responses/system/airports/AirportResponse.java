package com.bgvacc.web.responses.system.airports;

import java.io.Serializable;
import lombok.*;

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
public class AirportResponse implements Serializable {

  private String airportId;
  private String name;
  private String location;
  private String imagePath;

}
