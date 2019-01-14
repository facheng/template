package com.template.project.mail.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.template.project.mail.domain.Mail;
import com.template.project.mail.mapper.MailMapper;
import com.template.project.mail.service.IMailService;

@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private MailMapper mapper;

    @Override
    public void store(Mail mail) {
        mapper.store(mail);
    }

    @Override
    public List<Mail> getMailBatch() {
        LocalDateTime time = LocalDateTime.now().minusMinutes(3);
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", localDateTimeToDate(time));
        return mapper.getMailBatch(params);
    }

    @Override
    public void removeOldMails() {
        LocalDateTime time = LocalDateTime.now().minusDays(90).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", localDateTimeToDate(time));
        mapper.removeOldMails(params);

    }

    @Override
    public void removeMailsBody() {
        LocalDateTime time = LocalDateTime.now().minusHours(72).withMinute(0).withSecond(0).withNano(0);
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", localDateTimeToDate(time));
        mapper.removeMailsBody(params);
    }

    @Override
    public Mail getMail(Long id) {
        return mapper.getMail(id);
    }

    private Date localDateTimeToDate(LocalDateTime time) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = time.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    @Override
    public void updateSendTime(Mail mail) {
        mapper.updateSendTime(mail);
    }

}
