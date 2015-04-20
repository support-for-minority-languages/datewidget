package ru.udmspell.datewidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.util.Calendar;

public class DateService extends Service
{
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                DateWidget.class);

        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        SharedPreferences sp = this.getSharedPreferences(
                ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = getRemoteViewsText(sp, widgetId);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private RemoteViews getRemoteViewsText(SharedPreferences sp, int widgetId) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);

        int textColor = sp.getInt(ConfigActivity.TEXT_COLOR + widgetId, getResources().getColor(R.color.white));

        Calendar c = Calendar.getInstance();
        int monthDay = getMonthDay(c);
        int dayOfWeek = getDayOfWeek(c);
        int month = getMonthName(c);
        int year = getYear(c);
        String monthDayName = String.valueOf(monthDay) + "-тӥ ";
        String dayOfWeekName = getResources().getStringArray(R.array.day_names)[dayOfWeek - 1];
        String monthName = getResources().getStringArray(R.array.month_names)[month] + " ";
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

        //dev bd
        if (monthDay == 28 && month == 3) {
            remoteViews.setTextColor(R.id.tv_day, getResources().getColor(R.color.red));
        } else {
            remoteViews.setTextColor(R.id.tv_day, getResources().getColor(R.color.white));
        }

        remoteViews.setTextViewText(R.id.tv_day, monthDayName);
        remoteViews.setTextViewText(R.id.tv_month, monthName.toUpperCase());
        remoteViews.setTextViewText(R.id.tv_dayofweek, dayOfWeekName);
        remoteViews.setTextViewText(R.id.tv_year, yearName);
        remoteViews.setTextViewText(R.id.tv_bold, textName);
        remoteViews.setTextViewText(R.id.summary_text, summaryText);

        //set text color
        remoteViews.setInt(R.id.tv_day, "setTextColor", textColor);
        remoteViews.setInt(R.id.tv_month, "setTextColor", textColor);
        remoteViews.setInt(R.id.tv_dayofweek, "setTextColor", textColor);
        remoteViews.setInt(R.id.tv_year, "setTextColor", textColor);
        remoteViews.setInt(R.id.tv_bold, "setTextColor", textColor);
        remoteViews.setInt(R.id.summary_text, "setTextColor", textColor);

        return remoteViews;
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

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
