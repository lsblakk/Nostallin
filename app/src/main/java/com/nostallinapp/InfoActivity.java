package com.nostallinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class InfoActivity extends AppCompatActivity {

    Button find_another;
    Button check_out;
    private int PLACE_PICKER_CODE=1;
    int count_yes=0;
    int count_no=0;
    String Id;
    private LatLng Latlng;
    private String Name;
    private String Id_bundle;
    private String add;
    Place place1;
    boolean isPresent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        find_another = (Button) findViewById(R.id.find_another);
        check_out = (Button) findViewById(R.id.check_out);

        Bundle bundle = getIntent().getParcelableExtra("bundle");
        Latlng = bundle.getParcelable("Latlng");
        add = getIntent().getStringExtra("Add");
        Id_bundle = getIntent().getStringExtra("Id");
        Name = getIntent().getStringExtra("Name");


        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoActivity.this, MapsActivity.class);
                Bundle args = new Bundle();
                args.putParcelable("Latlng",Latlng);
                i.putExtra("bundle",args);
                i.putExtra("Name",Name);
                i.putExtra("Id",Id_bundle);
                i.putExtra("Add",add);
                startActivity(i);
                finish();
            }
        });

        find_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(InfoActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_CODE) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, this);
                Id = place.getId();
                place1 = place;
                checkdb(Id);
            }
        }
    }
    public  void checkdb(final String id) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("locations");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Log.v("Newtest",obj.getKey());
                    if(obj.getKey().equalsIgnoreCase(id)){
                        Log.v("Newtest","Inside found");
                        isPresent = true;
                    }
                }

                Log.v("Newtest","yes:"+count_yes);
                Log.v("Newtest","no: "+count_no);
                Log.v("Newtest","is present "+ isPresent);

                if(!isPresent){
                    Intent noinfo =  new Intent(InfoActivity.this,InfoActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("Latlng",place1.getLatLng());
                    noinfo.putExtra("bundle",args);
                    noinfo.putExtra("Name",place1.getName());
                    noinfo.putExtra("Id",place1.getId());
                    noinfo.putExtra("Add",place1.getAddress());
                    Log.v("add_test","From Main to Info Activity"+place1.getAddress());
                    startActivity(noinfo);
                    finish();
                }
                else if (isPresent){
                    //code when  info is found
                    Intent info = new Intent(InfoActivity.this,InfoAvailable.class);
                    Bundle args = new Bundle();
                    args.putParcelable("Latlng",place1.getLatLng());
                    info.putExtra("bundle",args);
                    info.putExtra("Name",place1.getName());
                    info.putExtra("Id",place1.getId());
                    info.putExtra("Add",place1.getAddress());
                    Log.v("add_test","From Main to Info Availabke"+place1.getAddress());
                    startActivity(info);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    protected void onResume(){
        super.onResume();
        count_yes=0;
        count_no=0;

    }

}
