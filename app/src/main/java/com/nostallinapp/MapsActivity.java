
package com.nostallinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private LatLng Latlng;
    private String Name;
    private String Id;
    private String add;
    private String markerText;
    int count_yes;
    int count_no;
    int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        Latlng = bundle.getParcelable("Latlng");
        add = getIntent().getStringExtra("Add");
        Id = getIntent().getStringExtra("Id");
        Name = getIntent().getStringExtra("Name");
        checkdb(Id);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }
    public  void checkdb(final String id) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference().child("locations");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot obj : dataSnapshot.getChildren()){
                        Log.v("NNN",obj.getKey());
                        if(obj.getKey().equalsIgnoreCase(Id)){

                        }
                    }

                    if(count_yes>=3){
                        check = 0;
                        markerText="Majority of the users have confirmed that there is atleast one stall with lock.";
                        setMarker(markerText);
                    }
                    else if(count_no>=3) {
                        check = 0;
                        markerText="Majority of the users have confirmed that there are no stall/s with lock.";
                        setMarker(markerText);
                       }
                    else
                    {
                        check = 1;
                        markerText= count_yes + " users confirmed that there is atleast one stall with lock" + "\n" + count_no + " users have confirmed that there are no stalls with locks." + "\n" + "Click here to add your vote!!!!!!!";
                        setMarker(markerText);
                        mMap.setOnInfoWindowClickListener(MapsActivity.this);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }
    private void setMarker(final String markerText) {
        Marker m = null;

        try {
            m = mMap.addMarker(new MarkerOptions().position(Latlng));
        }
        catch(NullPointerException e){
            Toast.makeText(this, "Location not selected properly. Please select again", Toast.LENGTH_SHORT).show();
            finish();
            }
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                TextView status= (TextView) v.findViewById(R.id.data);
                status.setText("Tap here to add data!!");
                status.setTextSize(25);
                return v;
            }
        });
        m.showInfoWindow();
    }
    @Override
    protected void onStart(){
        super.onStart();
       }
    @Override
    protected void onResume(){
        super.onResume();
        checkdb(Id);
    }
    @Override
    public void onBackPressed(){
        return;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latlng.latitude,Latlng.longitude), 16f), 4000, null);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        if(check==1) {
            Intent i = new Intent(this, Information.class);
            i.putExtra("Name", Name);
            i.putExtra("Id", Id);
            i.putExtra("Add", add);
            startActivity(i);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.reset:
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();

    }
        return(super.onOptionsItemSelected(item));
    }
}
