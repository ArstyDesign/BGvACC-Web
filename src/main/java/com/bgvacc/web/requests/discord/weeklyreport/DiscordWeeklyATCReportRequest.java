package com.bgvacc.web.requests.discord.weeklyreport;

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
public class DiscordWeeklyATCReportRequest implements Serializable {

  private String username;
  private String content;
}
