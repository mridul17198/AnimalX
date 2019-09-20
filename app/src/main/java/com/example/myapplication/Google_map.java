package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Google_map extends FragmentActivity implements OnMapReadyCallback {

    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static int REQUEST_CODE=101;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_map);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    private void fetchLastLocation()
    {
        Toast.makeText(this, "FetchLastLocation", Toast.LENGTH_SHORT).show();
        Task<Location> task=fusedLocationProviderClient.getLastLocation();
        //Toast.makeText(this, task, Toast.LENGTH_SHORT).show();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override

            public void onSuccess(Location location) {
                Log.d("Location_Got","###########");
                if(location!=null)
                {
                    currentlocation=location;
                    Toast.makeText(Google_map.this, currentlocation.getLatitude()+""+currentlocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(Google_map.this);
                }
                else
                {
                    Toast.makeText(Google_map.this, "FetchLastLocation", Toast.LENGTH_SHORT).show();
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Google_map.this, "Map_Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Toast.makeText(this, "FetchLastLocation", Toast.LENGTH_SHORT).show();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();

        LatLng latLng=new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("I am Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,200));
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 101:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    fetchLastLocation();
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }

    }
}
