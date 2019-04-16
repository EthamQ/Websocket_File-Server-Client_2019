package com.raphael;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class FileStorage {

    private HashMap<String, String> filenames;

    public FileStorage() {
        this.filenames = new HashMap<String, String>();
        this.filenames.put("testImage.jpg", "testimage.jpg");
        this.filenames.put("Karte4.jpg", "Karte4.jpg");
    }

    public boolean fileExists(String filename) {
        return !(this.filenames.get(filename) == null);
    }

    public ByteBuf getFile(String filename) throws IOException {
        return Unpooled.copiedBuffer(Files.readAllBytes(
            new File(this.getClass().getResource("files/" + filename).getFile()).toPath()));
    }
    
}