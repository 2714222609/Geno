package com.example.demo.process.process2;

import com.example.demo.Sftp.Remote;

public class Igvtools {
    public static void count(String remoteTempFileName,int i) throws Exception {
        String cmd = "/home/hanrui/modules/IGVTools/igvtools count --bases -w 1 " +
                "~/files/tempfiles/"+remoteTempFileName+"/process2"+"/bam/"+i+".sorted.bam " + "~/files/tempfiles/"+remoteTempFileName+
                "/process2"+"/wig/"+i+".wig" +
                " ~/files/Zea_mays.AGPv4.dna.chromosome.7.fa";
        Remote.connect(cmd);
        System.out.println("called Igvtools...");
    }
}
