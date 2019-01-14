package com.template.project.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("sendCloudSender")
public class SendCloudSupport implements IMailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendCloudSupport.class);

    @Value("${mailer.sendCloud.url}")
    private String url;

    @Value("${mailer.sendCloud.username}")
    private String username;

    @Value("${mailer.sendCloud.password}")
    private String password;

    // set timeout parameters
    @Value("${mailer.sendCloud.httpClientTimeout}")
    private int timeout = 30; // unit is second

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Date sendMail(String from, String fromName, String allRecipients, String subject, String replyToName,
            String replyToAddress, String mailBody) {
        LOGGER.info("about to send email by sendcloud. allRecipients: " + allRecipients);
        Date sendDate = new Date();

        String[] recipients = allRecipients.split(";");
        for (String singleRecipient : recipients) {
            CloseableHttpClient httpclient = null;
            try {
                httpclient = createHttpClient();
                HttpPost post = new HttpPost(url);

                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("api_user", username));
                params.add(new BasicNameValuePair("api_key", password));
                params.add(new BasicNameValuePair("from", from));
                params.add(new BasicNameValuePair("fromname", fromName));
                params.add(new BasicNameValuePair("to", singleRecipient));
                params.add(new BasicNameValuePair("replyto", replyToAddress));
                params.add(new BasicNameValuePair("subject", subject));
                params.add(new BasicNameValuePair("html", mailBody));

                post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                HttpResponse response = httpclient.execute(post);
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    LOGGER.error("Cannot send mail to {} {}", singleRecipient, response.getStatusLine());
                    return null;
                }
            } catch (SSLException e) {
                // SSL frequently fails, we can just retry, don't need to spam
                // the error log
                LOGGER.warn("Connection problem sending mail to {}: {} ", singleRecipient, e.toString());
                return null;
            } catch (Exception e) {
                // SSL frequently fails, we can just retry, don't need to spam
                // the error log
                LOGGER.warn("Connection problem sending mail to {}: {} ", singleRecipient, e.toString());
                System.out.println("Exception problem sending mail " + e);
                return null;
            } finally {
                closeHttpClient(httpclient);
            }
        }
        return sendDate;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private CloseableHttpClient createHttpClient() {
        HttpClientBuilder builder = HttpClientBuilder.create().useSystemProperties();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000).build();
        builder.setDefaultRequestConfig(requestConfig);
        return builder.build();
    }

    private static void closeHttpClient(CloseableHttpClient httpClient) {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            LOGGER.error("httpclient close exception", e);
        }
    }

}
