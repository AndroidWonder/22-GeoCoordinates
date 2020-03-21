//Go from latitude/longitude to street addresses and locations.
//The Log will contain all the street addresses found for this lat/long value.

package com.course.example.geocoordinates;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button btnSearch;
    private TextView txtAddress;
    private EditText txtLatitude;
    private EditText txtLongitude;
    private Geocoder gc = null;
    private String TAG = "SIZE";

    // locate initial coordinates at Bentley
    double latitude = 42.3889167;
    double longitude = -71.2208033;
    private LatLng position = new LatLng(latitude, longitude);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //set up geocoder
        gc = new Geocoder(this);

        txtAddress = (TextView) findViewById(R.id.myAddress);
        txtLatitude = (EditText) findViewById(R.id.txtLatitude);
        txtLongitude = (EditText) findViewById(R.id.txtLongitude);

        // locate initial coordinates at Bentley
        txtLatitude.setText(Double.toString(latitude));
        txtLongitude.setText(Double.toString(longitude));

        //get button handle and set listener
        btnSearch = (Button) findViewById(R.id.myBtnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //clear address textview
                    txtAddress.setText("");

                    // use coordinates supplied on the screen
                    latitude = Double.valueOf(txtLatitude.getText().toString());
                    longitude = Double.valueOf(txtLongitude.getText().toString());
                    position = new LatLng(latitude, longitude);

                    //clear old marker and place new marker on map
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

                    //reset center of map
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 10.0f));

                    //get and display street addresses; third parameter is max number of results wanted
                    List<Address> addresses = gc.getFromLocation(latitude, longitude, 5);
                    String st = null;

                    //loop on addresses
                    for (int j = 0; j < addresses.size(); j++) {

                        Address address = addresses.get(j);

                        //test if no address returned; -1 for no address; 0 for address
                        if (address.getMaxAddressLineIndex() == -1) {
                            Toast.makeText(getApplicationContext(), "No address returned", Toast.LENGTH_LONG).show();
                        } else {
                            String addr = address.getAddressLine(0);
                            Log.e(TAG, "Street address: " + addr);
                            Toast.makeText(getApplicationContext(), addr, Toast.LENGTH_LONG).show();
                            txtAddress.setText(addr);
                        }

                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }// onClick
        }); // btnSearch

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //reset center of map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 17.0f));

        mMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));


    }
}
