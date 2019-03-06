package com.template.framework.filter;

/**
 * Created by feng on 2019/3/6.
 */
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * http://wetfeetblog.com/servlet-filer-to-log-request-and-response
 */
public class RestApiLoggingFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiLoggingFilter.class);

    private static final String[] DEFAULT_TEXT_TYPES = { "text/plain", "text/html", "text/xml", "application/json",
            "application/xml", "application/x-www-form-urlencoded" };

    private static AtomicLong idCounter = new AtomicLong();

//    @Value("${api.filter.logging.exclude:}")
    private String[] excludes;

//    @Value("${api.filter.logging.textTypes:}")
    private String[] textTypes;

//    @Value("${api.filter.logging.limit:102400}")
    private int limit;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (loggingEnabled(request)) {
            BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(request);
            BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(response);

            long id = idCounter.getAndIncrement();
            logRequest(id, bufferedReqest);
            try {
                filterChain.doFilter(bufferedReqest, bufferedResponse);
            } finally {
                logResponse(id, bufferedResponse);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void logRequest(long id, BufferedRequestWrapper request) {
        LOGGER.info("<< {}", getTrace(id, request));
    }

    private void logResponse(long id, BufferedResponseWrapper response) {
        LOGGER.info(">> {}", getTrace(id, response));
    }

    private Map<String, Object> getTrace(long id, BufferedRequestWrapper request) {
        Map<String, Object> headers = new LinkedHashMap<String, Object>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, trimList(Collections.list(request.getHeaders(name))));
        }

        Map<String, Object> parameters = new LinkedHashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameters.put(name, trimList(Arrays.asList(request.getParameterValues(name))));
        }

        Map<String, Object> trace = new LinkedHashMap<String, Object>();
        trace.put("id", id);
        trace.put("remoteAddr", request.getRemoteAddr());
        trace.put("method", request.getMethod());
        trace.put("path", request.getRequestURI());
        trace.put("headers", headers);
        if (!parameters.isEmpty()) {
            trace.put("parameters", parameters);
        }

        try {
            if (isTextType(request.getContentType())) {
                trace.put("payload", request.getRequestBody(-1));
            } else {
                trace.put("payload", request.getRequestBody(limit));
            }
        } catch (IOException e) {
            LOGGER.warn("Error reading HTTP request content", e);
        }
        return trace;
    }

    private Map<String, Object> getTrace(long id, BufferedResponseWrapper response) {
        Map<String, Object> headers = new LinkedHashMap<String, Object>();
        for (String name : response.getHeaderNames()) {
            headers.put(name, trimList(response.getHeaders(name)));
        }
        Map<String, Object> trace = new LinkedHashMap<String, Object>();
        trace.put("id", id);
        trace.put("status", response.getStatus());
        trace.put("headers", headers);
        if (isTextType(response.getContentType())) {
            trace.put("payload", response.getContent(-1));
        } else {
            trace.put("payload", response.getContent(limit));
        }
        return trace;
    }

    private Object trimList(Collection<String> values) {
        if (values.size() == 1) {
            return values.iterator().next();
        } else if (values.isEmpty()) {
            return "";
        } else {
            return values;
        }
    }

    private boolean isTextType(String contentType) {
        return StringUtils.startsWithAny(contentType, DEFAULT_TEXT_TYPES)
                || StringUtils.startsWithAny(contentType, textTypes);
    }

    private boolean loggingEnabled(HttpServletRequest request) {
        return !ArrayUtils.contains(excludes, request.getRequestURI());
    }
}
