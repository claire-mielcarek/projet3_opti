package com.example.clair.computedebits;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SeeActivity extends AppCompatActivity {
    ListView list;
    ArrayList<Integer> qTots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.see_title);

        list = findViewById(R.id.see_list);
        readAndInsert();
    }

    private void readAndInsert(){
        qTots= new ArrayList<>();
        AssetManager assetManager = getAssets();
        InputStream is = null;

        try {
            /*
            for (String s : assetManager.list("")){
                Log.d("[ READ_CSV ]", s);
            }
            */
            //Log.d("[ READ_CSV ]", "assets : " + assetManager.list("").toString());
            is = assetManager.open("CCD_Data.csv");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (is != null){
            //Log.d("[ READ_CSV ]", "is : " + is.toString());
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String line = "";
            StringTokenizer st = null;
            try {

                while ((line = reader.readLine()) != null) {
                    st = new StringTokenizer(line, ";");
                    //Log.d("[ READ_CSV ]", st.toString());
                    qTots.add(Integer.parseInt(st.nextToken()));
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        list.setAdapter(new ItemAdapter(this, qTots));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, 0, Menu.NONE, "menu")
                .setIcon(R.drawable.ic_menu)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent i = new Intent(SeeActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }
}
