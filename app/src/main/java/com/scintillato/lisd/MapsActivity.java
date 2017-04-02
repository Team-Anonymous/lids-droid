package com.scintillato.lisd;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Postion> PositionArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        PositionArrayList = (ArrayList<Postion>) getIntent().getSerializableExtra(
                "test");
        for(int i=0;i<PositionArrayList.size();i++){
            Log.d("test",PositionArrayList.get(i).getLongitude().toString());
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng tmp;
        for(int i=0;i<PositionArrayList.size();i++){
            if(i==PositionArrayList.size()-1){
                tmp=new LatLng(PositionArrayList.get(i).getLatitude(),PositionArrayList.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(tmp).title(PositionArrayList.get(i).getTime().toString()).snippet(String.valueOf(PositionArrayList.get(i).getTrip_id())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp,21.0f));
            }
            else if(i==0){
                tmp=new LatLng(PositionArrayList.get(i).getLatitude(),PositionArrayList.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(tmp).title(PositionArrayList.get(i).getTime().toString()).snippet(String.valueOf(PositionArrayList.get(i).getTrip_id())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp,21.0f));
            }
            else {
                tmp = new LatLng(PositionArrayList.get(i).getLatitude(), PositionArrayList.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions().position(tmp).title(PositionArrayList.get(i).getTime().toString()).snippet(String.valueOf(PositionArrayList.get(i).getTrip_id())).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tmp, 21.0f));
            }

        }
    }
}
