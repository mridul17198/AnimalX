package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LocationFind extends AppCompatActivity {

    ArrayList<String> listSpinner = new ArrayList<String>();
    // to store the city and state in the format : City , State. Eg: New Delhi , India
    ArrayList<String> listAll = new ArrayList<String>();
    // for listing all states
    ArrayList<String> listState = new ArrayList<String>();
    // for listing all cities
    ArrayList<String> listCity = new ArrayList<String>();
    // access all auto complete text views
    AutoCompleteTextView act;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        callAll();
    }

    public void callAll() {
        obj_list();
        addToSpinner();
        addToAll();
        addCity();
        addState();
    }

    public String getJson() {
        String json = null;
        try {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list() {
        // Exceptions are returned by JSONObject when the object cannot be created
        try {
            // Convert the string returned to a JSON object
            JSONObject jsonObject = new JSONObject(getJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for (int i = 0; i < array.length(); i++) {
                // select the particular JSON data
                JSONObject object = array.getJSONObject(i);
                String city = object.getString("name");
                String state = object.getString("state");
                // add to the lists in the specified format
                listSpinner.add(city + " , " + state);
                listAll.add(city + " , " + state);
                listCity.add(city);
                listState.add(state);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void addToSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spCity);
        // Adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // The first auto complete text view
    void addToAll() {
        act = (AutoCompleteTextView) findViewById(R.id.actAll);
        adapterSetting(listAll);
    }

    // The second auto complete text view
    void addCity() {
        act = (AutoCompleteTextView) findViewById(R.id.actCity);
        adapterSetting(listCity);
    }

    // The third auto complete text view
    void addState() {
        Set<String> set = new HashSet<String>(listState);
        act = (AutoCompleteTextView) findViewById(R.id.actState);
        adapterSetting(new ArrayList(set));
    }

    void adapterSetting(ArrayList arrayList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        act.setAdapter(adapter);
        hideKeyBoard();
    }

    public void hideKeyBoard() {
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
}
