package com.template.framework.filter;

/**
 * Created by feng on 2019/3/6.
 */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

class BufferedRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] buffer;

    public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        // Read InputStream and store its content in a buffer.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(request.getInputStream(), baos);
        buffer = baos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new BufferedServletInputStream(new ByteArrayInputStream(buffer));
    }

    String getRequestBody(int limit) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
        StringBuilder sb = new StringBuilder();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                sb.append(line.trim());
            }
        } finally {
            reader.close();
        }
        if (limit <= 0 || limit >= sb.length()) {
            return sb.toString();
        } else {
            return sb.substring(0, limit);
        }
    }
}

class BufferedServletInputStream extends ServletInputStream {
    private ByteArrayInputStream bais;

    public BufferedServletInputStream(ByteArrayInputStream bais) {
        this.bais = bais;
    }

    @Override
    public int available() {
        return bais.available();
    }

    @Override
    public int read() {
        return bais.read();
    }

    @Override
    public int read(byte[] buf, int off, int len) {
        return bais.read(buf, off, len);
    }

    @Override
    public boolean isFinished() {
        return bais.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
