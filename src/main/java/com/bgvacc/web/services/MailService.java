package com.bgvacc.web.services;

import com.bgvacc.web.domains.MailDomain;
import com.bgvacc.web.requests.atc.ATCApplicationRequest;
import com.bgvacc.web.utils.Names;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface MailService {

  boolean sendNewATCTrainingApplicationMail(ATCApplicationRequest atcApplication);

  boolean sendForgottenPasswordMail(Names names, String email, String passwordResetToken);

  boolean sendUserCreatedMail(Names names, String cid, String email, String temporaryPassword);
  
  boolean sendEventControllerApplicationApprovedMail(Names names, String cid, String email, Long eventId, String eventName, String position, String date, String timeSlot);

  MailDomain createMail(String sender, String subject, String content, String... recipients);

}
