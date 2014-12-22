package ru.udmspell.datewidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Calendar;

public class DateWidget extends AppWidgetProvider {

    final String LOG_TAG = "myLogs";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Calendar c = Calendar.getInstance();
        int monthDay = getMonthDay(c);
        int dayOfWeek = getDayOfWeek(c);
        int month = getMonthName(c);
        int year = getYear(c);
        String monthDayName = String.valueOf(monthDay) + "тӥ ";
        String dayOfWeekName = context.getResources().getStringArray(R.array.day_names)[dayOfWeek - 1];
        String monthName = context.getResources().getStringArray(R.array.month_names)[month] + " ";
        String yearName = String.valueOf(year) + " ар";
        String textName = "";
        String summaryText = "";

        if (monthDay == 1 && month == 0) {
            summaryText = monthDayName + monthName + yearName;
            yearName = "Выль Арен!!!";
            textName = "Выль шудбурен!:)";
            monthDayName = "";
            dayOfWeekName = "";
            monthName = "";
        } else if (monthDay == 14 && month == 0) {
            summaryText = monthDayName + monthName + yearName;
            yearName = "Та нуналэ вордскиз";
            textName = "Кузебай Герд";
            monthDayName = "";
            dayOfWeekName = "";
            monthName = "";
        } else if (monthDay == 16 && month == 3) {
            summaryText = monthDayName + monthName + yearName;
            yearName = "Та нуналэ вордскиз";
            textName = "Ашальчи Оки";
            monthDayName = "";
            dayOfWeekName = "";
            monthName = "";
        } else if (monthDay == 1 && month == 11) {
            dayOfWeekName = "Нырысетӥ толалтэ нуналэн!";
        } else if (monthDay == 1 && month == 2) {
            dayOfWeekName = "Нырысетӥ тулыс нуналэн!";
        } else if (monthDay == 1 && month == 5) {
            dayOfWeekName = "Нырысетӥ гужем нуналэн!";
        } else if (monthDay == 1 && month == 8) {
            dayOfWeekName = "Нырысетӥ сӥзьыл нуналэн!";
        }

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds) + monthDayName + monthName.toUpperCase() + dayOfWeekName);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.tv_day, monthDayName);
        remoteViews.setTextViewText(R.id.tv_month, monthName.toUpperCase());
        remoteViews.setTextViewText(R.id.tv_dayofweek, dayOfWeekName);
        remoteViews.setTextViewText(R.id.tv_year, yearName);
        remoteViews.setTextViewText(R.id.tv_bold, textName);
        remoteViews.setTextViewText(R.id.summary_text, summaryText);

        //dev bd
        if (monthDay == 28 && month == 3) {
            remoteViews.setTextColor(R.id.tv_day, context.getResources().getColor(R.color.red));
        } else {
            remoteViews.setTextColor(R.id.tv_day, context.getResources().getColor(R.color.white));
        }

        //set background
        if ((month == 11 && monthDay >= 25) || (month == 0 && monthDay <= 10)) {
            remoteViews.setInt(R.id.block, "setBackgroundResource", R.drawable.nw_shary);
        } else if (month == 11 || month == 0 || month == 1) {
            remoteViews.setInt(R.id.block, "setBackgroundResource", R.drawable.nw_05);
        } else if (month >= 2  && month <= 4) { //spring
            remoteViews.setInt(R.id.block, "setBackgroundResource", R.drawable.spring01);
        } else if (month >= 5  && month <= 7) { //summer
            remoteViews.setInt(R.id.block, "setBackgroundResource", R.drawable.summer03);
        } else if (month >= 8  && month <= 10) { //autumn
            remoteViews.setInt(R.id.block, "setBackgroundResource", R.drawable.autumn03);
        }

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    private int getDayOfWeek(Calendar c) {
        int dow = c.get(Calendar.DAY_OF_WEEK);
        return dow;//day_names[dow-1];
    }

    private int getMonthName(Calendar c) {
        int month = c.get(Calendar.MONTH);
        return month;//month_names[month];
    }

    private int getMonthDay(Calendar c) {
        int monthDay = c.get(Calendar.DAY_OF_MONTH);
        return monthDay;
    }

    private int getYear(Calendar c) {
        int year = c.get(Calendar.YEAR);
        return year;
    }
}