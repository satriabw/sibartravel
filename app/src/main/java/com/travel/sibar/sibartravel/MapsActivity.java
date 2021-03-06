package com.travel.sibar.sibartravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;


public class MapsActivity extends AppCompatActivity {
    Spinner spinner;
    ArrayAdapter<CharSequence> arr;
    MapsService service;
    double lat ;
    double lon ;
    boolean set = false;


//    "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," +
//    lon  +   "&sensor=true"

    @Override
    protected void onCreate(Bundle savedInstanceState)  {

        service = new MapsService(MapsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        spinner = (Spinner) findViewById(R.id.spinner);
        arr = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr);
        final EditText text = (EditText) findViewById(R.id.editText);

        if(service.canGetLocation) {
            setLocation();

        } else {
            service.showSettingAlert();
            if(service.canGetLocation){
                setLocation();
            }
        }

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEvent.ACTION_UP == event.getAction()) {

                    Intent intent = new Intent(MapsActivity.this, ShowsMaps.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        Button search = (Button) findViewById(R.id.button);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordinates = lat+","+lon;
                String activities = spinner.getSelectedItem().toString().toLowerCase().trim();

                Intent intent = new Intent(MapsActivity.this, SearchResults.class);

                intent.putExtra("coordinates", coordinates);
                intent.putExtra("activities", activities);

                Log.d("coordinates",coordinates);
                Log.d("activities",activities);
                startActivity(intent);

            }
        });


        getReverseGeoCoding getReverseGeoCoding = new getReverseGeoCoding();
        getReverseGeoCoding.getAddress(lat,lon);
        String addr = getReverseGeoCoding.getState() +","+ getReverseGeoCoding.getCountry();
        text.setText(addr);
        if(getReverseGeoCoding.getState().length() < 1){
            text.setText(getReverseGeoCoding.getCountry());
        } else if (getReverseGeoCoding.getCountry().length() < 1 ){
            text.setText("Cannot Determine Your Location");
        }



        ImageView iv = (ImageView) findViewById(R.id.background);

        String url = "http://winsource.com/wp-content/uploads/2013/06/now-mountain.png";
        Picasso.with(getApplicationContext()).load(url).fit().into(iv);

    }

    public void setLocation(){

            lat = service.getLatitude();
            lon = service.getLongitude();
    }



    public double getLat(){
        return this.lat;
    }

    public double getLon(){
        return this.lon;
    }



}
