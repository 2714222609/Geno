package com.example.demo.process.process2;

import com.example.demo.tools.pythonVersion.ChangePythonVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FilterSNP {
    public static void filterSNP(String localTempFileName) {
        // TODO Auto-generated method stub
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(ChangePythonVersion.pythonVersion+" src/main/java/com/example/demo/tools/py/FilterSNP.py " +
                    "src/main/java/com/example/demo/tools/py/snp_7.csv " +
                    "src/main/resources/"+localTempFileName+"/wig/ " +
                    "src/main/resources/"+localTempFileName+"/csv/");// 执行py文件
            System.out.println("called FilerSNP...");
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


