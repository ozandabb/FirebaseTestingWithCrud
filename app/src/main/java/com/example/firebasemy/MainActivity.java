package com.example.firebasemy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText txtId, txtName, txtAdd, txtConNo;
    Button btnSave, btnShow, btnUpdate, btnDelete, btnclear;
    DatabaseReference dbref;
    Student std;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtId = findViewById(R.id.editText);
        txtName = findViewById(R.id.editText2);
        txtAdd = findViewById(R.id.editText3);
        txtConNo = findViewById(R.id.editText4);

        btnSave = findViewById(R.id.saveosa);
        btnShow = findViewById(R.id.showosa);
        btnUpdate = findViewById(R.id.updateosa);
        btnDelete = findViewById(R.id.deleteosa);
        btnclear = findViewById(R.id.clearosa);

        std = new Student();

        //Data add method
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbref = FirebaseDatabase.getInstance().getReference().child("Student");
                try {
                    if(TextUtils.isEmpty(txtId.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please enter an ID : ", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(txtName.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please enter a name  : ", Toast.LENGTH_SHORT).show();
                    else if(TextUtils.isEmpty(txtAdd.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Please enter a Address : ", Toast.LENGTH_SHORT).show();
                    else{
                        std.setID(txtId.getText().toString().trim());
                        std.setName(txtName.getText().toString().trim());
                        std.setAddress(txtAdd.getText().toString().trim());
                        std.setConNo(Integer.parseInt(txtConNo.getText().toString().trim()));

                        //String userId = std.getID();
                        //dbref.push().setValue(std);
                        dbref.child(txtId.getText().toString()).setValue(std);

                        Toast.makeText(getApplicationContext(),"Data saved",Toast.LENGTH_SHORT).show();
                        clearControls();
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Invalid contact no",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Display method
        btnShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userId = txtId.getText().toString();
                DatabaseReference readdata = FirebaseDatabase.getInstance().getReference().child("Student").child(userId);
                readdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()){
                            txtId.setText(dataSnapshot.child("id").getValue().toString());
                            txtName.setText(dataSnapshot.child("name").getValue().toString());
                            txtAdd.setText(dataSnapshot.child("address").getValue().toString());
                            txtConNo.setText(dataSnapshot.child("conNo").getValue().toString());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No source to display",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        //update method
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userId = txtId.getText().toString();
                DatabaseReference updatedata = FirebaseDatabase.getInstance().getReference().child("Student");
                updatedata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(txtId.getText().toString())){
                            try {
                                std.setID(txtId.getText().toString().trim());
                                std.setName(txtName.getText().toString().trim());
                                std.setAddress(txtAdd.getText().toString().trim());
                                std.setConNo(Integer.parseInt(txtConNo.getText().toString().trim()));

                                dbref = FirebaseDatabase.getInstance().getReference().child("Student").child(txtId.getText().toString());
                                dbref.setValue(std);

                                Toast.makeText(getApplicationContext(),"data updated",Toast.LENGTH_SHORT).show();
                            }
                            catch (NumberFormatException e){
                                Toast.makeText(getApplicationContext(),"Invalid contact number", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No source to update",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //delete method
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String userId = txtId.getText().toString();
                DatabaseReference deletedata = FirebaseDatabase.getInstance().getReference().child("Student");
                deletedata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(txtId.getText().toString())){
                                dbref = FirebaseDatabase.getInstance().getReference().child("Student").child(txtId.getText().toString());
                                dbref.removeValue();

                                Toast.makeText(getApplicationContext(),"data deleted",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No source to delete",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //all clear method calling button
        btnclear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearControls();
            }
        });

    }



    private void clearControls(){
        txtId.setText("");
        txtName.setText("");
        txtAdd.setText("");
        txtConNo.setText("");
    }
}
