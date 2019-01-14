package com.template.project.mail.domain;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Mail {
    private Long id;
    private Long version;
    private Date createdAt;
    private Date sendAt;
    private String subject;
    private String recipient;
    private String mailBody;
    private String replyToName;
    private String replyToAddress;
    private String fromAddress;
    private String fromName;

    public Mail() {
    }

    public Mail(String subject, String recipient, String mailBody) {
        createdAt = new Date();
        this.subject = subject;
        this.recipient = StringUtils.stripToNull(recipient);
        this.mailBody = mailBody;
    }

    public Mail(String subject, String recipient, String mailBody, String replyToName, String replyToAddress,
            String fromAddress, String fromName) {
        this(subject, recipient, mailBody);
        this.fromAddress = fromAddress;
        this.fromName = fromName;
        this.replyToAddress = replyToAddress;
        this.replyToName = replyToName;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void send(Date sendAt) {
        this.sendAt = sendAt;
    }

    public void clearMailBody() {
        mailBody = StringUtils.EMPTY;
    }

    public String getReplyToName() {
        return replyToName;
    }

    public void setReplyToName(String replyToName) {
        this.replyToName = replyToName;
    }

    public String getReplyToAddress() {
        return replyToAddress;
    }

    public void setReplyToAddress(String replyToAddress) {
        this.replyToAddress = replyToAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
