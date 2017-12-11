package com.nostallinapp;

import android.content.Intent;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Keep
public class Information extends AppCompatActivity {

    EditText stall,floor_number;
    String no_of_stalls,no_of_floor;
    Button submit;
    RadioGroup rg;
    int selectedId;
    int position;
    RadioGroup rg_n;
    int selectedId_n;
    int position_n;
    boolean isPresent=false;

    int total_hits=0;
    int all_stalls=0;
    int atleast_one_stall=0;
    int no_stalls=0;
    int gn_yes =0;
    int gn_no = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        final String name_intent = getIntent().getStringExtra("Name");
        final String add_intent = getIntent().getStringExtra("Add");
        final String id_intent = getIntent().getStringExtra("Id");

        TextView name = (TextView) findViewById(R.id.place_name);
        name.setText("Selected Location : " +  name_intent + "\n");

        stall = (EditText) findViewById(R.id.spinnerstall);
        floor_number = (EditText) findViewById(R.id.floorno);
        submit = (Button) findViewById(R.id.submit1);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg_n =(RadioGroup) findViewById(R.id.radioGroup1);

        checkDb(id_intent,name_intent,add_intent,stall.getText().toString(),floor_number.getText().toString());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_of_stalls = stall.getText().toString();

                selectedId = rg.getCheckedRadioButtonId();
                View radioButton =  rg.findViewById(selectedId);
                position = rg.indexOfChild(radioButton);
                selectedId_n = rg_n.getCheckedRadioButtonId();
                View rb = rg_n.findViewById(selectedId_n);
                        position_n = rg_n.indexOfChild(rb);

                no_of_floor = floor_number.getText().toString();

                if(no_of_stalls.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Some value for no_of_stalls",Toast.LENGTH_LONG).show();
                    return;
                }

                if(no_of_floor.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Some value for no_of_floor",Toast.LENGTH_LONG).show();
                    return;

                }
                check1(id_intent, name_intent, add_intent, no_of_stalls,no_of_floor,total_hits,atleast_one_stall,all_stalls,no_stalls,gn_yes,gn_no);
      Intent after = new Intent(Information.this,AfterSubmit.class);
                startActivity(after);
                finish();
            }
        });


    }
    private void checkDb(final String id_intent, final String name_intent, final String add_intent,final String no_of_stalls,final String no_of_floor) {
        isPresent = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    if(obj.getKey().equalsIgnoreCase(id_intent)){
                        isPresent =true;
                        try {
                            total_hits = Integer.parseInt(String.valueOf(obj.child("total_hits").getValue()));
                            Log.v("InfoTest","total_hits 1 : " + total_hits);
                            atleast_one_stall = Integer.parseInt(String.valueOf(obj.child("atleast_one_stall").getValue()));
                            no_stalls = Integer.parseInt(String.valueOf(obj.child("no_stalls").getValue()));
                            all_stalls = Integer.parseInt(String.valueOf(obj.child("all_stalls").getValue()));
                            gn_yes = Integer.parseInt(String.valueOf(obj.child("gn_yes").getValue()));
                            gn_no = Integer.parseInt(String.valueOf(obj.child("gn_no").getValue()));
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
    private void check1(String id_intent, String name_intent, String add_intent, String no_of_stalls,String no_of_floor,int total_hits,int atleast_one_stall,int all_stalls,int no_stalls,int gn_yes,int gn_no) {
        if(isPresent) {
            updatedb(id_intent, name_intent, add_intent, no_of_stalls,no_of_floor,total_hits,atleast_one_stall,all_stalls,no_stalls,gn_yes,gn_no);
            finish();
        }
        else {
            InsertDb(id_intent, name_intent, add_intent, no_of_stalls,no_of_floor,total_hits,atleast_one_stall,all_stalls,no_stalls,gn_yes,gn_no);
            finish();
        }
    }
    private void updatedb(String id,String name, String add, String no_of_stalls,String no_of_floor,int total_hits, int atleast_one_stall,int all_stalls, int no_stalls, int gn_yes, int gn_no) {
        Log.v("InfoTest","total_hits 3 : " + total_hits);
        Log.v("add_test","add update:  "+add);
        if(position==0)
            atleast_one_stall=atleast_one_stall+1;
        else if(position==1)
            all_stalls=all_stalls+1;
        else if(position==2)
            no_stalls=no_stalls+1;

        if(position_n == 0)
            gn_yes=gn_yes+1;
        else if(position_n == 1)
            gn_no = gn_no + 1;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        GlobalValues gv;
            gv = new GlobalValues(id,name,add,no_of_stalls,no_of_floor,total_hits+1,atleast_one_stall,all_stalls,no_stalls,gn_yes,gn_no);
        //gv = new GlobalValues(id, name, add, no_of_stalls,no_of_floor,1,1,1,1,1,1);
            myRef.child("locations").child(id).setValue(gv);
     }
    private void InsertDb(String id,String name, String add, String no_of_stalls,String no_of_floor,int total_hits,int atleast_one_stall,int all_stalls, int no_stalls, int gn_yes, int gn_no) {
        GlobalValues gv;
        if(position==0)
            atleast_one_stall=atleast_one_stall+1;
        else if(position==1)
            all_stalls=all_stalls+1;
        else if(position==2)
            no_stalls=no_stalls+1;

        if(position_n == 0)
            gn_yes=gn_yes+1;
        else if(position_n == 1)
            gn_no = gn_no + 1;


        Log.v("InfoTest","total_hits 2 : " + total_hits);
        Log.v("add_test","add insert:  "+add);

        gv = new GlobalValues(id, name, add, no_of_stalls,no_of_floor,total_hits+1,atleast_one_stall,all_stalls,no_stalls,gn_yes,gn_no);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("locations").child(id).setValue(gv);
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}

