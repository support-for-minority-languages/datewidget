package ru.udmspell.datewidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import ru.udmspell.datewidget.adapters.AppSpinnerAdapter;
import ru.udmspell.datewidget.adapters.ColorsSpinnerAdapter;

public class ConfigActivity extends Activity {

    private static final int WHITE = 0;
    private static final int BLACK = 1;
    private static final int RED = 2;
    private static final int PINK = 3;
    private static final int BLUE = 4;
    private static final int GREEN = 5;
    private static final int YELLOW = 6;
    private static final int PURPLE = 7;
    private static final int GREY = 8;

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String TEXT_COLOR = "text_color_";
    public static final String SEL_APP = "sel_app_";
    private Spinner textColorSpinner;
    private Spinner appListSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate config");

        // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config_layout);

        textColorSpinner = (Spinner) findViewById(R.id.text_color_spinner);
        textColorSpinner.setAdapter(new ColorsSpinnerAdapter(this, R.layout.color_spinner_item, R.id.text,
                getResources().getStringArray(R.array.color_names)));

        appListSpinner = (Spinner) findViewById(R.id.appsSpinner);
        ArrayList<AppInfo> appInfoArrayList = getListOfInstalledApp(this);
        appListSpinner.setAdapter(new AppSpinnerAdapter(this, R.layout.app_spinner_item,
                appInfoArrayList));
    }

    public void onClick(View v) {

        int selTextColorPos = textColorSpinner.getSelectedItemPosition();
        int textColor = getColor(selTextColorPos);

        String selAppPackage = ((AppInfo) appListSpinner.getSelectedItem()).getPackageName();
        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        sp.edit().putInt(TEXT_COLOR + widgetID, getResources().getColor(textColor)).apply();
        sp.edit().putString(SEL_APP + widgetID, selAppPackage).apply();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        Intent intent = new Intent(this, DateWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {widgetID};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        Log.d(LOG_TAG, "finish config " + widgetID);
        finish();
    }

    public static int getColor(int position) {
        int textColor;
        switch (position) {
            case WHITE:
                textColor = R.color.white;
                break;
            case BLACK:
                textColor = R.color.black;
                break;
            case RED:
                textColor = R.color.red;
                break;
            case PINK:
                textColor = R.color.pink;
                break;
            case BLUE:
                textColor = R.color.blue;
                break;
            case GREEN:
                textColor = R.color.green;
                break;
            case YELLOW:
                textColor = R.color.yellow;
                break;
            case PURPLE:
                textColor = R.color.purple;
                break;
            case GREY:
                textColor = R.color.grey;
                break;
            default:
                textColor = R.color.white;
        }
        return textColor;
    }

    private static ArrayList getListOfInstalledApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List apps = packageManager.getInstalledPackages(PackageManager.SIGNATURE_MATCH);
        if (apps != null && !apps.isEmpty()) {
            ArrayList installedApps = new ArrayList();
            for (int i = 0; i < apps.size(); i++) {
                PackageInfo p = (PackageInfo) apps.get(i);
                ApplicationInfo appInfo = null;
                try {
                    appInfo = packageManager.getApplicationInfo(p.packageName, 0);
                    AppInfo app = new AppInfo();
                    app.setName(p.applicationInfo.loadLabel(packageManager).toString());
                    app.setPackageName(p.packageName);
                    app.setVersionName(p.versionName);
                    app.setVersionCode(p.versionCode);
                    app.setIcon(p.applicationInfo.loadIcon(packageManager));

                    //check if the application is not an application system
                    Intent launchIntent = app.getLaunchIntent(context);
                    if(launchIntent != null && (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                        installedApps.add(app);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            //sort the list of applications alphabetically
            if (installedApps.size() > 0) {
                Collections.sort(installedApps, new Comparator<AppInfo>() {

                    @Override
                    public int compare(final AppInfo app1, final AppInfo app2) {
                        return app1.getName().toLowerCase(Locale.getDefault()).compareTo(app2.getName().toLowerCase(Locale.getDefault()));
                    }

                });
            }

            // добавляем пустой элемент
            AppInfo app = new AppInfo();
            app.setName("Нокыӵе но ватсэт лэзёно ӧвӧл");
            app.setPackageName("");
            app.setIcon(null);
            installedApps.add(0, app);
            return installedApps;
        }
        return null;
    }
}
