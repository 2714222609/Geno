package com.example.demo.Controller;

import com.example.demo.Sftp.GetRemoteFileName;
import com.example.demo.Sftp.SftpUpload3;
import com.example.demo.process.process3.Minima;
import com.example.demo.tools.clearDir.ClearRemoteDir;
import com.example.demo.tools.clearDir.DeleteFolderAtRegularTime;
import com.example.demo.tools.createDir.CreateLocalDir;
import com.example.demo.tools.createDir.CreateRemoteDir;
import com.example.demo.tools.getFiles.GetFiles;
import com.example.demo.process.process2.*;
import com.example.demo.process.process3.*;

import com.example.demo.tools.getPngPath.PngPath;
import com.example.demo.tools.opExcel.CreateXlsx;
import com.example.demo.tools.opExcel.ReadXlsx;
import com.example.demo.tools.primerDesign.DoPrimerDesign;
import com.example.demo.tools.sendmail.GetAddress;
import com.example.demo.Sftp.SftpDownload;
import com.example.demo.Sftp.SftpUpload;
import com.example.demo.tools.sendmail.SendEmail;
import com.jcraft.jsch.*;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@MultipartConfig
public class HelloController extends HttpServlet {
    @ResponseBody
    //Home page
    @RequestMapping("/home")
    public ModelAndView homePageFunc() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("homePage");
        return modelAndView;
    }

    //uploadRequest page
    @RequestMapping("/indexUpload")
    public ModelAndView uploadPageFunc() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadRequest");
        return modelAndView;
    }

    //primerdesign page
    @RequestMapping("/primerdesign")
    public ModelAndView primerdesign() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("primer");
        return modelAndView;
    }
    //do primer
    @RequestMapping("/doPrimer")
    public ModelAndView doPrimer(HttpServletRequest request, HttpServletResponse response, int strategy, int priMin,
                                int priOpt, int priMax, int proMin, int proMax, double tmMin, double tmOpt,
                                double tmMax) throws IOException, ServletException, ParseException {
        //创建临时文件夹，存储用户上传的引物设计文件

        long ts = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(ts);
        DecimalFormat df = new DecimalFormat("#");
        String tempFileName = formatter.format(date)+"-"+df.format((Math.random()+1) * 1000);
        CreateLocalDir.createPrimerDir(tempFileName);

        //获取引物设计文件
        Part part = request.getPart("primerFile");
        GetFiles.getPrimerFile(part,tempFileName);

        //调用Primer.py，进行引物设计
        String path = "src/main/webapp/"+tempFileName+"/primerFile";
        File f = new File(path);
        File[] fa = f.listFiles();
        assert fa != null;
        String primerFileName = fa[0].getName();
        String primerFile = "src/main/webapp/"+tempFileName+"/primerFile/"+primerFileName;
        String primerOutPutPath = "src/main/webapp/"+tempFileName+"/primerOutPutPath";
        DoPrimerDesign.doPrimerDesign(strategy,primerFile,primerOutPutPath,"user_"+tempFileName,priOpt,priMin,priMax,proMin,proMax,
                tmOpt,tmMin,tmMax);

        //7天后，自动删除文件
        DeleteFolderAtRegularTime.deletePrimer(tempFileName);

        //结果展示第一行示例
        ArrayList<String> args = new ArrayList<>();
        ReadXlsx.readXlsx(tempFileName,args);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("primer_result");
        modelAndView.addObject("tempFileName",tempFileName);
        modelAndView.addObject("xlsxFileName","user_"+tempFileName+".xlsx");
        modelAndView.addObject("txtFileName","user_"+tempFileName+".txt");
        modelAndView.addObject("ProductSize0",args.get(1));
        modelAndView.addObject("PrimerLeftSequence0",args.get(2));
        modelAndView.addObject("PrimerRightSequence0",args.get(3));
        modelAndView.addObject("PrimerLeftTm0",args.get(4));
        modelAndView.addObject("PrimerRightTm0",args.get(5));
        modelAndView.addObject("PrimerLeftGC0",args.get(6));
        modelAndView.addObject("PrimerRightGC0",args.get(7));
        modelAndView.addObject("PrimerLeftPosition0",args.get(8));
        modelAndView.addObject("PrimerRightPosition0",args.get(9));
        modelAndView.addObject("PrimerLeftPositionbp0",args.get(10));
        modelAndView.addObject("PrimerRightPositionbp0",args.get(11));
        return modelAndView;

    }

    @RequestMapping("/upload")
    public ModelAndView uploadFunc(HttpServletRequest request, HttpServletResponse response, String mailAddress) throws Exception {
        request.setCharacterEncoding("UTF-8");

        //生成临时文件夹，用于存储程序运行过程中生成的各种文件
        String tempFileName = GetAddress.getAddress(mailAddress);
        CreateLocalDir.createLocalDir(tempFileName);

        //获取name.txt
        Part part3 = request.getPart("file3");
        double size = part3.getSize();
        if(size != 0.0){
            GetFiles.getSplitFile(part3,tempFileName);
        }

        //获取用户上传的文件，获取到的形式为Part类
        Part part1 = request.getPart("file1");
        Part part2 = request.getPart("file2");

        //获取文件名
        String fileName1 = part1.getSubmittedFileName();
        String fileName2 = part2.getSubmittedFileName();

        //获取文件
        GetFiles.getFiles(part1,part2,tempFileName);

        //提示文件已上传
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println("<div>文件已上传，请注意查收邮箱！您可以关闭此页面。</div> <br/>");
        response.getWriter().flush();

        //调用样本拆分算法，将用户上传的两个fq文件拆分为多个fq文件
        System.out.println("calling SampleSplitAlgo...");
        response.getWriter().println("<div>calling SampleSplitAlgo...</div> <br/>");
        response.getWriter().flush();
        String fqFiles = "src/main/resources/"+tempFileName+"/"+"fqFiles/";
        SampleSplitAlgo.split(fqFiles+"fq/","src/main/resources/"+tempFileName+"/splitFile/name.txt",
                fqFiles+"source/"+fileName1,fqFiles+"source/"+fileName2);
        System.out.println("split finished...");
        response.getWriter().println("<div>split finished...</div> <br/>");
        response.getWriter().flush();

        //connect to a channel
        ChannelSftp channel = SftpUpload.connect();

        //创建服务器上的文件夹
        CreateRemoteDir.createRemoteDir2(tempFileName);

        //将文件上传至bwa所在服务器
        final int count = 96;
        for (int i = 0; i < count; i++) {
            //convert the files to input stream
            fileName1 = i + "_1.fq";
            fileName2 = i + "_2.fq";
            File file1 = new File(fqFiles + "fq/" + fileName1);
            File file2 = new File(fqFiles + "fq/" + fileName2);
            InputStream is1 = new FileInputStream(file1);
            InputStream is2 = new FileInputStream(file2);

            //upload files to the server
            SftpUpload.transfer("/home/hanrui/files/tempfiles/"+tempFileName+"/process2"+"/fq/",channel, is1, fileName1);
            SftpUpload.transfer("/home/hanrui/files/tempfiles/"+tempFileName+"/process2"+"/fq/",channel, is2, fileName2);

            //close the inputstream
            is1.close();
            is2.close();

            //call the algorithm
            System.out.println(i+1);
            int j=i+1;
            response.getWriter().println("<div>"+j+"</div> <br/>");
            response.getWriter().flush();

            Bwa.mem(tempFileName,fileName1,fileName2,i);
            response.getWriter().println("<div>called bwa...</div> <br/>");
            response.getWriter().flush();

            Samtools.view(tempFileName,i);
            response.getWriter().println("<div>called Samtools-view...</div> <br/>");
            response.getWriter().flush();

            Samtools.sort(tempFileName,i);
            response.getWriter().println("<div>called Samtools-sort...</div> <br/>");
            response.getWriter().flush();

            Samtools.index(tempFileName,i);
            response.getWriter().println("<div>called Samtools-index...</div> <br/>");
            response.getWriter().flush();

            Igvtools.count(tempFileName,i);
            response.getWriter().println("<div>called Igvtools...</div> <br/>");
            response.getWriter().flush();
        }
        //disconnect from channel
        SftpUpload.disconnect(channel);

        //对生成的wig文件进行处理
        for (int i = 0; i < count; i++) {
            String wigFileName = i + ".wig";
            SftpDownload.transfer("src/main/resources/"+tempFileName+"/wig/"+wigFileName, "/home/hanrui/files/tempfiles/"+tempFileName+"/process2"+"/wig/" + wigFileName);
        }

        //调用FilterSNP
        FilterSNP.filterSNP(tempFileName);
        response.getWriter().println("<div>called filterSNP...</div> <br/>");
        response.getWriter().flush();

        //调用Minima
        String path = "src/main/resources/"+tempFileName+"/csv";
        File f = new File(path);
        if(!f.exists()) {
            System.out.println(path + "not exists");
        }
        File[] fa = f.listFiles();
        for (File fs : fa) {
            Minima.minima(tempFileName,fs.getName());
            response.getWriter().println("<div>called minima...</div> <br/>");
            response.getWriter().flush();
        }

        //发送邮件
        SendEmail.sendAttachmentMail(tempFileName,mailAddress);
        response.getWriter().println("<div>the mail has been sent...</div> <br/>");
        response.getWriter().flush();

        CreateXlsx.createXlsx("src/main/resources/"+tempFileName+"/output/xlsx/");
        System.out.println("merge created");

        String[] fl = PngPath.getPngPath("src/main/resources/"+tempFileName+"/output/png");
        for (String s : fl) {
            Files.copy(Paths.get("src/main/resources/" + tempFileName + "/output/png/" + s), new FileOutputStream(
                    "src/main/webapp" + "/img/" +tempFileName+"/"+ s
            ));
            System.out.println(s+" has been copied");
        }

        //对done.jsp的操作

        request.setAttribute("paths",fl);
        //创建Excel，读取文件内容
        File file=new File("src/main/resources/"+tempFileName+"/output/xlsx/mergedExcel.xlsx");
        XSSFWorkbook workbook= null;
        try {
            workbook = new XSSFWorkbook(FileUtils.openInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet=workbook.getSheetAt(0);
        //获取sheet中最后一行行号
        int lastRowNum=sheet.getLastRowNum();
        int counter=0;
        String[] prints=new String[9999];
        for (int i=0;i<=lastRowNum;i++){
            Row row=sheet.getRow(i);
            //获取当前行最后单元格列号
            int lastCellNum=row.getLastCellNum();
            String res=null;
            for (int j=0;j<lastCellNum;j++){
                Cell cell=row.getCell(j);
                cell.setCellType(CellType.STRING);
                String value=cell.getStringCellValue();
                res=res+value+" "+"|";
            }
            res=res.replace("null","");
            prints[counter]=res;
            counter++;
        }
        request.setAttribute("prs",prints);
        request.setAttribute("counter",counter);
        String[] filename=tempFileName.split("-");
        request.setAttribute("tempFilename",filename);


        //7天后删除缓存文件
        DeleteFolderAtRegularTime.delete(tempFileName);
        ClearRemoteDir.clearRemoteTempFile(tempFileName);
        System.out.println("cache files cleared...");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("done");
        return mv;
    }

    //submit the files
    @RequestMapping("/upload3")
    public ModelAndView uploadFunc3(HttpServletRequest request, HttpServletResponse response, String mailAddress) throws Exception {

        request.setCharacterEncoding("UTF-8");

        //生成临时文件夹，用于存储运行过程中生成的各种文件
        String tempFileName = GetAddress.getAddress(mailAddress);
        CreateLocalDir.createLocalDir(tempFileName);
        request.setAttribute("tempFileName",tempFileName);
        //获取用户上传的文件，获取到的形式为Part类
        Part part1 = request.getPart("file1");
        Part part2 = request.getPart("file2");

        //获取文件名
        String fileName1 = part1.getSubmittedFileName();
        String fileName2 = part2.getSubmittedFileName();

        //获取文件
        GetFiles.getFiles(part1,part2,tempFileName);

        //提示文件已上传
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println("<div>文件已上传，请注意查收邮箱！您可以关闭此页面。</div> <br/>");
        response.getWriter().flush();

        //call the algorithm to split files
        response.getWriter().println("<div>calling ThirdSplit...</div> <br/>");
        response.getWriter().flush();
        ThirdSplit.thirdSplit(tempFileName);
        System.out.println("called ThirdSplit...");
        response.getWriter().println("<div>called ThirdSplit...</div> <br/>");
        response.getWriter().flush();
        //connect to a channel
        ChannelSftp channel = SftpUpload3.connect();

        //创建服务器上的文件夹
        CreateRemoteDir.createRemoteDir3(tempFileName);
        String fqFiles = "src/main/resources/"+tempFileName+"/fqFiles/";

        final int count = 96;
        for (int i = 0; i < count; i++) {
            //convert the files to input stream
            fileName1 = i + "_1.fq";
            fileName2 = i + "_2.fq";
            File file1 = new File(fqFiles + "fq/" + fileName1);
            File file2 = new File(fqFiles + "fq/" + fileName2);
            InputStream is1 = new FileInputStream(file1);
            InputStream is2 = new FileInputStream(file2);

            //upload files to the server
            SftpUpload3.transfer("/home/hanrui/files/tempfiles/"+tempFileName+"/process3"+"/fq/",channel, is1, fileName1);
            SftpUpload3.transfer("/home/hanrui/files/tempfiles/"+tempFileName+"/process3"+"/fq/",channel, is2, fileName2);

            //close the input stream
            is1.close();
            is2.close();

            System.out.println(i+1);
            int j=i+1;
            response.getWriter().println("<div>"+j+"</div> <br/>");
            response.getWriter().flush();
            //call the algorithm
            Bwa3.mem(tempFileName,fileName1,fileName2,i);
            response.getWriter().println("<div>calling bwa...</div> <br/>");
            response.getWriter().flush();

            Samtools3.view(tempFileName,i);
            response.getWriter().println("<div>calling Samtools-view...</div> <br/>");
            response.getWriter().flush();

            Samtools3.sort(tempFileName,i);
            response.getWriter().println("<div>calling Samtools-sort...</div> <br/>");
            response.getWriter().flush();

            Samtools3.index(tempFileName,i);
            response.getWriter().println("<div>calling Samtools-index...</div> <br/>");
            response.getWriter().flush();
        }
        //调用UmiFreq
        UmiFreq.umiFreq(tempFileName);
        response.getWriter().println("<div>called UmiFreq......</div> <br/>");
        response.getWriter().flush();

        //disconnect from channel
        SftpUpload.disconnect(channel);

        //download the .csv files from server,the downloaded files will be saved to "src/main/resources/demo/csv"
        List<String> csvLists = GetRemoteFileName.getRemoteFileName(tempFileName);
        for(String csv : csvLists){
            SftpDownload.transfer("src/main/resources/"+tempFileName+"/csv/"+csv,"/home/hanrui/files/tempfiles/"+tempFileName+"/process3"+"/csv/"+csv);
        }

        //调用Minima
        String path = "src/main/resources/"+tempFileName+"/csv";
        File f = new File(path);
        if(!f.exists()) {
            System.out.println(path + "not exists");
        }
        File[] fa = f.listFiles();
        for (File fs : fa) {
            Minima.minima(tempFileName,fs.getName());
            response.getWriter().println("<div>called Minima...</div> <br/>");
            response.getWriter().flush();
        }
        //send mail

        SendEmail.sendAttachmentMail(tempFileName,mailAddress);
        response.getWriter().println("<div>email has been sent</div> <br/>");
        response.getWriter().flush();

        //生成最终的xlsx文件
        CreateXlsx.createXlsx("src/main/resources/"+tempFileName+"/output/xlsx/");
        System.out.println("merge created");
        String[] fl = PngPath.getPngPath("src/main/resources/"+tempFileName+"/output/png");
        for (String s : fl) {
            Files.copy(Paths.get("src/main/resources/" + tempFileName + "/output/png/" + s), new FileOutputStream(
                    "src/main/webapp" + "/img/"+tempFileName+"/"+s
            ));
            System.out.println(s+"has been copied");
        }


        //对done.jsp操作
        request.setAttribute("paths",fl);
        //创建Excel，读取文件内容
        File file=new File("src/main/resources/"+tempFileName+"/output/xlsx/mergedExcel.xlsx");
        XSSFWorkbook workbook= null;
        try {
            workbook = new XSSFWorkbook(FileUtils.openInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet=workbook.getSheetAt(0);
        //获取sheet中最后一行行号
        int lastRowNum=sheet.getLastRowNum();
        int counter=0;
        String[] prints=new String[9999];
        for (int i=0;i<=lastRowNum;i++){
            Row row=sheet.getRow(i);
            //获取当前行最后单元格列号
            int lastCellNum=row.getLastCellNum();
            String res=null;
            for (int j=0;j<lastCellNum;j++){
                Cell cell=row.getCell(j);
                cell.setCellType(CellType.STRING);
                String value=cell.getStringCellValue();
                res=res+value+" "+"|";
            }
            res=res.replace("null","");
            prints[counter]=res;
            counter++;
        }
        request.setAttribute("prs",prints);
        request.setAttribute("counter",counter);
        String[] filename=tempFileName.split("-");
        request.setAttribute("tempFilename",filename);


        //7天后删除缓存文件
        DeleteFolderAtRegularTime.delete(tempFileName);
        ClearRemoteDir.clearRemoteTempFile(tempFileName);
        System.out.println("cache files cleared...");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("done");
        return mv;
    }

    //flowchart page
    @RequestMapping("/flowchart")
    public ModelAndView turnToFlowchart() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("Flowchart");
        return mv;
    }

    @RequestMapping("/flowchart1")
    public ModelAndView turnToFlowchart3() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("Flowchart3");
        return mv;
    }

    //get the json file
    @RequestMapping("/getJson")
    public ModelAndView getJson(HttpServletRequest request) throws Exception{
        request.setCharacterEncoding("UTF-8");
        String str = request.getParameter("myJson");
        OutputStream os = new FileOutputStream("src/main/resources/json/demo.json");
        PrintWriter pw = new PrintWriter(os);
        pw.write(str);
        pw.flush();
        os.close();
        pw.close();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadRequest");
        return modelAndView;
    }

    //get the json file
    @RequestMapping("/getJson3")
    public ModelAndView getJson3(HttpServletRequest request) throws Exception{
        request.setCharacterEncoding("UTF-8");
        String str = request.getParameter("myJson");
        OutputStream os = new FileOutputStream("src/main/resources/json/demo.json");
        PrintWriter pw = new PrintWriter(os);
        pw.write(str);
        pw.flush();
        os.close();
        pw.close();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("uploadRequest3");
        return modelAndView;
    }

    @RequestMapping("/result")
    public ModelAndView done(HttpServletRequest request){
        //获取tempfilename
        String fn=request.getQueryString();   //获取url参数
        int index=fn.indexOf("=")+1;
        fn=fn.substring(index);
        System.out.println("fn:"+fn);
//        获取png文件中所有图片的名字来生成路径
        String[] fl= PngPath.getPngPath("src/main/resources/"+fn+"/output/png");
        request.setAttribute("paths",fl);
//        CreateXlsx.createXlsx("src/main/resources/"+fn+"/output/xlsx/");
        System.out.println("merge created");
        //创建Excel，读取文件内容
        File file=new File("src/main/resources/"+fn+"/output/xlsx/mergedExcel.xlsx");
        XSSFWorkbook workbook= null;
        try {
            workbook = new XSSFWorkbook(FileUtils.openInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet=workbook.getSheetAt(0);
        //获取sheet中最后一行行号
        int lastRowNum=sheet.getLastRowNum();
        int counter=0;
        String[] prints=new String[9999];
        for (int i=0;i<=lastRowNum;i++){
            Row row=sheet.getRow(i);
            //获取当前行最后单元格列号
            int lastCellNum=row.getLastCellNum();
            String res=null;
            for (int j=0;j<lastCellNum;j++){
                Cell cell=row.getCell(j);
                cell.setCellType(CellType.STRING);
                String value=cell.getStringCellValue();
                res=res+value+" "+"|";
            }
            res=res.replace("null","");
            prints[counter]=res;
            counter++;
        }
        request.setAttribute("prs",prints);
        request.setAttribute("counter",counter);
        String[] filename=fn.split("-");
        request.setAttribute("tempFilename",filename);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("done");
        return mv;
    }
    @RequestMapping("/reference")
    public ModelAndView turnToReferPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("references");
        return mv;
    }
    @RequestMapping("/contactUs")
    public ModelAndView turnToContPage(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("contact");
        return mv;
    }
    @RequestMapping("/sendFeedBack")
    public void sendFeedBackMes(HttpServletResponse response,String userEmailAddr, String userFeedBack) throws IOException {
        System.out.println(userEmailAddr);
        System.out.println(userFeedBack);

        response.setContentType("text/html; charset=utf-8");
        response.getWriter().println("<script>alert('Thanks!');</script>");
        response.getWriter().flush();
    }
}
