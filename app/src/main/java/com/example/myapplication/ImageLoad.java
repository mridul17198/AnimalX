package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.provider.MediaStore;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class ImageLoad extends AppCompatActivity {
    private static final int PICK_IMAGE=1;
    Button Choose,upload;
    ImageView imageView;
    Uri mimageuri ,test;
    StorageReference mstorage;
    DatabaseReference mdatabase;
    ProgressBar imProgress;
    Bitmap imageBitmap;
    int flag=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        Log.d("Inside_Image","#####");
        Choose=(Button)findViewById(R.id.choose);
        imageView=(ImageView) findViewById(R.id.imagex);
        upload=(Button)findViewById(R.id.upload);
        imProgress=(ProgressBar) findViewById(R.id.progressBar);

        mstorage= FirebaseStorage.getInstance().getReference("images");
        mdatabase=FirebaseDatabase.getInstance().getReference("images");

        Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                dispatchTakePictureIntent();
            }
        });
        Choose.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                fileChooser();
                return true;
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUpload();
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == PICK_IMAGE&& resultCode == RESULT_OK && flag==1) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            //ImageView imageView = (ImageView) findViewById(R.id.imageView);

            imageView.setImageBitmap(imageBitmap);
        }
        else if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            mimageuri=data.getData();
            Picasso.with(this).load(mimageuri).into(imageView);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent,PICK_IMAGE);
        }
    }
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
    //}

    private String getfileExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void imageUpload()
    {
        if(mimageuri==null)
        {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
            StorageReference mountainsRef = mstorage.child("camera.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(ImageLoad.this, "Failureeee", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imProgress.setProgress(0);
                        }
                    },4000);
                    Toast.makeText(ImageLoad.this, "onsuccess", Toast.LENGTH_SHORT).show();
                    upload up=new upload(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadId=mdatabase.push().getKey();
                    mdatabase.child(uploadId).setValue(up);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    imProgress.setProgress((int)progress);
                }
            });
            /*test=getImageUri(this,imageBitmap);
            StorageReference fileStorage=mstorage.child(System.currentTimeMillis()+'.'+getfileExtension(test));
            String x=System.currentTimeMillis()+'.'+getfileExtension(test);
            Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
            fileStorage.putFile(test).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imProgress.setProgress(0);
                        }
                    },4000);
                    Toast.makeText(ImageLoad.this, "onsuccess", Toast.LENGTH_SHORT).show();
                    upload up=new upload(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadId=mdatabase.push().getKey();
                    mdatabase.child(uploadId).setValue(up);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageLoad.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    imProgress.setProgress((int)progress);
                }
            });*/
        }
        else
        {
            StorageReference fileStorage=mstorage.child(System.currentTimeMillis()+'.'+getfileExtension(mimageuri));
            //String x=System.currentTimeMillis()+'.'+getfileExtension(mimageuri);
            //Toast.makeText(this, x, Toast.LENGTH_SHORT).show();
            fileStorage.putFile(mimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imProgress.setProgress(0);
                        }
                    },4000);
                    Toast.makeText(ImageLoad.this, "onsuccess", Toast.LENGTH_SHORT).show();
                    upload up=new upload(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                    String uploadId=mdatabase.push().getKey();
                    mdatabase.child(uploadId).setValue(up);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ImageLoad.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    imProgress.setProgress((int)progress);
                }
            });
        }
    }
}
