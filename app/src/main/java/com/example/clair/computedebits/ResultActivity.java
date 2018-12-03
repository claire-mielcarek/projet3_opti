package com.example.clair.computedebits;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ResultActivity extends AppCompatActivity {
    ArrayList<Integer> debits;
    int power;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compute_results);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.result_title);

        Intent intent = getIntent();
        debits = intent.getIntegerArrayListExtra("debits");
        power = intent.getIntExtra("power", 0);

        GridLayout grid = findViewById(R.id.results_layout);

        TextView title;
        TextView result;
        for(int i=0; i<5; i++){
            if(debits.get(i) != 0){
                title = new TextView(getApplicationContext());
                title.setText("Turbine " + (i+1) + " :");
                title.setTextColor(Color.BLACK);
                grid.addView(title);
                result = new TextView(getApplicationContext());
                result.setText(debits.get(i).toString() + "m³/sec");
                result.setTextColor(Color.BLACK);
                grid.addView(result);
            }
        }

        TextView powerView = findViewById(R.id.power);
        powerView.setText(this.power + "MW");


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
        Intent i = new Intent(ResultActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }
}
