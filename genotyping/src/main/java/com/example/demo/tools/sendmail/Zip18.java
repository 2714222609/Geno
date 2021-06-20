package com.example.demo.tools.sendmail;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/*用于压缩文件夹*/
public class Zip18 {

    public static void compreSsion(String zipFileName, File target) throws IOException {//压缩
        System.out.println("file compressing...");
        ZipOutputStream out=new ZipOutputStream(new FileOutputStream(zipFileName));
        BufferedOutputStream bos=new BufferedOutputStream(out);
        zip(out,target,target.getName(),bos);
        bos.close();
        out.close();
        System.out.println("file compression completed...");
    }

    public static void zip(ZipOutputStream zout, File target, String name, BufferedOutputStream bos) throws IOException {
        //判断是不是目录
        if (target.isDirectory()) {
            File[] files = target.listFiles();
            if (files.length == 0) {//空目录
                zout.putNextEntry(new ZipEntry(name + "/"));
            /*  开始编写新的ZIP文件条目，并将流定位到条目数据的开头。
              关闭当前条目，如果仍然有效。 如果没有为条目指定压缩方法，
              将使用默认压缩方法，如果条目没有设置修改时间，将使用当前时间。*/
            }
            for (File f : files) {
                //递归处理
                zip(zout, f, name + "/" + f.getName(), bos);
            }
        } else {
            zout.putNextEntry(new ZipEntry(name));
            InputStream inputStream = new FileInputStream(target);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[16];
            int len = -1;
            while ((len = bis.read(bytes)) != -1) {
                zout.write(bytes, 0, len);
            }
            bis.close();
        }
    }
}
