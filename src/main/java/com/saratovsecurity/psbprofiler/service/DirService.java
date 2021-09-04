package com.saratovsecurity.psbprofiler.service;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DirService {
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }
}
