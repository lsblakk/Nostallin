package com.nostallinapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUp extends AppCompatActivity {
    EditText stall;
    Spinner lock;
    Button submit1;
    boolean isPresent=false;
    int count_yes;
    int count_no;
    RadioGroup rg;
    int selectedId;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up2);
        stall = (EditText) findViewById(R.id.spinnerstall);
        submit1 = (Button) findViewById(R.id.submit1);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        String[] l = new String[] {"yes","no"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, l);
        lock.setAdapter(adapter);
        TextView name = (TextView) findViewById(R.id.place_name);
        final String name_intent = getIntent().getStringExtra("Name");
        final String add_intent = getIntent().getStringExtra("add");
        final String id_intent = getIntent().getStringExtra("Id");
        name.setText("Selected Location : " +  name_intent + "\n");
        checkDb(id_intent,name_intent,add_intent,stall.getText().toString(),lock.getSelectedItem().toString());
        submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n1 =  stall.getText().toString();
                String l1 = lock.getSelectedItem().toString();
                check1(id_intent, name_intent, add_intent, n1, l1);
                    selectedId = rg.getCheckedRadioButtonId();
                    View radioButton =  rg.findViewById(selectedId);
                    position = rg.indexOfChild(radioButton);
                    Toast.makeText(getApplicationContext(),""+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkDb(final String id_intent, final String name_intent, final String add_intent, final String n1, final String l1) {
        isPresent = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("locations");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    if(obj.getKey().equalsIgnoreCase(id_intent)){
                        isPresent =true;
                        count_yes= Integer.parseInt(String.valueOf(obj.child("count_yes").getValue())) ;
                        count_no = Integer.parseInt(String.valueOf(obj.child("count_no").getValue())) ;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void check1(String id_intent, String name_intent, String add_intent, String n1, String l1) {
        if(isPresent) {
            updatedb(id_intent, name_intent, add_intent, n1, l1);
            finish();
        }
        else {
            InsertDb(id_intent, name_intent, add_intent, n1, l1);
            finish();
        }
    }
    private void updatedb(String id,String name, String add, String no_of_stalls, String locks) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
    }
    private void InsertDb(String id,String name, String add, String no_of_stalls, String locks) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
    }
}
