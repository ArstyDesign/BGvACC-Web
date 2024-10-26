package com.bgvacc.web.schedulers;

import com.bgvacc.web.tasks.MailSender;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class MailScheduler {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MailSender mailSender;

  private final JdbcTemplate jdbcTemplate;

  @Scheduled(cron = "20 * * * * *", zone = "UTC")
  public void mailScheduler() {
    log.debug("Checking for mails to send...");

    final String sql = "SELECT mail_id FROM mailbox WHERE is_sent = false";

    try {
      SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);

      int mailsToSend = 0;

      while (rs.next()) {
        mailsToSend++;
        mailSender.sendMail(rs.getInt("mail_id"));
      }

      switch (mailsToSend) {
        case 0:
          log.debug("No mails to send have been found!");
          break;
        case 1:
          log.debug("1 mail to send has been found!");
          break;
        default:
          log.debug(mailsToSend + " mails to send have been found!");
          break;
      }

    } catch (DataAccessException e) {
      log.error("Error sending mails!", e);
    }

    log.debug("End checking for mails to send.");
  }
}
