package com.chrisschnuck.hourlyforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class WeatherDetailActivity extends AppCompatActivity {
    private ImageView iconImageView;
    private TextView dateTimeTextView;
    private TextView humidityTextView;
    private TextView highTempTextView;
    private TextView lowTempTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);

        dateTimeTextView = (TextView)findViewById(R.id.detail_day);
        humidityTextView = (TextView)findViewById(R.id.detail_humidity);
        highTempTextView = (TextView)findViewById(R.id.detail_highTemp);
        lowTempTextView  = (TextView)findViewById(R.id.detail_lowTemp);
        iconImageView = (ImageView)findViewById(R.id.detail_icon);



        String iconName = getIntent().getStringExtra("icon");
        String url="http://openweathermap.org/img/w/" + iconName + ".png";
        Picasso.with(this).load(url).resize(200,200).into(iconImageView);

        String city = getIntent().getStringExtra("city");

        String dateTime = getIntent().getStringExtra("dateTime");
        Long epochTime = Long.parseLong(dateTime) * 1000;
        Date UTCDate = new Date(epochTime);
        String dayOfWeek = UTCDate.toString().substring(0, 3);

        String displayTime;
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(epochTime);
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour > 12){
            displayTime = String.valueOf(hour - 12) + " PM";
        }else{
            displayTime = String.valueOf(hour) + " AM";
        }

        dateTimeTextView.setText(city + " weather for " + dayOfWeek + " " + displayTime);

        String humidity = getIntent().getStringExtra("humidity");
        humidityTextView.setText("Humidity:" + humidity);

        String highTemp= getIntent().getStringExtra("highTemp");
        highTempTextView.setText("High: " + highTemp);

        String lowTemp= getIntent().getStringExtra("lowTemp");
        lowTempTextView.setText("Low: " + lowTemp);





    }
}
