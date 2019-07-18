package com.example.drashtimuni.hydrologicaldatamanagement;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Drashti Pathak
 * date :  15-May-2019
 */
public class MainFeaturesOfApp extends AppCompatActivity {
    Button weatherButton;
    TextView modelDescriptionTextView;
    Spinner modelNameSpinner;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_features_of_app);

        final String mriCgcmHtmlText = "<a href=\"https://www.jstage.jst.go.jp/article/jmsj/90A/0/90A_2012-A02/_article\">MRI-CGCM3</a> model is developed by Meteorological Research Institute, Japan";
        final String bccCsmHtmlText = "<a href=\"http://forecast.bcccsm.ncc-cma.net/web/channel-43.htm\">BCC-CSM1-1</a> model is developed by Beijing Climate Center, China Meteorological Administration";
        final String ccsmHtmlText = "<a href=\"https://en.wikipedia.org/wiki/Community_Climate_System_Model\">CCSM4</a> model is developed by National Center of Atmospheric Research, USA";

        modelDescriptionTextView = findViewById(R.id.model_description_text_view);

        modelNameSpinner = findViewById(R.id.spinnerModelName);
        modelNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    modelDescriptionTextView.setText( Html.fromHtml(mriCgcmHtmlText));
                } else if(position == 1) {
                    modelDescriptionTextView.setText( Html.fromHtml(ccsmHtmlText));
                } else if(position == 2) {
                    modelDescriptionTextView.setText( Html.fromHtml(bccCsmHtmlText));
                }
                modelDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                modelDescriptionTextView.setText( Html.fromHtml(mriCgcmHtmlText));
                modelDescriptionTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        });

        weatherButton = findViewById(R.id.precipitation_button);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //3,1/31/51 0:00,40.5,285.25,0.5423261
                try {

                    TextView addressTextView = findViewById(R.id.editTextAddress);
                    String address = addressTextView.getText().toString();
                    Geocoder geocoder = new Geocoder(context);
                    List<Address> addresses = geocoder.getFromLocationName(address,1);
                    Address geoAddress = addresses.get(0);
                    String latitude = Double.toString(geoAddress.getLatitude());
                    String longitude = Double.toString(360 + geoAddress.getLongitude());
                    Log.d("Geo location","Retreived information from address Latitude : [ " + latitude + " ] Longitude : [ " + longitude + " ]");

                    //Date date = new SimpleDateFormat("M/dd/yy H:mm").parse("1/31/51 0:00");
                    TextView dateTextView = findViewById(R.id.editTextDate);
                    Date date = new SimpleDateFormat("MM/dd/yyyy").parse(dateTextView.getText().toString());

                    Spinner modelNameSpinner = findViewById(R.id.spinnerModelName);
                    String modelName = modelNameSpinner.getSelectedItem().toString();

                    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                    databaseAccess.open();

                    String precipitationData = databaseAccess.getPrecipitationData(modelName, latitude, longitude, date);
                    TextView precipitationTextView = findViewById(R.id.precipitation_data_text_view);
                    if("No data".equalsIgnoreCase(precipitationData)) {
                        precipitationTextView.setText("No data");
                    } else {
                        precipitationTextView.setText(precipitationData + " inches");
                    }

                    String humidityData = databaseAccess.getHumidityData(modelName, latitude, longitude, date);
                    TextView humidityTextView = findViewById(R.id.humidity_data_text_box);
                    if("No data".equalsIgnoreCase(humidityData)) {
                        humidityTextView.setText("No data");
                    } else {
                        humidityTextView.setText(humidityData + "%");
                    }

                    Spinner temperatureUnitSpinner = findViewById(R.id.spinnerTemperatureUnits);
                    String temperatureUnit = temperatureUnitSpinner.getSelectedItem().toString();

                    String minimumTemperatureData = databaseAccess.getMinimumTemperatureData(modelName, latitude, longitude, date, temperatureUnit);
                    TextView minimumTemperatureTextView = findViewById(R.id.min_temperature_text_box);
                    if("No data".equalsIgnoreCase(minimumTemperatureData)) {
                        minimumTemperatureTextView.setText("No data");
                    } else {
                        if ("fahrenheit".equalsIgnoreCase(temperatureUnit)) {
                            minimumTemperatureTextView.setText(minimumTemperatureData + " °F");
                        } else {
                            minimumTemperatureTextView.setText(minimumTemperatureData + " °C");
                        }
                    }

                    String maximumTemperatureData = databaseAccess.getMaximumTemperatureData(modelName, latitude, longitude, date, temperatureUnit);
                    TextView maximumTemperatureTextView = findViewById(R.id.max_temperature_text_box);
                    if("No data".equalsIgnoreCase(maximumTemperatureData)) {
                        maximumTemperatureTextView.setText("No data");
                    } else {
                        if ("fahrenheit".equalsIgnoreCase(temperatureUnit)) {
                            maximumTemperatureTextView.setText(maximumTemperatureData + " °F");
                        } else {
                            maximumTemperatureTextView.setText(maximumTemperatureData + " °C");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
