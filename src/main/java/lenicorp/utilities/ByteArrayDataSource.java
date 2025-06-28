package lenicorp.utilities;

import jakarta.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteArrayDataSource implements DataSource {
    
    private final byte[] data;
    private final String contentType;
    
    public ByteArrayDataSource(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }
    
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Not Supported");
    }
    
    @Override
    public String getContentType() {
        return contentType;
    }
    
    @Override
    public String getName() {
        return "ByteArrayDataSource";
    }
}