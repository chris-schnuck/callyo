package com.chrisschnuck.hourlyforecast;

/**
 * Created by ChrisSchnuck on 12/20/17.
 */
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

public class WeatherAdapter extends ArrayAdapter<WeatherDay> {
    // View lookup cache
    private static class ViewHolder {
        ImageView icon;
        TextView day;
        TextView time;
        TextView description;
        TextView highLow;
    }

    public WeatherAdapter(Context context, ArrayList<WeatherDay> weatherDays) {
        super(context, R.layout.weather_row, weatherDays);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherDay day = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        WeatherAdapter.ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new WeatherAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.weather_row, parent, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.day = (TextView) convertView.findViewById(R.id.day);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.highLow = (TextView) convertView.findViewById(R.id.highlow);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (WeatherAdapter.ViewHolder) convertView.getTag();
        }
        String url="http://openweathermap.org/img/w/" + day.getIcon() + ".png";
        Picasso.with(getContext()).load(url).resize(200,200).into(viewHolder.icon);

        Long epochTime = Long.parseLong(day.getDatetime()) * 1000;
        Date UTCDate = new Date(epochTime);
        String dayOfWeek = UTCDate.toString().substring(0, 3);
        viewHolder.day.setText(dayOfWeek);

        String displayTime;
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(epochTime);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour > 12){
           displayTime = String.valueOf(hour - 12) + " PM";
        }else{
           displayTime = String.valueOf(hour) + " AM";
        }

        viewHolder.time.setText(displayTime);
        viewHolder.description.setText(day.getDescription());

        viewHolder.highLow.setText("H:" + String.valueOf(day.getHighTemp()) + " L:" + String.valueOf(day.getLowTemp()));


        return convertView;
    }

}
