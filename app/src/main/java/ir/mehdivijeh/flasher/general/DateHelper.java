package ir.mehdivijeh.flasher.general;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {


    public static String getCurrentDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        Date date = new Date();
        return format.format(date);
    }

    public static String getCurrentDateInMilli() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.UK);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Tehran"));
        Date date = new Date();
        return format.format(date);
    }


}
