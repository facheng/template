package com.template.project.mail.mapper;

import java.util.List;
import java.util.Map;

import com.template.project.mail.domain.Mail;

public interface MailMapper {

    void store(Mail mail);

    List<Mail> getMailBatch(Map<String, Object> param);

    void removeOldMails(Map<String, Object> param);

    void removeMailsBody(Map<String, Object> param);

    Mail getMail(Long id);

    void updateSendTime(Mail mail);
}
