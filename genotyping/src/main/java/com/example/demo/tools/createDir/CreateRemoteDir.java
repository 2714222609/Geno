package com.example.demo.tools.createDir;

import com.example.demo.Sftp.Remote;
import org.junit.Test;

public class CreateRemoteDir {
    public static void createRemoteDir2(String remoteTempFileName) throws Exception {
        String cmd = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName;
        String cmd1 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process2";
        String cmd2 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process2"+"/wig";
        String cmd3 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process2"+"/fq";
        String cmd4 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process2"+"/bam";
        Remote.connect(cmd);
        Remote.connect(cmd1);
        Remote.connect(cmd2);
        Remote.connect(cmd3);
        Remote.connect(cmd4);
    }
    public static void createRemoteDir3(String remoteTempFileName) throws Exception {
        String cmd = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName;
        String cmd1 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process3";
        String cmd2 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process3"+"/fq";
        String cmd3 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process3"+"/sam";
        String cmd4 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process3"+"/csv";
        String cmd5 = "mkdir /home/hanrui/files/tempfiles/"+remoteTempFileName+"/process3"+"/bam";
        Remote.connect(cmd);
        Remote.connect(cmd1);
        Remote.connect(cmd2);
        Remote.connect(cmd3);
        Remote.connect(cmd4);
        Remote.connect(cmd5);
    }
}
