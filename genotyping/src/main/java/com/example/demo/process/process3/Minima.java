package com.example.demo.process.process3;

import com.example.demo.tools.pythonVersion.ChangePythonVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Minima {
    public static void minima(String localTempFileName,String inputCsvFileName) {
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(ChangePythonVersion.pythonVersion+
                    " src/main/java/com/example/demo/tools/py/Minima.py" +
                    " src/main/resources/"+localTempFileName+"/csv/"+inputCsvFileName+
                    " src/main/resources/"+localTempFileName+"/output/xlsx/" +
                    " src/main/resources/"+localTempFileName+"/output/png/");
            System.out.println("called minima...");
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
