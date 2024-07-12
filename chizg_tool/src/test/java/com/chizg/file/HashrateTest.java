package com.chizg.file;

import org.junit.Test;

import java.io.File;

public class HashrateTest {

    @Test
    public void readTxt() {
        File folder = new File("E:\\data\\test");
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        }
    }
}
