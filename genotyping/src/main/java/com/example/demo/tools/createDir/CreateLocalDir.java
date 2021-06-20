package com.example.demo.tools.createDir;

import com.example.demo.tools.sendmail.GetAddress;

import java.io.File;

public class CreateLocalDir {
    public static void createDir(String path) {
        File localDir = new File(path);
        if(!localDir.exists()){
            localDir.mkdir();
        }
    }
    public static void createLocalDir(String tempFileName) {
        CreateLocalDir.createDir("src/main/resources/"+tempFileName);
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"csv");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"output");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"outputZip");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"output/png");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"output/xlsx");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"fqFiles");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"fqFiles/fq");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"fqFiles/source");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"wig");
        CreateLocalDir.createDir("src/main/resources/"+tempFileName+"/"+"splitFile");

        CreateLocalDir.createDir("src/main/webapp/img/"+tempFileName);
    }
    public static void createPrimerDir(String tempFileName) {
        CreateLocalDir.createDir("src/main/webapp/"+tempFileName);
        CreateLocalDir.createDir("src/main/webapp/"+tempFileName+"/"+"primerFile");
        CreateLocalDir.createDir("src/main/webapp/"+tempFileName+"/"+"primerOutPutPath");
    }
}
