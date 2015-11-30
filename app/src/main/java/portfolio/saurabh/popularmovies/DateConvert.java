package portfolio.saurabh.popularmovies;

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
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return null;

        }
    }
}
