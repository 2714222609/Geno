package com.example.demo.process.process2;

import com.example.demo.Sftp.Remote;

public class Bwa {
    public static void mem(String remoteTempFileName,String filename1, String filename2, int i) throws Exception {
        String cmd = "/home/hanrui/modules/bwa-0.7.17/bwa mem -t 1 -M ~/files/Zea_mays.AGPv4.dna.chromosome.7.fa" +
                " ~/files/tempfiles/"+remoteTempFileName+"/process2"+"/fq/"+filename1+" ~/files/tempfiles/"+remoteTempFileName+
                "/process2"+"/fq/"+filename2 + " > " + "~/files/tempfiles/"+remoteTempFileName+"/process2"+"/bam/"+i+".sam";
        Remote.connect(cmd);
        System.out.println("called bwa...");
    }
}
