package com.example.demo.process.process3;

import com.example.demo.Sftp.Remote;

public class Samtools3 {
    public static void view(String remoteTempFileName, int i) throws Exception {
        String cmd = "/home/hanrui/modules/samtools-1.12/samtools view -Sb " + "~/files/tempfiles/"
                +remoteTempFileName+"/process3"+"/sam/"+i+".sam > " + "~/files/tempfiles/"
                +remoteTempFileName+"/process3"+"/sam/"+i+".bam";
        Remote.connect(cmd);
        System.out.println("called Samtools-view...");
    }
    public static void sort(String remoteTempFileName, int i) throws Exception {
        String cmd = "/home/hanrui/modules/samtools-1.12/samtools sort -o " + "~/files/tempfiles/"
                +remoteTempFileName+"/process3"+"/bam/"+i+".sorted.bam " + "~/files/tempfiles/"
                +remoteTempFileName+"/process3"+"/sam/"+i+".bam";
        Remote.connect(cmd);
        System.out.println("called Samtools-sort...");
    }
    public static void index(String remoteTempFileName, int i) throws Exception {
        String cmd = "/home/hanrui/modules/samtools-1.12/samtools index " + "~/files/tempfiles/"
                +remoteTempFileName+"/process3"+"/bam/"+i+".sorted.bam";
        Remote.connect(cmd);
        System.out.println("called Samtools-index...");
    }
}