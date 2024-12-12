package com.bgvacc.web.responses.authentication;

import com.bgvacc.web.enums.AuthenticationError;
import com.bgvacc.web.responses.users.UserResponse;
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
public class AuthAttemptResponse implements Serializable {

  private UserResponse user;
  private AuthenticationError error;

  public boolean hasErrors() {
    return error != null;
  }
}