package com.tylernorbury.albumrater.database.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeConverter {
    @TypeConverter
    public static GregorianCalendar toDate(Long timestamp) {
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(timestamp);
        return timestamp == null ? null : date;
    }

    @TypeConverter
    public static Long toTimestamp(GregorianCalendar date) {
        return date == null ? null : date.getTimeInMillis();
    }
}
