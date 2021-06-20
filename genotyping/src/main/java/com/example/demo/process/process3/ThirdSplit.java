package com.example.demo.process.process3;

import com.example.demo.tools.pythonVersion.ChangePythonVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThirdSplit {
    public static void thirdSplit(String localTempFileName) {
        // TODO Auto-generated method stub
        Process proc;
        try {
            String path = "src/main/resources/"+localTempFileName+"/fqFiles/source";
            File f = new File(path);
            if(!f.exists()) {
                System.out.println(path + "not exists");
            }
            File fa[] = f.listFiles();
            String fqName1 = fa[0].getName();
            String fqName2 = fa[1].getName();

            proc = Runtime.getRuntime().exec(ChangePythonVersion.pythonVersion +" src/main/java/com/example/demo/tools/py/ThirdSplit.py" +
                    " src/main/java/com/example/demo/tools/py/barcode_96.txt" +
                    " src/main/resources/"+localTempFileName+"/fqFiles/source/" + fqName1 +
                    " src/main/resources/"+localTempFileName+"/fqFiles/source/" + fqName2 + " "+ localTempFileName);// 执行py文件
            //用输入输出流来截取结果
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
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
