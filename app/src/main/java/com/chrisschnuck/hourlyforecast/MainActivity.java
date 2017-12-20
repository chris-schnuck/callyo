package com.chrisschnuck.hourlyforecast;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;

import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import org.json.JSONException;
import java.util.ArrayList;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.content.Intent;



public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private ImageView ow_icon;
    private TextView titleTextView;
    private ListView weatherListView;

    private String openWeatherMapURL = "http://api.openweathermap.org/data/2.5/forecast?q=";
    private String openWeatherApiKey = "faf6883c340fbd6dc0f03b3c454509a3";
    private String forecastUnits = "imperial";
    public WeatherDay[] FiveDayForecast;
    private double[][] highLowTemp;
    private String defaultCity = "New York";
    private String city;
    private boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherListView = (ListView) findViewById(R.id.weatherList);

        titleTextView = (TextView)findViewById(R.id.title);
        searchEditText = (EditText) findViewById(R.id.searchText);
        searchEditText.setText(defaultCity);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = Uri.encode(searchEditText.getText().toString().trim());
                if (city!="") {
                    String openWeatherURL = openWeatherMapURL + city + "&appid=" + openWeatherApiKey + "&units=" + forecastUnits;
                    success = false;
                    new GetWeather().execute(openWeatherURL);
                }
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //run with defaultCity on start
        city = defaultCity;
        String openWeatherURL = openWeatherMapURL + city + "&appid=" + openWeatherApiKey + "&units=" + forecastUnits;
        success = false;
        new GetWeather().execute(openWeatherURL);
    }

    private void checkSuccess(boolean success){
        if(success) {
            titleTextView.setText(city);
            buildWeatherList();
        }else
            Toast.makeText(this,R.string.city_not_found,Toast.LENGTH_LONG).show();
        ;
    }
    private void buildWeatherList(){
        // Construct the data source
        ArrayList<WeatherDay> weatherDays = new ArrayList<WeatherDay>();

        // Create the adapter to convert the array to views
        WeatherAdapter adapter = new WeatherAdapter(this, weatherDays);

        // Attach the adapter to a ListView
        weatherListView.setAdapter(adapter);

        //load up the listview
        for(int i=0; i < FiveDayForecast.length; i++){
            WeatherDay obj = FiveDayForecast[i];
            Log.d("weather", String.valueOf(obj.getHighTemp()));
            adapter.add(obj);
        }

        weatherListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                //String value = (String)adapter.getItemAtPosition(position);

                Log.d("item clicked", String.valueOf(position));
                WeatherDay obj = FiveDayForecast[position];
                Intent i = new Intent(MainActivity.this, WeatherDetailActivity.class);
                i.putExtra("icon", obj.getIcon());
                i.putExtra("city", city);
                i.putExtra("dateTime", obj.getDatetime());
                i.putExtra("humidity", obj.getHumidity());
                i.putExtra("highTemp", String.valueOf(obj.getHighTemp()));
                i.putExtra("lowTemp", String.valueOf(obj.getLowTemp()));
                startActivity(i);
            }
        });
    }


    private class GetWeather extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            JSONObject jsonOpenWeather = null;
            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Open Weather object: ", "> " + line);
                    try {
                        jsonOpenWeather = new JSONObject(line);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        int arraycnt = Integer.parseInt(jsonOpenWeather.getString("cnt"));
                        JSONArray list = jsonOpenWeather.getJSONArray("list");
                        Log.d("List", list.toString());
                        Log.d("cnt", String.valueOf(arraycnt));
                        FiveDayForecast = new WeatherDay[arraycnt];

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject day = list.getJSONObject(i).getJSONObject("main");
                            JSONArray weather = list.getJSONObject(i).getJSONArray("weather");
                            FiveDayForecast[i] = new WeatherDay(
                                    list.getJSONObject(i).getString("dt"),
                                    weather.getJSONObject(0).getString("icon"),
                                    weather.getJSONObject(0).getString("description"),
                                    day.getDouble("temp_max"),
                                    day.getDouble("temp_min"),
                                    day.getString("humidity"));
                        }
                        success = true;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return success;
        }


        protected void onPostExecute(Boolean result){
            checkSuccess(result);
        }
    }








}
