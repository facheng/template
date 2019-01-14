package com.template.project.mail.service;

import java.util.List;

import com.template.project.mail.domain.Mail;

public interface IMailService {

    void store(Mail mail);

    List<Mail> getMailBatch();

    void removeOldMails();

    void removeMailsBody();

    Mail getMail(Long id);

    void updateSendTime(Mail mail);
}
