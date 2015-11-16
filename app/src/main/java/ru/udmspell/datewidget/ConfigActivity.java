package ru.udmspell.datewidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import ru.udmspell.datewidget.adapters.ColorsSpinnerAdapter;

public class ConfigActivity extends Activity implements View.OnClickListener {

    private static final int WHITE = 0;
    private static final int BLACK = 1;
    private static final int RED = 2;
    private static final int PINK = 3;
    private static final int BLUE = 4;
    private static final int GREEN = 5;
    private static final int YELLOW = 6;
    private static final int PURPLE = 7;
    private static final int GREY = 8;
    final static long CALENDAR_COLOR_RED = 0xF44336;
    final static long CALENDAR_COLOR_YELLOW = 0xFFEB3B;

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String TEXT_COLOR = "text_color_";
    public static final String BACKGROUND = "background_";
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
        findViewById(R.id.ok_button).setOnClickListener(this);
        findViewById(R.id.udmurt_meetings).setOnClickListener(this);
        findViewById(R.id.udmurt_holidays).setOnClickListener(this);
        
        textColorSpinner = (Spinner) findViewById(R.id.text_color_spinner);
        textColorSpinner.setAdapter(new ColorsSpinnerAdapter(this, R.layout.color_spinner_item, R.id.text,
                getResources().getStringArray(R.array.color_names)));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_button:
                onOkButton();
                break;
            case R.id.udmurt_meetings:
                AndroidCalendar.addCalendar(this, getString(R.string.meetings), getString(R.string.meetings_account), CALENDAR_COLOR_RED);
                break;
            case R.id.udmurt_holidays:
                AndroidCalendar.addCalendar(this, getString(R.string.holidays), getString(R.string.holidays_account), CALENDAR_COLOR_YELLOW);
                break;
        }
    }

    private void onOkButton() {
        int selTextColorPos = textColorSpinner.getSelectedItemPosition();
        int textColor = getColor(selTextColorPos);

        boolean backgroundShow = ((CheckBox) findViewById(R.id.show_background)).isChecked();

        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        sp.edit().putInt(TEXT_COLOR + widgetID, getResources().getColor(textColor)).apply();
        sp.edit().putBoolean(BACKGROUND + widgetID, backgroundShow).apply();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        Intent intent = new Intent(this, DateWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {widgetID};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);

        finish();
    }
}
