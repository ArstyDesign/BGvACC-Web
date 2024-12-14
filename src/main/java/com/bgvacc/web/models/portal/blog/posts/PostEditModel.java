package com.bgvacc.web.models.portal.blog.posts;

import jakarta.validation.constraints.NotBlank;
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
public class PostEditModel implements Serializable {

//  @NotBlank
  private String id;

  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private boolean isVisible;

}
