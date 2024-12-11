package com.bgvacc.web.models.portal.profile.settings;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class ChangePasswordModel implements Serializable {

  @NotBlank
  private String currentPassword;

  @NotBlank
  @Size(min = 6, max = 30)
  private String newPassword;

  @NotBlank
  @Size(min = 6, max = 30)
  private String confirmPassword;

}
