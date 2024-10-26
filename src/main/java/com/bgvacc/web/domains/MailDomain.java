package com.bgvacc.web.domains;

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
public class MailDomain implements Serializable {

  private String mailId;
  private String sender;
  private String receivers;
  private String subject;
  private String content;
}
