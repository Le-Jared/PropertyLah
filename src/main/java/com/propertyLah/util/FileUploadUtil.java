package com.propertyLah.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUploadUtil {
    public static void saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        // Get the project's root directory
        String projectDir = System.getProperty("user.dir");

        // Define the path to the static/img directory
        String uploadDir = projectDir + "/src/main/resources/static/img";

        Path uploadPath = Paths.get(uploadDir);

        // Create directories if they don't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Copy contents of MultipartFile to new file on disk
        Path filePath = uploadPath.resolve(fileName);
        multipartFile.transferTo(filePath.toFile());
    }
}





