package ru.udmspell.datewidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    private Spinner textColorSpinner;

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
        textColorSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spiner_item, R.id.text,
                getResources().getStringArray(R.array.color_names)));
    }


    public void onClick(View v) {

        int selTextColorPos = textColorSpinner.getSelectedItemPosition();
        int textColor;
        switch (selTextColorPos) {
            case WHITE:
                textColor = getResources().getColor(R.color.white);
                break;
            case BLACK:
                textColor = getResources().getColor(R.color.black);
                break;
            case RED:
                textColor = getResources().getColor(R.color.red);
                break;
            case PINK:
                textColor = getResources().getColor(R.color.pink);
                break;
            case BLUE:
                textColor = getResources().getColor(R.color.blue);
                break;
            case GREEN:
                textColor = getResources().getColor(R.color.green);
                break;
            case YELLOW:
                textColor = getResources().getColor(R.color.yellow);
                break;
            case PURPLE:
                textColor = getResources().getColor(R.color.purple);
                break;
            case GREY:
                textColor = getResources().getColor(R.color.grey);
                break;
            default:
                textColor = getResources().getColor(R.color.white);
        }

        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        sp.edit().putInt(TEXT_COLOR + widgetID, textColor).apply();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        Intent intent = new Intent(this, DateWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {widgetID};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
        sendBroadcast(intent);

        Log.d(LOG_TAG, "finish config " + widgetID);
        finish();
    }
}
