package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebViewDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText EditName,EditPass;
    Button Btn,re;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference Databaselogin=database.getReference("Login");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditName=(EditText) findViewById(R.id.Name);
        EditPass=(EditText) findViewById(R.id.Password);
        Btn=(Button) findViewById(R.id.Submit);
        re=(Button) findViewById(R.id.ReadData);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=EditName.getText().toString().trim();
                String password=EditPass.getText().toString().trim();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(MainActivity.this, "Enter The Fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    long pass=Long.parseLong(password);
                    String id=Databaselogin.push().getKey();

                    Login l1=new Login(id,name,pass);
                    Databaselogin.child(id).setValue(l1);
                    Toast.makeText(MainActivity.this, "Datasaved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, ViewDatabase.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Onstart","#####");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onstop","#####");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ondestroy","#####");
    }
}
