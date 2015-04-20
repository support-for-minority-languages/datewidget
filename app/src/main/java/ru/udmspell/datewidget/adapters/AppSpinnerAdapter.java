package ru.udmspell.datewidget.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.udmspell.datewidget.AppInfo;
import ru.udmspell.datewidget.R;

/**
 * Created by vorgoron on 20.04.2015.
 */
public class AppSpinnerAdapter extends ArrayAdapter<AppInfo> {
    private Context context;
    private int resource;
    private ArrayList<AppInfo> objects;

    public AppSpinnerAdapter(Context context, int resource, ArrayList<AppInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return  getAppView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  getAppView(position, convertView, parent);
    }

    private View getAppView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.app_spinner_item, parent, false);
            holder = new ViewHolder();
            holder.appName = (TextView) convertView.findViewById(R.id.appName);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.appIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.appName.setText(objects.get(position).getName());
        holder.appIcon.setImageDrawable(objects.get(position).getIcon());
        return convertView;
    }

    static class ViewHolder {
        public ImageView appIcon;
        public TextView appName;
    }
}
