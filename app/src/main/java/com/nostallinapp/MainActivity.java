package com.nostallinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

        Button find_place;
        Button find_place1;
        private int PLACE_PICKER_CODE=1;
        int count_yes=0;
        int count_no=0;
        String Id;
        Place place1;
        boolean isPresent=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_place = (Button) findViewById(R.id.togo);
        find_place1 = (Button) findViewById(R.id.went);
        find_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(MainActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        find_place1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(MainActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PLACE_PICKER_CODE){
            if(resultCode==RESULT_OK){
                final Place place = PlacePicker.getPlace(data,this);
                Id=place.getId();
                checkdb(Id);
                place1=place;
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
                    Intent noinfo =  new Intent(MainActivity.this,InfoActivity.class);
                    Bundle args = new Bundle();
                    args.putParcelable("Latlng",place1.getLatLng());
                    noinfo.putExtra("bundle",args);
                    noinfo.putExtra("Name",place1.getName());
                    noinfo.putExtra("Id",place1.getId());
                    noinfo.putExtra("Add",place1.getAddress());
                    Log.v("add_test","From Main to Info Activity"+place1.getAddress());
                    startActivity(noinfo);
                }
                else if (isPresent){
                    //code when  info is found
                    Intent info = new Intent(MainActivity.this,InfoAvailable.class);
                    Bundle args = new Bundle();
                    args.putParcelable("Latlng",place1.getLatLng());
                    info.putExtra("bundle",args);
                    info.putExtra("Name",place1.getName());
                    info.putExtra("Id",place1.getId());
                    info.putExtra("Add",place1.getAddress());
                    Log.v("add_test","From Main to Info Availabke"+place1.getAddress());
                    startActivity(info);

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
        isPresent=false;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.about:
            Intent about = new Intent(this, aboutActivity.class);
            startActivity(about);
            return(true);
        case R.id.faq:
            Intent faq =  new Intent(this, faqactivity.class);
            startActivity(faq);
            return(true);
        case R.id.contact:
            Intent contact=  new Intent(this, contact.class);
            startActivity(contact);
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }
}
