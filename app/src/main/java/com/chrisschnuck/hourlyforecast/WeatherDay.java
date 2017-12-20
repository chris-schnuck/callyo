package com.chrisschnuck.hourlyforecast;

/**
 * Created by ChrisSchnuck on 12/18/17.
 */

public class WeatherDay {
    private String mDatetime;
    private String mIcon;
    private String mDescription;
    private double mHighTemp;
    private double mLowTemp;
    private String mHumidity;

    public WeatherDay(String datetime, String icon, String description, double highTemp, double lowTemp, String humidity){
        mDatetime = datetime;
        mIcon = icon;
        mDescription = description;
        mHighTemp = highTemp;
        mLowTemp = lowTemp;
        mHumidity = humidity;
    }

    public String getDatetime() {
        return mDatetime;
    }

    public void setDatetime(String datetime) {
        mDatetime = datetime;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getHighTemp() {
        return mHighTemp;
    }

    public void setHighTemp(double highTemp) {
        mHighTemp = highTemp;
    }

    public double getLowTemp() {
        return mLowTemp;
    }

    public void setLowTemp(double lowTemp) {
        mLowTemp = lowTemp;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }
}
