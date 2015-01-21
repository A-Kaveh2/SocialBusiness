package ir.rasen.myapplication.helper;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import ir.rasen.myapplication.R;

/**
 * Created by Tandis on 11/29/2014.
 */
public class PersianDate {

    private int day;
    private int month;
    private int year;

    private Resources resources;


    public PersianDate(Context context) {
        this.resources = context.getResources();
    }

    public PersianDate(Context context, int day, int month, int year) throws Exception {
        this.resources = context.getResources();
        setDay(day);
        setMonth(month);
        setYear(year);
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) throws Exception {
        if (day < 1 || day > 31)
            throw new Exception("Invalid day value");
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) throws Exception {
        if (month < 1 || month > 12)
            throw new Exception("Invalid month value");
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) throws Exception {
        if (year < 1250 || year > 1393)
            throw new Exception("Invalid year value");
        this.year = year;
    }


    public static boolean validateDayBaseOnMonth(int day, int month) {
        if (month < 7) {
            if (day > 31)
                return false;
        } else if (month > 6 && month < 12) {
            if (day > 30)
                return false;
        } else if (month == 12)
            if (day > 29)
                return false;
        return true;
    }

    public String getMonthString(int month) {
        String monthString = "";
        switch (month) {
            case 1:
                resources.getString(R.string.FARVARDIN);
                break;
            case 2:
                resources.getString(R.string.ORDIBEHESHT);
                break;
            case 3:
                resources.getString(R.string.KHORDAD);
                break;
            case 4:
                resources.getString(R.string.TIR);
                break;
            case 5:
                resources.getString(R.string.MORDAD);
                break;
            case 6:
                resources.getString(R.string.SHAHRIVAR);
                break;
            case 7:
                resources.getString(R.string.MEHR);
                break;
            case 8:
                resources.getString(R.string.ABAN);
                break;
            case 9:
                resources.getString(R.string.AZAR);
                break;
            case 10:
                resources.getString(R.string.DEY);
                break;
            case 11:
                resources.getString(R.string.BAHMAN);
                break;
            case 12:
                resources.getString(R.string.ESFAND);
                break;
        }
        return monthString;
    }

    public ArrayList<String> getMonthStringList() {
        //get list of persian month strings to display in spinner
        ArrayList<String> monthStringList = new ArrayList<String>();

        monthStringList.add(resources.getString(R.string.FARVARDIN));
        monthStringList.add(resources.getString(R.string.ORDIBEHESHT));
        monthStringList.add(resources.getString(R.string.KHORDAD));
        monthStringList.add(resources.getString(R.string.TIR));
        monthStringList.add(resources.getString(R.string.MORDAD));
        monthStringList.add(resources.getString(R.string.SHAHRIVAR));
        monthStringList.add(resources.getString(R.string.MEHR));
        monthStringList.add(resources.getString(R.string.ABAN));
        monthStringList.add(resources.getString(R.string.AZAR));
        monthStringList.add(resources.getString(R.string.DEY));
        monthStringList.add(resources.getString(R.string.BAHMAN));
        monthStringList.add(resources.getString(R.string.ESFAND));

        return monthStringList;
    }
}
