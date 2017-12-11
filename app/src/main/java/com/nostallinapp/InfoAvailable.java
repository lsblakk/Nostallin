package com.nostallinapp;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Keep
public class InfoAvailable extends AppCompatActivity {

    public Button add_more_info;
    public  TextView location;
    public  TextView number_stalls;
    public  TextView number_lock;
    public TextView number_gender_neutral;
    public TextView number_floors;

    public int total_hits=0;
    public int all_stalls=0;
    public int atleast_one_stall=0;
    public int no_stalls=0;
    public int gn_yes =0;
    public int gn_no = 0;
    public int no_of_stalls=0;
    public int no_of_floor;
    public boolean isPresent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_available);


        final String name_intent = getIntent().getStringExtra("Name");
        final String add_intent = getIntent().getStringExtra("Add");
        final String id_intent = getIntent().getStringExtra("Id");


        add_more_info = (Button) findViewById(R.id.add_more_info);
        location = (TextView) findViewById(R.id.location);
        number_stalls = (TextView) findViewById(R.id.number_stalls);
        number_lock = (TextView) findViewById(R.id.number_lock);
        number_gender_neutral = (TextView) findViewById(R.id.number_gender_neutral);
        number_floors = (TextView) findViewById(R.id.number_floors);

        location.setText("Selected Location : "+ name_intent);

        checkDb(id_intent,name_intent,add_intent);

        add_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InfoAvailable.this,Information.class);
                i.putExtra("Name",name_intent);
                i.putExtra("Id",id_intent);
                i.putExtra("Add",add_intent);
                Log.v("add_test","from info available to add more information" + add_intent);
                startActivity(i);
                finish();
            }
        });
    }
    private void checkDb(final String id_intent, final String name_intent, final String add_intent) {
        isPresent = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    if(obj.getKey().equalsIgnoreCase(id_intent)){
                        Log.v("Last_Test","Is present ");

                        isPresent =true;
                        try {
                            total_hits = Integer.parseInt(String.valueOf(obj.child("total_hits").getValue()));
                            Log.v("Last_Test","Total hits: "+total_hits );
                            atleast_one_stall = Integer.parseInt(String.valueOf(obj.child("atleast_one_stall").getValue()));
                            no_stalls = Integer.parseInt(String.valueOf(obj.child("no_stalls").getValue()));
                            all_stalls = Integer.parseInt(String.valueOf(obj.child("all_stalls").getValue()));
                            gn_yes = Integer.parseInt(String.valueOf(obj.child("gn_yes").getValue()));
                            gn_no = Integer.parseInt(String.valueOf(obj.child("gn_no").getValue()));
                            no_of_stalls= Integer.parseInt(String.valueOf(obj.child("no_of_stalls").getValue()));
                            no_of_floor = Integer.parseInt(String.valueOf(obj.child("no_of_floor").getValue()));
                            number_stalls.setText(""+no_of_stalls);
                            number_floors.setText(""+no_of_floor);

                            atleast_one_stall = (atleast_one_stall*100)/total_hits;


                            all_stalls = (all_stalls*100)/total_hits;
                            no_stalls = (no_stalls*100)/total_hits;

                            gn_yes = (gn_yes*100)/total_hits;
                            gn_no =  (gn_no*100)/total_hits;

                            number_lock.setText(atleast_one_stall+"% of users say At least one stall"+"\n"+all_stalls+"% of users say all stalls"+"\n"+no_stalls+"% of users say No stalls");
                            number_gender_neutral.setText(gn_yes+"% of users say Yes"+"\n"+gn_no+"% users say No");


                        }
                        catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
