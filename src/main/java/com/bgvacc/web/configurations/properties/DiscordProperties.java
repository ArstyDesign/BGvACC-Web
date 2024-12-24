package com.bgvacc.web.configurations.properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class DiscordProperties {

  public String getDiscordClientId() {
    return getProperty("config/discord/client-id");
  }

  public String getDiscordBotToken() {
    return getProperty("config/discord/bot/token");
  }

  public String getDiscordControllersAnnouncementChannelId() {
    return getProperty("config/discord/controllers-online/announcements-channel-id");
  }

  public String getDiscordControllersOnlineWebhookId() {
    return getProperty("config/discord/controllers-online/webhook-id");
  }

  public String getDiscordEventsAnnouncementChannelId() {
    return getProperty("config/discord/events/announcements-channel-id");
  }

  public String getDiscordEventsWebhookId() {
    return getProperty("config/discord/events/webhook-id");
  }
  
  public String getDiscordWeeklyATCReportChannelId() {
    return getProperty("config/discord/weekly-atc-report/report-channel-id");
  }

  public String getDiscordWeeklyATCReportWebhookId() {
    return getProperty("config/discord/weekly-atc-report/webhook-id");
  }

  private String getProperty(String propertyName) {
    try {
      Context ctx = new InitialContext();
      return (String) ctx.lookup("java:comp/env/" + propertyName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to fetch JNDI resource", e);
    }
  }
}
