package com.example.demo.tools.clearDir;

import com.example.demo.Sftp.Remote;

public class ClearRemoteDir {
    //clear remote dir
    public static void clearRemoteTempFile(String remoteTempFileName) throws Exception {
        String cmd = "rm -rf ~/files/tempfiles/"+remoteTempFileName;
        Remote.connect(cmd);
    }
}
