package com.bgvacc.web.requests.discord;

import java.io.Serializable;
import java.util.List;
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
public class DiscordEmbedsRequest implements Serializable {

  private String title;
  private String description;
  private Long color;
  private List<DiscordEmbedsField> fields;
  private DiscordEmbedsFooterRequest footer;
}
