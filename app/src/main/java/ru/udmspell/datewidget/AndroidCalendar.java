package ru.udmspell.datewidget;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Patterns;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class AndroidCalendar {

    private static final String ACCOUNT_TYPE = "com.google";
    private static final String[] CALENDAR_PROJECTION = new String[]{
            CalendarContract.Calendars._ID
    };;

    private static Uri buildPubCalUri(String accountName) {
        return CalendarContract.Calendars.CONTENT_URI
                .buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE)
                .build();
    }

    private static void addPublicCalendar(Context context, String accountName, String name, String ownerAccount, long calendarColor) {
        if (isCalendarExist(context, accountName, ownerAccount)) {
            Toast.makeText(context, name + context.getString(R.string.calendar_was_add), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver cr = context.getContentResolver();
        final ContentValues cv = buildPublicCalendarValues(accountName, name, ownerAccount, calendarColor);
        Uri calUri = buildPubCalUri(accountName);
        cr.insert(calUri, cv);
        Toast.makeText(context, name + context.getString(R.string.calend_added), Toast.LENGTH_SHORT).show();
    }

    private static ContentValues buildPublicCalendarValues(String accountName, String name, String ownerAccount, long calendarColor) {
        final ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, accountName);
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, ACCOUNT_TYPE);
        cv.put(CalendarContract.Calendars.NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, name);
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, calendarColor);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_READ);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, ownerAccount);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        cv.put(CalendarContract.Calendars.CAL_SYNC1, ownerAccount);
        return cv;
    }

    public static void addCalendar(final Context context, final String name, final String ownerAccount, final long calendarColor) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType(ACCOUNT_TYPE);
        final List<String> possibleEmails = new LinkedList<>();

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmails.add(account.name);
            }
        }

        if (possibleEmails.size() == 0) {
            Toast.makeText(context, R.string.no_accounts, Toast.LENGTH_SHORT).show();
        } else if (possibleEmails.size() == 1) {
            addPublicCalendar(context, possibleEmails.get(0), name, ownerAccount, calendarColor);
        } else {
            final String[] items = new String[possibleEmails.size()];
            for (int i = 0; i < possibleEmails.size(); i++) {
                items[i] = possibleEmails.get(i);
            }
            AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.choose_account)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addPublicCalendar(context, possibleEmails.get(which), name, ownerAccount, calendarColor);
                        }
                    });
            dialog.create().show();
        }
    }

    private static Boolean isCalendarExist(Context context, String accountName, String ownerAccount) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = String.format("((%s = ?) AND (%s = ?))", CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.Calendars.OWNER_ACCOUNT);

        String[] selectionArgs = new String[]{accountName, ownerAccount};

        Cursor cursor = cr.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, null);
        try {
            if (cursor.moveToNext()) {
                return true;
            }
        } finally {
            cursor.close();
        }
        return false;
    }
}
