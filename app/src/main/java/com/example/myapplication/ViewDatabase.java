package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class ViewDatabase extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference Databaselogin;
    private String id;
    private ListView mlistview;
    ArrayList<String> array=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.view_database_layout);
        super.onCreate(savedInstanceState);
        mlistview=(ListView)findViewById(R.id.ListView);
        database=FirebaseDatabase.getInstance();
        Databaselogin=database.getReference("Login");
        Databaselogin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ShowData","#####");
                Showdata(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Update","####");
                UpdateData();
            }
        });
    }
    public void Showdata(DataSnapshot dataSnapshot)
    {
        int i=0;
        Log.d("Inside_Show","######");
        Toast.makeText(this, "Inside Toast", Toast.LENGTH_SHORT).show();
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {
            i++;
            Toast.makeText(this, "Number"+i, Toast.LENGTH_SHORT).show();
            UserInformation ui=new UserInformation();
            ui.setId(ds.getValue(UserInformation.class).getId());
            ui.setName(ds.getValue(UserInformation.class).getName());
            ui.setPass(ds.getValue(UserInformation.class).getPass());
            array.add(ui.getName());
            ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mlistview.setAdapter(adapter);

        }
    }
    public void UpdateData()
    {
        database=FirebaseDatabase.getInstance();
        Databaselogin=database.getReference("Login");
        Databaselogin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                   Log.d("Inside_Data","#######");
                    String id = ds.getKey();
                    Toast.makeText(ViewDatabase.this, id, Toast.LENGTH_SHORT).show();
                    dataSnapshot.getRef().child(id).child("name").setValue("Updated_Value");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
