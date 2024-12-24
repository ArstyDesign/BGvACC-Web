package com.bgvacc.web.services;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.domains.MailDomain;
import com.bgvacc.web.requests.atc.ATCApplicationRequest;
import com.bgvacc.web.tasks.MailSender;
import com.bgvacc.web.utils.Names;
import com.bgvacc.web.utils.Translator;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MailServiceImpl extends Base implements MailService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  private final TemplateEngine templateEngine;

  private final MailSender mailSender;

  private final Translator translator;

  @Override
  public boolean sendNewATCTrainingApplicationMail(ATCApplicationRequest atcApplication) {

    try {
      Context ctx = new Context();

      atcApplication.setCurrentRating(translator.toLanguage("atc.application.form.currentrating." + atcApplication.getCurrentRating().toLowerCase(), "en"));

      ctx.setVariable("atcApplication", atcApplication);

      String htmlContent = templateEngine.process("atc-application-mail.html", ctx);

//      MailEntity mail = createMail(mailSettings.getSender(), "Policy expiring notification", htmlContent, "a.arshinkov97@gmail.com");
      MailDomain mail = createMail("mycardocsapp@gmail.com", "New ATC Application", htmlContent, "a.arshinkov97@gmail.com");

      mailSender.sendMail(mail, "a.arshinkov97@gmail.com");

      return true;

    } catch (NoSuchMessageException e) {
      log.error("Sending failed!", e);
      return false;
    }
  }

  @Override
  public boolean sendForgottenPasswordMail(Names names, String email, String passwordResetToken) {

    try {
      Context ctx = new Context();

      ctx.setVariable("appUrl", "http://31.13.207.134");
      ctx.setVariable("names", names);
      ctx.setVariable("email", email);
      ctx.setVariable("passwordResetToken", passwordResetToken);

      String htmlContent = templateEngine.process("forgotten-password-mail.html", ctx);

      MailDomain mail = createMail("mycardocsapp@gmail.com", "Forgotten password", htmlContent, email);
//      MailDomain mail = createMail("mycardocsapp@gmail.com", "Forgotten password", htmlContent, "a.arshinkov97@gmail.com");

      mailSender.sendMail(mail, email);

      return true;

    } catch (NoSuchMessageException e) {
      log.error("Sending failed!", e);
      return false;
    }
  }

  @Override
  public boolean sendUserCreatedMail(Names names, String cid, String email, String temporaryPassword) {

    try {
      Context ctx = new Context();

      ctx.setVariable("appUrl", "http://31.13.207.134");
      ctx.setVariable("cid", cid);
      ctx.setVariable("email", email);
      ctx.setVariable("names", names);
      ctx.setVariable("temporaryPassword", temporaryPassword);

      String htmlContent = templateEngine.process("user-created-mail.html", ctx);

      MailDomain mail = createMail("mycardocsapp@gmail.com", "User account created", htmlContent, email);
//      MailDomain mail = createMail("mycardocsapp@gmail.com", "User account created", htmlContent, "a.arshinkov97@gmail.com");

      mailSender.sendMail(mail, email);

      return true;

    } catch (NoSuchMessageException e) {
      log.error("Sending failed!", e);
      return false;
    }
  }

  @Override
  public boolean sendEventControllerApplicationApprovedMail(Names names, String cid, String email, Long eventId, String eventName, String position, String date, String timeSlot) {

    try {
      Context ctx = new Context();

      ctx.setVariable("appUrl", "http://31.13.207.134");
      ctx.setVariable("names", names);
      ctx.setVariable("cid", cid);
      ctx.setVariable("email", email);
      ctx.setVariable("eventId", eventId);
      ctx.setVariable("eventName", eventName);
      ctx.setVariable("position", position);
      ctx.setVariable("date", date);
      ctx.setVariable("timeSlot", timeSlot);

      String htmlContent = templateEngine.process("event-application-approved-mail.html", ctx);

      MailDomain mail = createMail("mycardocsapp@gmail.com", "Event application approved", htmlContent, email);
//      MailDomain mail = createMail("mycardocsapp@gmail.com", "Event application approved", htmlContent, "a.arshinkov97@gmail.com");

      mailSender.sendMail(mail, email);

      return true;

    } catch (NoSuchMessageException e) {
      log.error("Sending failed!", e);
      return false;
    }
  }

  @Override
  public MailDomain createMail(String sender, String subject, String content, String... recipients) {

    StringBuilder to = new StringBuilder();

    for (String recipient : recipients) {
      to.append(recipient);
      to.append(";");
    }

    final String createMailSql = "INSERT INTO mailbox (mail_id, sender, receivers, subject, content) VALUES (?, ?, ?, ?, ?)";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement createMailPstmt = conn.prepareStatement(createMailSql)) {

      try {

        conn.setAutoCommit(false);

        final String MAIL_ID = UUID.randomUUID().toString();

        createMailPstmt.setString(1, MAIL_ID);
        createMailPstmt.setString(2, sender);
        createMailPstmt.setString(3, String.valueOf(to));
        createMailPstmt.setString(4, subject);
        createMailPstmt.setString(5, content);

        int rows = createMailPstmt.executeUpdate();

        if (rows > 0) {

          conn.commit();

          MailDomain createdMail = new MailDomain();
          createdMail.setMailId(MAIL_ID);
          createdMail.setSender(sender);
          createdMail.setReceivers(String.valueOf(to));
          createdMail.setSubject(subject);
          createdMail.setContent(content);

          return createdMail;
        }

        conn.rollback();

        return null;

      } catch (SQLException ex) {
        log.error("Error creating a new mail.", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error creating a new mail.", e);
    }

    return null;
  }
}
