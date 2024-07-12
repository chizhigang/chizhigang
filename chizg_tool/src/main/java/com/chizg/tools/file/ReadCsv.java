package com.chizg.tools.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ReadCsv {

    public static void main(String[] args) {
        Set<String> citySet = new HashSet<>();

        try (
                BufferedReader br = Files.newBufferedReader(Paths.get("E:\\IP2LOCATION-LITE-DB3.CSV"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);

                // ip ip cn 国家 城市
                if("\"China\"".equals(columns[3])){
                    System.out.println(columns[4]);
                    citySet.add(columns[4]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(citySet.toString());
    }
}
