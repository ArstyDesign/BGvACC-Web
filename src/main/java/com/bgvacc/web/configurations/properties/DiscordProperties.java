package com.bgvacc.web.configurations.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
@PropertySource("classpath:discord.properties")
public class DiscordProperties {

  @Value("${discord.clientid}")
  private String discordClientId;

  @Value("${discord.bot.token}")
  private String discordBotToken;

  @Value("${discord.controllers.online.announcement.channel.id}")
  private String discordControllersAnnouncementChannelId;

  @Value("${discord.controllers.online.webhook.id}")
  private String discordControllersOnlineWebhookId;

  @Value("${discord.events.announcement.channel.id}")
  private String discordEventsAnnouncementChannelId;

  @Value("${discord.events.webhook.id}")
  private String discordEventsWebhookId;

  public String getDiscordClientId() {
    return discordClientId;
  }

  public String getDiscordBotToken() {
    return discordBotToken;
  }

  public String getDiscordControllersAnnouncementChannelId() {
    return discordControllersAnnouncementChannelId;
  }

  public String getDiscordControllersOnlineWebhookId() {
    return discordControllersOnlineWebhookId;
  }

  public String getDiscordEventsAnnouncementChannelId() {
    return discordEventsAnnouncementChannelId;
  }

  public String getDiscordEventsWebhookId() {
    return discordEventsWebhookId;
  }
}
