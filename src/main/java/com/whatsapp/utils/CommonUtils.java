package com.whatsapp.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CommonUtils {
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File convFile = File.createTempFile("upload_", "_" + file.getOriginalFilename(), tempDir);

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }

        convFile.deleteOnExit();
        return convFile;
    }

}
