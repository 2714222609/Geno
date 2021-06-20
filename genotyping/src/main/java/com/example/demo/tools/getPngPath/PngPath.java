package com.example.demo.tools.getPngPath;

import com.example.demo.process.process3.Minima;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.File;

public class PngPath {
    public static String[] getPngPath(String pngPath){

        File f = new File(pngPath);
        if(!f.exists()) {
            System.out.println(pngPath + "not exists");
        }
        File[] fa = f.listFiles();
        String[] pngName = new String[fa.length];
        for(int i = 0;i < fa.length; i++){
            pngName[i] = fa[i].getName();
        }
        return pngName;
    }
}
