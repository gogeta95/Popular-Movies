package portfolio.saurabh.popularmovies.util;

import java.util.Calendar;
import java.util.Date;


public class DateConvert {
    public static String convert(Date date) {
        Calendar cal= Calendar.getInstance();
        cal.setTime(date);
        return monthToName(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR);
    }

    public static String monthToName(int id) {
        switch (id) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return null;

        }
    }
}
