package com.bgvacc.web.api.discord;

import com.bgvacc.web.api.Api;
import com.bgvacc.web.configurations.properties.DiscordProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.requests.discord.*;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.responses.events.UpcomingEventsResponse;
import com.bgvacc.web.responses.sessions.ClosedControllerSession;
import com.bgvacc.web.responses.sessions.NewlyOpenedControllerSession;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.time.LocalDateTime;
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
    List<DiscordEmbedsFieldRequest> fields = new ArrayList<>();
    fields.add(new DiscordEmbedsFieldRequest(":person_red_hair: Controller", nocs.getControllerNames().getFullName() + " - " + nocs.getCid()));
    fields.add(new DiscordEmbedsFieldRequest(":id: Position", nocs.getPositionCallsign() + " - " + nocs.getPositionName()));
    fields.add(new DiscordEmbedsFieldRequest(":radio: Frequency", nocs.getFrequency()));

    der.setFields(fields);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    DiscordEmbedsFooterRequest footer = new DiscordEmbedsFooterRequest("Logged on at: " + (nocs.getLoggedAt().toLocalDateTime().format(dtf)) + "z");

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
    List<DiscordEmbedsFieldRequest> fields = new ArrayList<>();
//    fields.add(new DiscordEmbedsFieldRequest(":person_red_hair: Controller", ccs.getControllerNames().getFullName() + " - " + nocs.getCid()));
    fields.add(new DiscordEmbedsFieldRequest(":id: Position", ccs.getPositionCallsign() + " - " + ccs.getPositionName()));
    fields.add(new DiscordEmbedsFieldRequest(":clock3: Active time", ccs.getActiveTime().toString()));
    fields.add(new DiscordEmbedsFieldRequest(":radio: Frequency", ccs.getFrequency()));

    der.setFields(fields);

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    DiscordEmbedsFooterRequest footer = new DiscordEmbedsFooterRequest("Logged off at: " + (ccs.getLoggedOffAt().toLocalDateTime().format(dtf)) + "z");

    der.setFooter(footer);

    embedRequests.add(der);

    dcsr.setEmbeds(embedRequests);

    doRequest(Methods.POST, url, dcsr, Void.class);
  }

  public void notifyForUpcomingEvents(UpcomingEventsResponse uer) throws JsonProcessingException {

    final String url = "https://discord.com/api/webhooks/" + discordProperties.getDiscordEventsAnnouncementChannelId() + "/" + discordProperties.getDiscordEventsWebhookId();

    DiscordEventNotificationRequest denr = new DiscordEventNotificationRequest();

    denr.setUsername("BGvACC Events");

    StringBuilder cb = new StringBuilder();
    cb.append("Hello, fellow aviators! ");

    List<DiscordEmbedsRequest> embedRequests = new ArrayList<>();

    for (EventResponse ue : uer.getUpcomingEvents()) {
      DiscordEmbedsRequest der = new DiscordEmbedsRequest();

      der.setTitle(ue.getName());
      der.setDescription(ue.getShortDescription());
      der.setColor(44209L);
      der.setTimestamp(ue.getStartAtTimestamp().toString());
      log.debug("VATEUD Event URL: " + ue.getVateudEventUrl());
      der.setUrl(ue.getVateudEventUrl());

      DiscordEmbedsAuthorRequest author = new DiscordEmbedsAuthorRequest();
      author.setName("BGvACC Event director");
      der.setAuthor(author);

      DiscordEmbedsImageRequest image = new DiscordEmbedsImageRequest();
      image.setUrl(ue.getImageUrl());
      der.setImage(image);

      DiscordEmbedsFooterRequest footer = new DiscordEmbedsFooterRequest();
      footer.setText("Starting time ->");
      der.setFooter(footer);

      if (ue.isEvent()) {

        LocalDateTime startAtDateTime = ue.getStartAtTimestamp().toLocalDateTime();
        LocalDateTime endAtDateTime = ue.getEndAtTimestamp().toLocalDateTime();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter hourTimeFormatter = DateTimeFormatter.ofPattern("HH");

        cb.append("**");
        cb.append(ue.getName());
        cb.append("** takes place on **");
        cb.append(startAtDateTime.format(weekFormatter));
        cb.append("** (**");
        cb.append(startAtDateTime.format(dateFormatter));
        cb.append("**) between **");
        cb.append(startAtDateTime.format(hourTimeFormatter));
        cb.append("** and **");
        cb.append(endAtDateTime.format(hourTimeFormatter));
        cb.append(" Zulu** at **LBSR FIR**. You can expect full ATC coverate at LBSF. Connect with VATSIM and immerse yourself in the event on **");
        cb.append(startAtDateTime.format(dateFormatter));
        cb.append("**. Fly High, Soar Higher! :flag_bg: :airplane:");
      }

      if (ue.isCpt()) {

        if (uer.getEventsCount() > 0) {
          cb.append("\nAlso, there will be ");

          if (uer.getCptCount() == 1) {
            cb.append("**one CPT**");
          } else if (uer.getCptCount() > 1) {
            cb.append("**");
            cb.append(uer.getCptCount());
            cb.append(" CPTs**");
          }

          cb.append(" :speaking_head:. You are most welcome to come and help **");
          cb.append((ue.getCptExaminee() != null) ? ue.getCptExaminee() : "");
          cb.append("** ");

          if (uer.getCptCount() > 1) {
            cb.append("gain their ");
          } else {
            cb.append("gain his/her **");
          }
          cb.append(ue.getCptRatingSymbol());
          cb.append(" rating**! ");
          cb.append("Whether you're navigating IFR or VFR, we welcome all pilots to be part of this special event. You can expect **full ATC coverage at LBSR_CTR**. Connect with VATSIM and immerse yourself in the event on **");

          LocalDateTime startAtDateTime = ue.getStartAtTimestamp().toLocalDateTime();

          DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

          cb.append(startAtDateTime.format(dateFormatter));
          cb.append("** :star_struck:.");
        }
      }

      embedRequests.add(der);
    }

    denr.setContent(String.valueOf(cb));
    denr.setEmbeds(embedRequests);

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(denr);

    log.debug("JSON: " + json);
//    doRequest(Methods.POST, url, denr, Void.class);
  }
}
