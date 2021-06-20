package com.example.demo.tools.sendmail;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetAddress {
    public static String getAddress(String mailAddress) {
        long ts = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date(ts);
        String str = mailAddress.substring(0,mailAddress.indexOf("@"));
        return str+"-"+formatter.format(date);
    }
}
