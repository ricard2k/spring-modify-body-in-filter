package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CspResponseWrapper extends HttpServletResponseWrapper {

    private final CspServletOutputStream out;
    private final PrintWriter writer;

    public CspResponseWrapper(@Nonnull HttpServletResponse response, @Nonnull String nonceValue) {
        super(response);
        final Charset charset = Charset.forName(response.getCharacterEncoding() == null? StandardCharsets.UTF_8.name() : response.getCharacterEncoding());
        out = new CspServletOutputStream(nonceValue, charset);
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
        return this.out.toByteArray();
    }
}
