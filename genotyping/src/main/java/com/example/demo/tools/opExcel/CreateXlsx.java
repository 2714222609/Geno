package com.example.demo.tools.opExcel;

import com.example.demo.tools.getPngPath.PngPath;
import com.example.demo.tools.pythonVersion.ChangePythonVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CreateXlsx {
    public static void createXlsx(String path) {
        Process proc;
        try {
            //读取test.py
            proc = Runtime.getRuntime().exec(ChangePythonVersion.pythonVersion+" src/main/java/com/example/demo/tools" +
                    "/py/mergeExcel.py"+" "+path);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
