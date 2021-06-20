package com.example.demo.tools.clearDir;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时删除指定文件夹 */

public class DeleteFolderAtRegularTime {

    public static void delete(String localTempFileName) throws ParseException {
        Timer t = new Timer();
        //当前毫秒
        long ts=System.currentTimeMillis();
        ts+=604800000;//7天后自动删除文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
        calendar.setTimeInMillis(ts);
        String Colck =sdf.format(calendar.getTime());
        Date d=sdf.parse(Colck);

        //设置被删除文件夹路径
        String path="src/main/resources/"+localTempFileName;
        String pngPath = "src/main/webapp/img/"+localTempFileName;
        t.schedule(new DeleteFolder(t,pngPath),d);
        t.schedule(new DeleteFolder(t,path), d);
    }

    public static void deletePrimer(String localTempFileName) throws ParseException {
        Timer t = new Timer();
        //当前毫秒
        long ts=System.currentTimeMillis();
        ts+=60480000;//7天后自动删除文件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
        calendar.setTimeInMillis(ts);
        String clock =sdf.format(calendar.getTime());
        Date d=sdf.parse(clock);

        //设置被删除文件夹路径
        String path="src/main/webapp/"+localTempFileName;
        t.schedule(new DeleteFolder(t,path), d);
    }
}

//删除文件夹的类
class DeleteFolder extends TimerTask {

    private Timer timer;
    private String path;

    public DeleteFolder(Timer t,String p) {
        this.timer = t;
        this.path=p;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        File srcFolder = new File(path);
        deleteFiles(srcFolder);
        // 最后释放资源
        timer.cancel();
    }

    //删除文件夹的方法
    private void deleteFiles(File srcFolder) {
        // TODO Auto-generated method stub
        File[] fileArrayFiles = srcFolder.listFiles();
        if (fileArrayFiles != null) {
            for (File file : fileArrayFiles) {
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    file.delete();
                }
            }
        }
        srcFolder.delete();
    }
}
