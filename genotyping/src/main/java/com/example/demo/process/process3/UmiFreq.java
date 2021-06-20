package com.example.demo.process.process3;

import com.example.demo.Sftp.Remote;

public class UmiFreq {
    public static void umiFreq(String remoteTempFileName) throws Exception {
        String cmd = "/home/hanrui/modules/python3.7/bin/python3.7" +
                " ~/modules/UmiFreq/UmiFreq.py" +
                " ~/modules/UmiFreq/snp.csv" +
                " ~/files/tempfiles/"+remoteTempFileName+"/process3"+"/bam/" +
                " ~/files/tempfiles/"+remoteTempFileName+"/process3"+"/fq/" +
                " ~/files/tempfiles/"+remoteTempFileName+"/process3"+"/csv/";
        Remote.connect(cmd);
        System.out.println("called UmiFreq...");
    }
}
