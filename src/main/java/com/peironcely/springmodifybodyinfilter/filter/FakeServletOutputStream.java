package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;

@Slf4j
public class FakeServletOutputStream extends ServletOutputStream {

    @Getter
    private final ByteArrayOutputStream buffer;

    private int size = 0;

    public FakeServletOutputStream() {
        this.buffer = new ByteArrayOutputStream();
    }

    @Override
    public void write(int b) {
        buffer.write((char) b);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    @SneakyThrows
    public void setWriteListener(WriteListener writeListener) {
        writeListener.onWritePossible();
    }
}
