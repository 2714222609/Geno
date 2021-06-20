package com.example.demo.Sftp;

import java.util.List;

public class GetRemoteFileName {
    public static List<String> getRemoteFileName(String remoteTempFileName) throws Exception {
        String cmd = "ls files/tempfiles/"+remoteTempFileName+"/process3"+"/csv/ -F | grep -v [/]$";
        return Remote.connect(cmd);
    }
}
