package com.chizg.tools.file;

import com.chizg.tools.date.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class OutFile {

    private static final String FILE_FOLDER = "E:\\develop\\file";
    public static void toFile(String fileName,String format, List<String> arr) throws IOException {
        String filePath = FILE_FOLDER +"\\"+ DateUtils.getYYYYMMDD();
        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            Files.createDirectory(path);
        }
        Path file = Paths.get(filePath + "\\" + fileName);
        if(Files.exists(file)){
            File directory = new File(filePath + "\\" + fileName);
            directory.delete();
        }
        Files.createFile(file);

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,  StandardOpenOption.APPEND)) {
            for (int i = 0; i < arr.size(); i++) {
                String format1 = String.format(format,arr.get(i), arr.get(i));
                System.out.println(format1);
                writer.write(format1);
            }
        }
    }
}
