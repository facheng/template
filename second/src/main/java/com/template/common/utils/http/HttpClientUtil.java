package com.template.common.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.Credentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final Integer ENTITY_LOGGING_LIMIT = 100 * 1024;

    private static AtomicLong idCounter = new AtomicLong();

    private HttpClientUtil() {
    }

    public static HttpResponse callHttpRequest(final HttpUriRequest request) {
        return callHttpRequest(HttpClientBuilderFactory.createHttpClientBuilder(), request);
    }

    public static HttpResponse callHttpRequest(final HttpUriRequest request, final Credentials credentials) {
        return callHttpRequest(HttpClientBuilderFactory.createHttpClientBuilder(credentials), request);
    }

    public static String parseHttpResponse(final HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (IOException e) {
            throw new HttpClientUtilException("Failed to parse http response: " + response.toString(), e);
        }
    }

    private static HttpResponse callHttpRequest(HttpClientBuilder builder, final HttpUriRequest request) {
        try {
            long id = idCounter.getAndIncrement();
            LOGGER.info(">> {}", getTrace(id, request));
            HttpResponse response = builder.build().execute(request);
            StatusLine status = response.getStatusLine();
            response.setEntity(new BufferedHttpEntity(response.getEntity()));
            LOGGER.info("<< {}", getTrace(id, response));
            if (HttpStatus.SC_OK != status.getStatusCode()) {
                throw new HttpClientUtilException(
                        MessageFormat.format("Http status {0} {1}", status.getStatusCode(), status.getReasonPhrase()));
            }
            return response;
        } catch (IOException e) {
            throw new HttpClientUtilException("Failed to call http request: " + request.toString(), e);
        }
    }

    private static Map<String, Object> getTrace(long id, HttpUriRequest request) {
        Map<String, Object> trace = new LinkedHashMap<String, Object>();
        trace.put("id", id);
        try {
            trace.put("URI", URLDecoder.decode(request.getURI().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn("Failed to decode URI", e);
        }
        trace.put("method", request.getMethod());
        trace.put("headers", parseHeaders(request.getAllHeaders()));
        if (request instanceof HttpEntityEnclosingRequest) {
            HttpEntityEnclosingRequest entityRequest = (HttpEntityEnclosingRequest) request;
            try {
                boolean urlEncoded = entityRequest.getEntity() instanceof UrlEncodedFormEntity;
                HttpEntity entity = new BufferedHttpEntity(entityRequest.getEntity());
                entityRequest.setEntity(entity);
                String entityString = readEntityTrimSpace(entity);
                if (urlEncoded) {
                    entityString = URLDecoder.decode(entityString, "UTF-8");
                }
                trace.put("entity", entityString);
            } catch (IOException e) {
                LOGGER.warn("Failed to read HTTP request entity", e);
            }
        }
        return trace;
    }

    private static Map<String, Object> getTrace(long id, HttpResponse response) {
        Map<String, Object> trace = new LinkedHashMap<String, Object>();
        trace.put("id", id);
        trace.put("status", response.getStatusLine());
        trace.put("headers", parseHeaders(response.getAllHeaders()));
        try {
            HttpEntity entity = new BufferedHttpEntity(response.getEntity());
            response.setEntity(entity);
            trace.put("entity", readEntityTrimSpace(entity));
        } catch (IOException e) {
            LOGGER.warn("Failed to read HTTP response entity", e);
        }
        return trace;
    }

    private static Map<String, Object> parseHeaders(Header[] headers) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        for (Header header : headers) {
            result.put(header.getName(), header.getValue());
        }
        return result;
    }

    private static String readEntityTrimSpace(HttpEntity entity) throws IOException {
        Charset charset = null;
        try {
            charset = ContentType.get(entity).getCharset();
        } catch (RuntimeException e) {
            LOGGER.debug("Failed to parse entity type", e);
        }
        if (charset == null) {
            charset = Consts.UTF_8;
        }
        List<String> lines = IOUtils.readLines(entity.getContent(), charset);
        final StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(line.trim());
            if (sb.length() > ENTITY_LOGGING_LIMIT) {
                sb.setLength(ENTITY_LOGGING_LIMIT);
                break;
            }
        }
        return sb.toString();
    }
}
