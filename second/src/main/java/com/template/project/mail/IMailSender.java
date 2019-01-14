package com.template.project.mail;

import java.util.Date;

/**
 * Created by facheng on 19-1-14.
 */
public interface IMailSender {

    Date sendMail(String from, String fromName, String allRecipients, String subject, String replyToName,
            String replyToAddress, String mailBody);
}
