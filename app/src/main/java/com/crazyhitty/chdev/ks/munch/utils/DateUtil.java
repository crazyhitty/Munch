package com.crazyhitty.chdev.ks.munch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kartik_ch on 11/8/2015.
 */
public class DateUtil {
    private String date;

    public String getCurrDate() {
        date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date());
        return date;
    }

    //converts rss publish date into a readable format
    public String getDate(String pubDate) throws ParseException {
        SimpleDateFormat pubDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = pubDateFormat.parse(pubDate);
        return new SimpleDateFormat("EEE, d MMM yyyy").format(date);
    }

    //get Date object from pub date
    public Date getDateObj(String pubDate) throws ParseException {
        SimpleDateFormat pubDateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        return pubDateFormat.parse(pubDate);
    }
}
