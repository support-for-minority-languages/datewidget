package ru.udmspell.datewidget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.udmspell.datewidget.ConfigActivity;

/**
 * Created by vorgoron on 20.04.2015.
 */
public class ColorsSpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private int textResourse;
    private String[] objects;

    public ColorsSpinnerAdapter(Context context, int resource, int textResourse, String[] objects) {
        super(context, resource, textResourse, objects);
        this.context = context;
        this.resource = resource;
        this.textResourse = textResourse;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView resultView = (TextView) super.getView(position, convertView, parent);
        int textColor = ConfigActivity.getColor(position);
        resultView.setTextColor(context.getResources().getColor(textColor));
        return resultView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView resultView = (TextView) super.getView(position, convertView, parent);
        int textColor = ConfigActivity.getColor(position);
        resultView.setTextColor(context.getResources().getColor(textColor));
        return resultView;
    }
}
