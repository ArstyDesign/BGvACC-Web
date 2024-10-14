package com.bgvacc.web.exceptions;

import lombok.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralErrorException extends RuntimeException {

  private Integer errorCode;
  private String message;
  private String description;
  private String dateTime;
  private String httpStatus;

}
