package com.template.common.utils.http;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientBuilderFactory {
    private static final int TIME_OUT_MILLISECONDS = 15 * 1000;

    private HttpClientBuilderFactory() {
    }

    public static HttpClientBuilder createHttpClientBuilder() {
        return createHttpClientBuilder(null);
    }

    public static HttpClientBuilder createHttpClientBuilder(Credentials credentials) {
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom()
                .setConnectTimeout(TIME_OUT_MILLISECONDS).setSocketTimeout(TIME_OUT_MILLISECONDS).build())
                .useSystemProperties();
        if (credentials != null) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, credentials);
            builder.setDefaultCredentialsProvider(credentialsProvider);
        }
        return builder;
    }

}
