package com.template.common.utils.http;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

public class HttpRequestBuilder {
    private RequestBuilder builder;
    private StringEntity entity;
    private LinkedList<NameValuePair> parameters;
    private RequestConfig.Builder buildConfig;

    HttpRequestBuilder(RequestBuilder builder) {
        this.builder = builder;
    }

    public static HttpRequestBuilder get() {
        return new HttpRequestBuilder(RequestBuilder.get());
    }

    public static HttpRequestBuilder post() {
        return new HttpRequestBuilder(RequestBuilder.post());
    }

    public static HttpRequestBuilder put() {
        return new HttpRequestBuilder(RequestBuilder.put());
    }

    public static HttpRequestBuilder delete() {
        return new HttpRequestBuilder(RequestBuilder.delete());
    }

    public RequestBuilder getBuilder() {
        return builder;
    }

    public HttpRequestBuilder setUri(final String uri) {
        builder.setUri(uri);
        return this;
    }

    public HttpRequestBuilder setUri(final URI uri) {
        builder.setUri(uri);
        return this;
    }

    private RequestConfig.Builder getBuildConfig() {
        if (buildConfig == null) {
            buildConfig = RequestConfig.custom();
        }
        return buildConfig;
    }

    public HttpRequestBuilder setTimeout(int timeout) {
        getBuildConfig().setConnectTimeout(timeout).setSocketTimeout(timeout).setConnectionRequestTimeout(timeout);
        return this;
    }

    public HttpRequestBuilder setConnectTimeout(int timeOut) {
        getBuildConfig().setConnectTimeout(timeOut);
        return this;
    }

    public HttpRequestBuilder setSocketTimeout(int timeOut) {
        getBuildConfig().setSocketTimeout(timeOut);
        return this;
    }

    public HttpRequestBuilder setConnectionRequestTimeout(int timeOut) {
        getBuildConfig().setConnectionRequestTimeout(timeOut);
        return this;
    }

    public HttpRequestBuilder addParameter(final NameValuePair nvp) {
        if (nvp != null && entity == null) {
            if (HttpPost.METHOD_NAME.equalsIgnoreCase(builder.getMethod())
                    || HttpPut.METHOD_NAME.equalsIgnoreCase(builder.getMethod())) {
                if (parameters == null) {
                    parameters = new LinkedList<NameValuePair>();
                }
                parameters.add(nvp);
            } else {
                builder.addParameter(nvp);
            }
        }
        return this;
    }

    public HttpRequestBuilder addParameter(final String name, final String value) {
        return addParameter(new BasicNameValuePair(name, value));
    }

    public List<NameValuePair> getParameters() {
        return parameters != null ? new ArrayList<NameValuePair>(parameters) : builder.getParameters();
    }

    public HttpRequestBuilder addHeader(final String name, final String value) {
        if (name != null && value != null) {
            builder.addHeader(name, value);
        }

        return this;
    }

    public HttpRequestBuilder setXml(final String string) {
        entity = new StringEntity(string, Consts.UTF_8);
        entity.setContentType("application/xml");
        return this;
    }

    public HttpRequestBuilder setJson(final String string) {
        entity = new StringEntity(string, Consts.UTF_8);
        entity.setContentType("application/json");
        return this;
    }

    public HttpUriRequest build() {
        if (this.buildConfig != null) {
            this.builder.setConfig(this.buildConfig.build());
        }
        if (entity != null) {
            if (parameters != null) {
                builder.addParameters(parameters.toArray(new NameValuePair[0]));
            }
            builder.setEntity(entity);
        } else if (parameters != null) {
            builder.setEntity(new UrlEncodedFormEntity(parameters, Consts.UTF_8));
        }
        return builder.build();
    }
}
