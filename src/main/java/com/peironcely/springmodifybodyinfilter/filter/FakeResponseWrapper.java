package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;

public class FakeResponseWrapper extends HttpServletResponseWrapper {

    private final FakeServletOutputStream out;
    private final PrintWriter writer;

    public FakeResponseWrapper(HttpServletResponse response) {
        super(response);
        out = new FakeServletOutputStream();
        writer = new PrintWriter(out);
    }

    @Override
    public PrintWriter getWriter() {
        return this.writer;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.out;
    }

    protected byte[] getBody() {
        writer.flush();
        return this.out.getBuffer().toByteArray();
    }
}
