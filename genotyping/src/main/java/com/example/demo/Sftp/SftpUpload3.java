package com.example.demo.Sftp;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/*用于上传文件到服务器*/
public class SftpUpload3 {

    public static ChannelSftp connect() throws JSchException {
        Remote remote = new Remote();
        JSch jSch = new JSch();
        //建立一个Linux session
        Session session = jSch.getSession(remote.user, remote.host, remote.port);
        session.setPassword(remote.password);
        //关闭key的检验
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        //连接Linux
        session.connect();
        //通过sftp的方式连接
        ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();
        return channel;
    }

    public static void transfer(String fqDir, ChannelSftp channel, InputStream is, String fileName) throws SftpException, JSchException, IOException {
        //upload file stream
        channel.put(is, fqDir + fileName);
        is.close();
    }

    public static void disconnect(ChannelSftp channel) {
        channel.disconnect();
    }

}
