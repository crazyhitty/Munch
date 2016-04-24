package com.crazyhitty.chdev.ks.munch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kartik_ch on 11/8/2015.
 */
public class DateUtil {
    String formatRfc2822 = "EEE, dd MMM yyyy HH:mm:ss Z";
    String formatLocal = "EEE, d MMM yyyy";
    private String date;

    public String getCurrDate() {
        date = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date());
        return date;
    }

    //converts rss publish date into a readable format
    public String getDate(String pubDate) throws ParseException {
        Date date = getDateObj(pubDate);
        return new SimpleDateFormat(formatLocal).format(date);
    }

    //get Date object from pub date
    public Date getDateObj(String pubDate) throws ParseException {
        SimpleDateFormat pubDateFormat = new SimpleDateFormat(formatRfc2822, Locale.ENGLISH); //rss spec
        Date date = null;
        try {
            date = pubDateFormat.parse(pubDate);
        } catch (ParseException e) {
            pubDateFormat = new SimpleDateFormat(formatLocal); //fallback
            date = pubDateFormat.parse(pubDate);
        }
        return date;
    }
}
