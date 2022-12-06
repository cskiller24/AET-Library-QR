package com.example.aet_library_qr.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class DateHelpers {
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private static DateHelpers instance;
    SimpleDateFormat simpleDateFormat;
    private DateHelpers() {
        simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    }

    /**
     * Singleton pattern for using the book
     * @return
     */
    public static DateHelpers getInstance()
    {
        if (instance == null) {
            instance = new DateHelpers();
        }
        return instance;
    }

    public String getDateToday()
    {
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public String getDateDaysFromNow(int days)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Check if date is
     */
    public boolean checkIfExpire(String borrowed,String expires)
    {
        try {
            return Objects.requireNonNull(simpleDateFormat.parse(borrowed)).after(simpleDateFormat.parse(expires));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
