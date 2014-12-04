package ru.udmspell.datewidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vorgoron on 02.12.2014.
 */
public class DateService extends Service
    {

        final String LOG_TAG = "myLogs";
        private String[] day_names = new String[] {"арнянунал",
                "вордӥськон",
                "пуксён",
                "вирнунал",
                "покчиарня",
                "удмуртарня",
                "кӧснунал"};

        private String[] month_names = new String[] {"толшор",
                "тулыспал",
                "южтолэзь",
                "оштолэзь",
                "куартолэзь",
                "инвожо",
                "пӧсьтолэзь",
                "гудырикошкон",
                "куарусён",
                "коньывуон",
                "шуркынмон",
                "толсур"};

        @Override
        public void onCreate()
        {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId)
        {
            buildUpdate();
            return super.onStartCommand(intent, flags, startId);
        }

        private void buildUpdate()
        {
            Calendar c = Calendar.getInstance();
            int monthDay = getMonthDay(c);
            int dayOfWeek = getDayOfWeek(c);
            int month = getMonthName(c);
            int year = getYear(c);
            String monthDayName = "";
            String dayOfWeekName = "";
            String monthName = "";
            String yearName = "";
            String textName = "";
            if (monthDay == 1 && month == 0) {
                yearName = "Выль Арен!!!";
                textName = "Выль шудбурен!:)";
            } else if (monthDay == 14 && month == 0) {
                yearName = "Та нуналэ вордскиз";
                textName = "Кузебай Герд";
            } else {
                monthDayName = String.valueOf(monthDay) + "-тӥ ";
                monthName = month_names[month] + " ";
                yearName = String.valueOf(year) + " ар";
                dayOfWeekName = day_names[dayOfWeek - 1];
            }

            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.tv_day, monthDayName);
            remoteViews.setTextViewText(R.id.tv_month, monthName.toUpperCase());
            remoteViews.setTextViewText(R.id.tv_dayofweek, dayOfWeekName);
            remoteViews.setTextViewText(R.id.tv_year, yearName);
            remoteViews.setTextViewText(R.id.tv_bold, textName);
//            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

            String lastUpdated = DateFormat.format("hh:mm:ss", new Date()).toString();

//            RemoteViews view = new RemoteViews(getPackageName(), R.layout.widget_layout);
//            view.setTextViewText(R.id.btn_widget, lastUpdated);

            ComponentName thisWidget = new ComponentName(this, DateWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, remoteViews);
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
