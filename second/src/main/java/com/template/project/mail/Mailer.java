package com.template.project.mail;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.template.project.mail.domain.Mail;
import com.template.project.mail.service.IMailService;

@Component
public class Mailer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mailer.class);

    @Autowired
    @Qualifier("sendCloudSender")
    private IMailSender mailSender;

    @Autowired
    private IMailService mailService;

    @Value("${mailer.address}")
    private String senderAddress;

    @Value("${mailer.skipSending}")
    private boolean skipSending;

    /**
     * mail sending is done using up to five worker threads
     */
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    // org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
    public Mailer() {
        executor.allowCoreThreadTimeOut(true);
    }

    @Transactional
    public void send() {
        for (Mail m : mailService.getMailBatch()) {
            send(m.getId());
        }
    }

    @Transactional
    public void cleanUpOldMails() {
        mailService.removeOldMails();
    }

    @Transactional
    public void removeMailBody() {
        mailService.removeMailsBody();
    }

    public Long store(Mail mail) {
        mailService.store(mail);
        return mail.getId();
    }

    void send(final Long mailId) {
        if (Boolean.TRUE.equals(skipSending)) {
            LOGGER.info("Email sending disabled");
            return;
        }

        executor.execute(new Runnable() {

            @Override
            @Transactional
            public void run() {
                long start = System.currentTimeMillis();
                try {
                    Mail mail = mailService.getMail(mailId);
                    // could have been already sent by now, then we do nothing
                    if (mail == null || mail.getSendAt() != null)
                        return;
                    Date sentAt;
                    sentAt = sendMail(mail.getRecipient(), mail.getSubject(), mail.getMailBody(), mail.getReplyToName(),
                            mail.getReplyToAddress(), mail.getFromAddress(), mail.getFromName());
                    // if mailing failed don't do anything
                    if (sentAt == null)
                        return;

                    mail.send(sentAt);
                    mailService.updateSendTime(mail);
                    LOGGER.debug("mail {} send at {}", mail.getId(), sentAt);
                } catch (Exception e) {
                    LOGGER.error("Failed to send mail {}", mailId, e);
                } finally {
                    LOGGER.info("approximate #of active threads={}, current no of threads in pool={}, time spent={}",
                            executor.getActiveCount(), executor.getPoolSize(), (System.currentTimeMillis() - start));
                }
            }
        });
    }

    private Date sendMail(String allRecipients, String subject, String mailBody, String replyToName,
            String replyToAddress, String fromAddress, String fromName) {

        String from = StringUtils.stripToNull(senderAddress);

        // check if we need to use the SendCloud API or normal SMTP
        if (mailSender != null) {
            return mailSender.sendMail(from, fromName, allRecipients, subject, replyToName, replyToAddress, mailBody);
        } else {
            return null;
        }

    }

}
