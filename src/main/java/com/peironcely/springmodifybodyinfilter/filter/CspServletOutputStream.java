package com.peironcely.springmodifybodyinfilter.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Arrays;

@Slf4j
public class CspServletOutputStream extends ServletOutputStream {

    private byte[] buf = new byte[32];
    final private String nonceTag;
    final private Charset charset;
    private int count = 0;

    public CspServletOutputStream(@Nonnull String nonceValue, @Nonnull Charset charset) {
        this.nonceTag = "nonce='%s'".formatted(nonceValue);
        if ("a".getBytes(charset).length>1) {throw new IllegalArgumentException("Multi-byte charset not supported");}
        if ("<".getBytes(charset)[0] != '<') {throw new IllegalArgumentException("Character sets not ASCII based are not Supported");}
        this.charset = charset;
    }

    @Override
    public synchronized void write(int b) {
        ensureCapacity(count + 1);
        buf[count] = (byte) b;
        count += 1;
        endsWithScriptTag();
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

    //copied from ByteArrayOutputStream
    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buf.length;
        int minGrowth = minCapacity - oldCapacity;
        if (minGrowth > 0) {
            buf = Arrays.copyOf(buf, newLength(oldCapacity, minGrowth, oldCapacity ));
        }
    }

    //nearly copied from jdk.internal.util.ArraysSupport
    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if ((0 < prefLength) && (prefLength <= (Integer.MAX_VALUE-8))) {
            return prefLength;
        } else {
            throw new OutOfMemoryError( "Required array length " + oldLength + " + " + minGrowth + " is too large");
        }
    }

    //Does not work for multi-byte character sets
    private void endsWithScriptTag() {
        if((count > 7) && ('t' == (char)buf[count-1]) && ('p' == (char)buf[count-2]) && ('i' == (char)buf[count-3]) && ('r' == (char)buf[count-4]) && ('c' == (char)buf[count-5]) && ('s' == (char)buf[count-6]) && ('<' == (char)buf[count-7])) {
            ensureCapacity(count + nonceTag.length() + 1);
            buf[count] = ' ';
            count += 1;
            for (byte b : nonceTag.getBytes(charset)) {
                buf[count] = b;
                count += 1;
            }
        }
    }

    public synchronized byte[] toByteArray(){
        return Arrays.copyOf(buf, count);
    }
}
