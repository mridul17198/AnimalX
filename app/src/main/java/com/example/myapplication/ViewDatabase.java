package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
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

public class ViewDatabase extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference Databaselogin;
    private String id;
    private ListView mlistview;
    ArrayList<String> array=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_database_layout);
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
    }
    public void Showdata(DataSnapshot dataSnapshot)
    {
        int i=0;
        Toast.makeText(this, "Inside Toast", Toast.LENGTH_SHORT).show();
        for(DataSnapshot ds:dataSnapshot.getChildren())
        {
            i++;
            Toast.makeText(this, "Number"+i, Toast.LENGTH_SHORT).show();
            UserInformation ui=new UserInformation();
            ui.setName(ds.getValue(UserInformation.class).getName());
            ui.setPass(ds.getValue(UserInformation.class).getPass());
            array.add(ui.getName());
            ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mlistview.setAdapter(adapter);

        }
    }
}
