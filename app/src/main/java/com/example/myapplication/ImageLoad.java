package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ImageLoad extends AppCompatActivity {
    private static final int PICK_IMAGE=1;
    Button Choose;
    ImageView imageView;
    Uri mimageuri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        Log.d("Inside_Image","#####");
        Choose=(Button)findViewById(R.id.choose);
        imageView=(ImageView) findViewById(R.id.imagex);

        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileChooser();
            }
        });
    }
    public void fileChooser()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            mimageuri=data.getData();
            Picasso.with(this).load(mimageuri).into(imageView);
        }
    }
}
