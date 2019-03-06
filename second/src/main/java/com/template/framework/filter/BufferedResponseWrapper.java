package com.template.framework.filter;

/**
 * Created by feng on 2019/3/6.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;

public class BufferedResponseWrapper extends HttpServletResponseWrapper {
    private final HttpServletResponse original;

    private TeeServletOutputStream tee;
    private ByteArrayOutputStream bos;

    public BufferedResponseWrapper(HttpServletResponse response) {
        super(response);
        original = response;
    }

    public String getContent(int limit) {
        if (bos != null) {
            if (limit <= 0 || limit >= bos.size()) {
                return bos.toString();
            } else {
                return new String(bos.toByteArray(), 0, limit);
            }
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return original.getWriter();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (tee == null) {
            bos = new ByteArrayOutputStream();
            tee = new TeeServletOutputStream(original.getOutputStream(), bos);
        }
        return tee;
    }
}

class TeeServletOutputStream extends ServletOutputStream {
    private final TeeOutputStream targetStream;

    public TeeServletOutputStream(OutputStream one, OutputStream two) {
        targetStream = new TeeOutputStream(one, two);
    }

    @Override
    public void write(int b) throws IOException {
        targetStream.write(b);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        targetStream.flush();
    }

    @Override
    public void close() throws IOException {
        super.close();
        targetStream.close();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
