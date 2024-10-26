package com.bgvacc.web.services;

import com.bgvacc.web.domains.MailDomain;
import com.bgvacc.web.requests.atc.ATCApplicationRequest;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface MailService {

  boolean sendNewATCTrainingApplicationMail(ATCApplicationRequest atcApplication);

  MailDomain createMail(String sender, String subject, String content, String... recipients);

}
