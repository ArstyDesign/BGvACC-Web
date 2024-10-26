package com.bgvacc.web.tasks;

import com.bgvacc.web.configurations.properties.MailProperties;
import com.bgvacc.web.domains.MailDomain;
import java.util.*;
import javax.annotation.*;
import javax.mail.*;
import javax.mail.internet.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.dao.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Component
public class MailSender {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MailProperties mailProperties;

  private JavaMailSender sender;

  @Async
  public void sendSimpleMail(SimpleMailMessage message) {
    try {
      sender.send(message);
    } catch (MailException e) {
      log.debug("Error sending mail!");
    }
  }

//    @Async
//    @Transactional(rollbackFor = Exception.class)
//    public void sendSignupMail(MailEntity mail, SignupMail signupMail) {
//        log.debug("Sending mail: " + mail.getMailId());
//
//        String sqlUpd = "UPDATE MAILBOX SET IS_SENT = ? WHERE MAIL_ID = ?";
//
//        try {
//            MimeMessage mimeMessage = sender.createMimeMessage();
//            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
//            message.setSubject(mail.getSubject());
//            message.setFrom(mail.getSender());
//            message.setTo(signupMail.getEmail());
//            message.setText(mail.getContent(), true); //true = isHtml
//
//            sender.send(mimeMessage);
//
//            jdbcTemplate.update(sqlUpd, true, mail.getMailId());
//            log.debug("Mail sent: " + mail.getMailId());
//        } catch (Exception e) {
//            log.error("Sending mail with id = " + mail.getMailId() + " failed!", e);
//        }
//    }
  @Async
  @Transactional(rollbackFor = Exception.class)
  public void sendMail(int mailId) {
    log.debug("Sending mail: " + mailId);

    final String sql = "SELECT * FROM mailbox WHERE is_sent = false AND mail_id = ? FOR UPDATE NOWAIT";
    final String sqlUpd = "UPDATE mailbox SET is_sent = true, sent_on = NOW() WHERE mail_id = ?";
    try {
      SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, mailId);

      if (rs.next()) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(rs.getString("subject"));
        message.setFrom(rs.getString("sender"));
        message.setTo(rs.getString("receivers"));
        String content = rs.getString("content");
        message.setText(content, true);
        sender.send(mimeMessage);

        jdbcTemplate.update(sqlUpd, mailId);
        log.debug("Mail sent: " + mailId);
      } else {
        log.debug("Mail: " + mailId + " not found for sending. Maybe already sent.");
      }
    } catch (Exception e) {
      log.error("Mail " + mailId + " not sent: " + e.getMessage());
    }
  }

  @Async
  @Transactional(rollbackFor = Exception.class)
  public void sendMail(MailDomain mail, String email) {
    log.debug("Sending mail: " + mail.getMailId());

    String sqlUpd = "UPDATE MAILBOX SET IS_SENT = ? WHERE MAIL_ID = ?";

    try {
      MimeMessage mimeMessage = sender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
      message.setSubject(mail.getSubject());
      message.setFrom(mail.getSender());
      message.setTo(email);
      message.setText(mail.getContent(), true); //true = isHtml

      sender.send(mimeMessage);

      jdbcTemplate.update(sqlUpd, true, mail.getMailId());
      log.debug("Mail sent: " + mail.getMailId());
    } catch (MessagingException | DataAccessException | MailException e) {
      log.error("Sending mail with id = " + mail.getMailId() + " failed!", e);
    }
  }

  @PostConstruct
  private void init() {
    JavaMailSenderImpl ms = new JavaMailSenderImpl();

//    log.debug(es.toString());
    ms.setHost(mailProperties.getHost());
    ms.setPort(mailProperties.getPort());
    ms.setProtocol(mailProperties.getProtocol());
    ms.setUsername(mailProperties.getUsername());
    ms.setPassword(mailProperties.getPassword());

    Properties properties = new Properties();
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.quitwait", "false");
    ms.setJavaMailProperties(properties);
    sender = ms;
  }
}
