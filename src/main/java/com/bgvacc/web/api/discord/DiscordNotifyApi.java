package com.bgvacc.web.api.discord;

import com.bgvacc.web.api.Api;
import com.bgvacc.web.configurations.DiscordProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.requests.discord.*;
import com.bgvacc.web.responses.sessions.ClosedControllerSession;
import com.bgvacc.web.responses.sessions.NewlyOpenedControllerSession;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class DiscordNotifyApi extends Api {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private DiscordProperties discordProperties;

  public void notifyDiscordForNewOnlineController(NewlyOpenedControllerSession nocs) {

    final String url = "https://discord.com/api/webhooks/" + discordProperties.getDiscordControllersAnnouncementChannelId() + "/" + discordProperties.getDiscordControllersOnlineWebhookId();

    DiscordControllerSessionRequest dcsr = new DiscordControllerSessionRequest();

    List<DiscordEmbedsRequest> embedRequests = new ArrayList<>();

    DiscordEmbedsRequest der = new DiscordEmbedsRequest();
    der.setTitle("ATC Announcement");
    der.setDescription(":white_check_mark: New ATC logged in just now!");
    der.setColor(5763719L);
    List<DiscordEmbedsField> fields = new ArrayList<>();
    fields.add(new DiscordEmbedsField(":person_red_hair: Controller", nocs.getControllerNames().getFullName() + " - " + nocs.getCid()));
    fields.add(new DiscordEmbedsField(":id: Position", nocs.getPositionCallsign() + " - " + nocs.getPositionName()));
    fields.add(new DiscordEmbedsField(":radio: Frequency", nocs.getFrequency()));

    der.setFields(fields);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    DiscordEmbedsFooterRequest footer = new DiscordEmbedsFooterRequest("Logged on at: " + (nocs.getLoggedAt().toLocalDateTime().format(dtf)));

    der.setFooter(footer);

    embedRequests.add(der);

    dcsr.setEmbeds(embedRequests);

    doRequest(Methods.POST, url, dcsr, Void.class);
  }

  public void notifyDiscordForClosedOnlineController(ClosedControllerSession ccs) {

    final String url = "https://discord.com/api/webhooks/" + discordProperties.getDiscordControllersAnnouncementChannelId() + "/" + discordProperties.getDiscordControllersOnlineWebhookId();

    DiscordControllerSessionRequest dcsr = new DiscordControllerSessionRequest();

    List<DiscordEmbedsRequest> embedRequests = new ArrayList<>();

    DiscordEmbedsRequest der = new DiscordEmbedsRequest();
    der.setTitle("ATC Announcement");
    der.setDescription(":x: ATC logged off! :x:");
    der.setColor(15548997L);
    List<DiscordEmbedsField> fields = new ArrayList<>();
    fields.add(new DiscordEmbedsField(":id: Position", ccs.getPositionCallsign() + " - " + ccs.getPositionName()));
    fields.add(new DiscordEmbedsField(":radio: Frequency", ccs.getFrequency()));

    der.setFields(fields);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    DiscordEmbedsFooterRequest footer = new DiscordEmbedsFooterRequest("Logged off at: " + (ccs.getLoggedOffAt().toLocalDateTime().format(dtf)));

    der.setFooter(footer);

    embedRequests.add(der);

    dcsr.setEmbeds(embedRequests);

    doRequest(Methods.POST, url, dcsr, Void.class);
  }
}
