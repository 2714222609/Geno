package com.example.demo.tools.primerDesign;

import com.example.demo.tools.pythonVersion.ChangePythonVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DoPrimerDesign {
    public static void doPrimerDesign(int strategy, String primerFile, String primerOutPutPath, String tempFileName,
                                      int priOpt, int priMin, int priMax, int proMin, int proMax, double tmOpt,double tmMin,
                                      double tmMax){
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(ChangePythonVersion.pythonVersion+" src/main/java/com/example/demo/tools/py/Primers.py"+
                    " "+strategy+" "+ primerFile+" "+primerOutPutPath+" "+tempFileName+" "+priOpt+" "+priMin+" "+priMax+" "+proMin+" "
                    +proMax+" "+tmOpt +" "+tmMin+" "+tmMax);// 执行py文件
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
