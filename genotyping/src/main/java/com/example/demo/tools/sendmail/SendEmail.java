package com.example.demo.tools.sendmail;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

/*用于发送邮件*/
public class SendEmail {
    private static String smtpHost = "smtp.qq.com";
    private static String userName = "2544823084@qq.com";
    private static String authKey = "abwhfjkquhsmdicb";

    public static void sendAttachmentMail(String localTempFileName, String Receiver) throws Exception {
        //收件者邮箱
        String receiver = Receiver;

        Properties properties = new Properties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.auth", "true");

        //设置ssl
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //根据配置创建会话对象，用于和邮件服务器进行交互
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //使用邮箱用户和邮箱授权码进行认证
                return new PasswordAuthentication(userName, authKey);
            }
        });

//        //开启debug模式，查看详细的发送log
//        session.setDebug(true);
        // 根据Session获取邮件传输对象
        Transport transport = session.getTransport();
        // 连接邮件服务器，使用邮箱用户和邮箱授权码进行认证
        transport.connect(smtpHost, userName, authKey);
        // 创建带图片和附件的邮件，传入session对象
        MimeMessage message = createMimeMessage(session,receiver,localTempFileName);

        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        System.out.println("the mail has been sent...");
//        System.out.println(localTempFileName);
        // 关闭连接
        transport.close();
    }


    private static MimeMessage createMimeMessage(Session session,String Receiver,String localTempFileName) throws Exception {
        // 创建邮件消息对象
        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(userName, "vip494", "UTF-8"));
        // 收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(Receiver));
        // 邮件主题
        message.setSubject("Genotyping");
        // 邮件文本内容
        MimeBodyPart contentPart = new MimeBodyPart();
        contentPart.setContent("你好，分析结果已生成，请下载附件，或点击"+"<a href="+"'"+"http://122.205.95.54:8080/result?filename="+localTempFileName+
                        "'"+
                ">查看</a>",
                "text" +
                "/html;charset=utf-8");
        // 附件资源
        MimeBodyPart appendix = new MimeBodyPart();
        //压缩附件
        Zip18.compreSsion("src/main/resources/"+localTempFileName+"/outputZip/output.zip",new File("src/main/resources/"+localTempFileName+"/output"));

        FileDataSource fileSource = new FileDataSource("src/main/resources/"+localTempFileName+"/outputZip/output.zip");
        DataHandler fileHandler = new DataHandler(fileSource);
        appendix.setDataHandler(fileHandler);
        appendix.setFileName("output.zip");
        // 封装邮件所有内容
        MimeMultipart allFile = new MimeMultipart();
        allFile.addBodyPart(contentPart);
        allFile.addBodyPart(appendix);
        allFile.setSubType("mixed");
        // 放到message消息中
        message.setContent(allFile);
        message.saveChanges(); // 保存修改
        return message;
    }
}
