package com.bgvacc.web.responses.blog;

import com.bgvacc.web.utils.Names;
import java.io.Serializable;
import java.sql.Timestamp;
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
public class PostResponse implements Serializable {

  private String id;
  private String title;
  private String content;
  private String mainImage;
  private boolean isVisible;
  private String cid;
  private Names names;
  private Timestamp createdAt;
  private Timestamp updatedAt;

}
