package com.prog4.employee_db.entity;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomMultipartFile implements MultipartFile {
    private final String value;

    public CustomMultipartFile(String value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return value;
    }

    @Override
    public String getOriginalFilename() {
        return value;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        // Do nothing
    }
}
