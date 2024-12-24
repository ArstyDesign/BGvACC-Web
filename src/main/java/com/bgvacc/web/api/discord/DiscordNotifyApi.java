package com.bgvacc.web.api.discord;

import com.bgvacc.web.api.Api;
import com.bgvacc.web.configurations.properties.DiscordProperties;
import com.bgvacc.web.enums.Methods;
import com.bgvacc.web.requests.discord.*;
import com.bgvacc.web.requests.discord.weeklyreport.DiscordWeeklyATCReportRequest;
import com.bgvacc.web.responses.atc.report.*;
import com.bgvacc.web.responses.events.EventResponse;
import com.bgvacc.web.responses.events.UpcomingEventsResponse;
import com.bgvacc.web.responses.sessions.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

  final int START_END_SPACES_COUNT = 2;

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

  public void generateWeeklyATCReport(ControllersOnlineReportResponse report) throws JsonProcessingException {

    final String url = "https://discord.com/api/webhooks/" + discordProperties.getDiscordWeeklyATCReportChannelId() + "/" + discordProperties.getDiscordWeeklyATCReportWebhookId();

    DiscordWeeklyATCReportRequest dwarr = new DiscordWeeklyATCReportRequest();

    dwarr.setUsername("BGvACC");

    StringBuilder cb = new StringBuilder();
    cb.append("@everyone Hello, fellow aviators! I present you the ATC report from last week.\n\n");

    if (report.getTowerPositions() == null || report.getTowerPositions().isEmpty()) {
      cb.append("**NOBODY CONTROLLED TWR THIS WEEK** :cry:\n\n");
    } else {
      cb.append("**:date:  WEEKLY CONTROLLER REPORT FOR TWR :saluting_face:**");
      cb = generatePositionReport(report.getTowerPositions(), cb);
      cb.append("Total controlled time on Tower: **").append(report.getTotalTimeControlledTower().printTimeLeadingZeros(true, true, false)).append("**");
      cb.append("\n\n");
    }

    if (report.getApproachPositions() == null || report.getApproachPositions().isEmpty()) {
      cb.append("**NOBODY CONTROLLED APP THIS WEEK** :cry:\n\n");
    } else {
      cb.append("**:date:  WEEKLY CONTROLLER REPORT FOR APP :saluting_face:**");
      cb = generatePositionReport(report.getApproachPositions(), cb);
      cb.append("Total controlled time on Approach: **").append(report.getTotalTimeControlledApproach().printTimeLeadingZeros(true, true, false)).append("**");
      cb.append("\n\n");
    }

    if (report.getControlPositions() == null || report.getControlPositions().isEmpty()) {
      cb.append("**NOBODY CONTROLLED CTR THIS WEEK** :cry:\n\n");
    } else {
      cb.append("**:date:  WEEKLY CONTROLLER REPORT FOR CTR :saluting_face:**");
      cb = generatePositionReport(report.getControlPositions(), cb);
      cb.append("Total controlled time on Control: **").append(report.getTotalTimeControlledControl().printTimeLeadingZeros(true, true, false)).append("**");
      cb.append("\n\n");
    }

    cb.append("CONGRATULATIONS AND GOOD JOB EVERYBODY! :clap:\n\n");
    cb.append("TOGETHER YOU MANAGED TO CONTROL TOTAL TIME - **").append(report.getTotalTimeControlled().printTimeLeadingZeros(true, true, false)).append("** :partying_face:\n\n");
    cb.append("See you next week,\n");
    cb.append("BGvACC Bot");

    dwarr.setContent(String.valueOf(cb));

    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    String json = ow.writeValueAsString(dwarr);

    log.debug("JSON: " + json);
    doRequest(Methods.POST, url, dwarr, Void.class);
  }

  private StringBuilder generatePositionReport(List<ControllersOnlineReportItemResponse> items, StringBuilder b) {

    List<String> names = new ArrayList<>();
    List<String> callsigns = new ArrayList<>();
    List<String> times = new ArrayList<>();

    items.forEach(item -> {
      names.add(item.getControllerName() != null ? item.getControllerName().getFullName() : "-");
      callsigns.add(item.getPosition());
      times.add(item.getTotalTimeControlled().printTimeLeadingZeros(true, true, false));
    });

    b.append("```| ");

    int totalControllers = names.size();

    List<String> numbers = new ArrayList<>();
    for (int i = 1; i <= totalControllers; i++) {
      numbers.add(i + ".");
    }

    final String NO_COLUMN_HEADER = "No.";
    b.append(NO_COLUMN_HEADER);
    ColumnHeaderResponse nuch = generateColumnHeader(NO_COLUMN_HEADER, b, numbers);
    b = nuch.getStringBuilder();

    final String NAME_COLUMN_HEADER = "Name";
    b.append(" | ").append(NAME_COLUMN_HEADER);
    ColumnHeaderResponse nch = generateColumnHeader(NAME_COLUMN_HEADER, b, names);
    b = nch.getStringBuilder();

    final String CALLSIGN_COLUMN_HEADER = "Callsign";
    b.append(" | ").append(CALLSIGN_COLUMN_HEADER);
    ColumnHeaderResponse cch = generateColumnHeader(CALLSIGN_COLUMN_HEADER, b, callsigns);
    b = cch.getStringBuilder();

    final String TIME_COLUMN_HEADER = "Time";
    b.append(" | ").append(TIME_COLUMN_HEADER);
    ColumnHeaderResponse tch = generateColumnHeader(TIME_COLUMN_HEADER, b, times);
    b = tch.getStringBuilder();

    b.append(" |\n");

    // Row 2
    b.append("| ");

    for (int i = 1; i <= nuch.getColumnMostChars() - START_END_SPACES_COUNT; i++) {
      b.append("-");
    }

    b.append(" | ");

    for (int i = 1; i <= nch.getColumnMostChars() - START_END_SPACES_COUNT; i++) {
      b.append("-");
    }

    b.append(" | ");

    for (int i = 1; i <= cch.getColumnMostChars() - START_END_SPACES_COUNT; i++) {
      b.append("-");
    }

    b.append(" | ");

    for (int i = 1; i <= tch.getColumnMostChars() - START_END_SPACES_COUNT; i++) {
      b.append("-");
    }

    b.append(" |").append("\n");

    // Row 3 and onwards
    for (int i = 0; i < items.size(); i++) {
      b.append("| ");
      b.append(numbers.get(i));
      for (int j = 1; j <= nuch.getColumnMostChars() - numbers.get(i).length() - START_END_SPACES_COUNT; j++) {
        b.append(" ");
      }
      b.append(" | ");
      String name = items.get(i).getControllerName() != null ? items.get(i).getControllerName().getFullName() : "-";
      b.append(name);
      for (int j = 1; j <= nch.getColumnMostChars() - name.length() - START_END_SPACES_COUNT; j++) {
        b.append(" ");
      }
      b.append(" | ");
      b.append(items.get(i).getPosition());
      for (int j = 1; j <= cch.getColumnMostChars() - items.get(i).getPosition().length() - START_END_SPACES_COUNT; j++) {
        b.append(" ");
      }
      b.append(" | ");
      b.append(items.get(i).getTotalTimeControlled().printTimeLeadingZeros(true, true, false));
      for (int j = 1; j <= tch.getColumnMostChars() - items.get(i).getTotalTimeControlled().printTimeLeadingZeros(true, true, false).length() - START_END_SPACES_COUNT; j++) {
        b.append(" ");
      }
      b.append(" |\n");
    }

    b.append("```");

    return b;
  }

  private ColumnHeaderResponse generateColumnHeader(String columnName, StringBuilder b, List<String> items) {

    int columnMinimumChars = columnName.length() + START_END_SPACES_COUNT; // For space before and after column name
    int columnMostChars = columnMinimumChars;

    for (String item : items) {
      if (item.length() + START_END_SPACES_COUNT > columnMostChars) {
        columnMostChars = item.length() + START_END_SPACES_COUNT;
      }
    }

    if (columnMostChars > columnMinimumChars - START_END_SPACES_COUNT) {
      for (int i = 1; i <= columnMostChars - columnName.length() - START_END_SPACES_COUNT; i++) {
        b.append(" ");
      }
    }

    ColumnHeaderResponse chr = new ColumnHeaderResponse();
    chr.setColumnMostChars(columnMostChars);
    chr.setStringBuilder(b);

    return chr;
  }
}
