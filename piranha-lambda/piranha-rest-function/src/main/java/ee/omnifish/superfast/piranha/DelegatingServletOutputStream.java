package ee.omnifish.superfast.piranha;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class DelegatingServletOutputStream extends ServletOutputStream {
    
    private final OutputStream out;

    public DelegatingServletOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
    
}
