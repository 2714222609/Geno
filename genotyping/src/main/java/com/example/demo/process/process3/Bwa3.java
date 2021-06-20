package com.example.demo.process.process3;

import com.example.demo.Sftp.Remote;

public class Bwa3 {
    public static void mem(String remoteTempFileName, String filename1, String filename2, int i) throws Exception {
        String cmd = "/home/hanrui/modules/bwa-0.7.17/bwa mem -t 1 -M ~/files/Zea_mays.AGPv4.dna.chromosome.6.fa" +
                " ~/files/tempfiles/"+remoteTempFileName+"/process3"+"/fq/"+filename1+" ~/files/tempfiles/"+remoteTempFileName+
                "/process3"+"/fq/"+filename2 + " > " + "~/files/tempfiles/"+remoteTempFileName+"/process3"+"/sam/"+i+".sam";
        Remote.connect(cmd);
        System.out.println("called bwa...");
    }
}