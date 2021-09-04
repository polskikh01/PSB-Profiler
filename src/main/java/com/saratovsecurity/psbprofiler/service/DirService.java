package com.saratovsecurity.psbprofiler.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class DirService {

    public ArrayList<String> listFilesForFolder(final File folder) {
        ArrayList<String> listFiles = new ArrayList<String>();

        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                listFiles.add(fileEntry.getName());
            }
        }
        return listFiles;
    }
}
