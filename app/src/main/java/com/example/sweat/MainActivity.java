package com.example.sweat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static androidx.appcompat.app.ActionBar.DISPLAY_SHOW_CUSTOM;

public class MainActivity extends AppCompatActivity {
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.e("SWEAT", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.action_bar, null);
        actionBar.setCustomView(cView);

         String json = loadJSONFromAsset();
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            CardListAdapter adapter = new CardListAdapter(this, jsonArray);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
    }

    public String loadJSONFromAsset() {
        //Log.e("SWEAT", "loadJSONFromAsset");
        InputStream is = getResources().openRawResource(R.raw.trainer_programs);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        String jsonString = writer.toString();
        return jsonString;
    }
}
