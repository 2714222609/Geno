package com.example.demo.tools.getFiles;

import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

public class GetFiles {
    public static void getFiles(Part part1, Part part2, String tempFileName) throws IOException {
        //获取文件名
        String fileName1 = part1.getSubmittedFileName();
        String fileName2 = part2.getSubmittedFileName();

        //文件大小
        double size1 = part1.getSize();
        double size2 = part2.getSize();

        //将Part类转换为InputStream
        FileInputStream fis1 = (FileInputStream) part1.getInputStream();
        FileInputStream fis2 = (FileInputStream) part2.getInputStream();

        //将文件写入到项目目录中
        FileOutputStream fos1 = new FileOutputStream("src/main/resources/"+tempFileName+"/"+"fqFiles/source/"+fileName1);
        FileOutputStream fos2 = new FileOutputStream("src/main/resources/"+tempFileName+"/"+"fqFiles/source/"+fileName2);

        byte[] bytes1 = new byte[1024*50];
        int len1;
        while((len1 = fis1.read(bytes1)) != -1){
            fos1.write(bytes1,0,len1);
            String rate = new DecimalFormat("0").format(100 - fis1.available()/size1*100);
//            System.out.println("file 1: " + rate+"%");
        }
        fos1.close();
        fis1.close();

        byte[] bytes2 = new byte[1024*50];
        int len2;
        while ((len2 = fis2.read(bytes2)) != -1){
            fos2.write(bytes2,0,len2);
            String rate = new DecimalFormat("0").format(100 - fis2.available()/size2*100);
//            System.out.println("file 2: " + rate+"%");
        }
        fos2.close();
        fis2.close();
    }
    public static void getSplitFile(Part part, String tempFileName) throws IOException {
        //获取文件名
        String fileName = part.getSubmittedFileName();
        //文件大小
        double size = part.getSize();
        //将Part类转换为InputStream
        FileInputStream fis = (FileInputStream) part.getInputStream();
        //将文件写入到项目目录中
        FileOutputStream fos = new FileOutputStream("src/main/resources/"+tempFileName+"/"+"splitFile/"+fileName);
        byte[] bytes = new byte[1024*50];
        int len;
        while((len = fis.read(bytes)) != -1){
            fos.write(bytes,0,len);
            String rate = new DecimalFormat("0").format(100 - fis.available()/size*100);
//            System.out.println("file 1: " + rate+"%");
        }
        fos.close();
        fis.close();
    }
    public static void getPrimerFile(Part part, String tempFileName) throws IOException {
        String fileName = part.getSubmittedFileName();
        double size = part.getSize();
        FileInputStream fis = (FileInputStream) part.getInputStream();
        FileOutputStream fos = new FileOutputStream("src/main/webapp/"+tempFileName+"/"+"primerFile/"+fileName);
        byte[] bytes = new byte[1024*50];
        int len;
        while((len = fis.read(bytes)) != -1){
            fos.write(bytes,0,len);
            String rate = new DecimalFormat("0").format(100 - fis.available()/size*100);
//            System.out.println("file 1: " + rate+"%");
        }
        fos.close();
        fis.close();
    }
}
