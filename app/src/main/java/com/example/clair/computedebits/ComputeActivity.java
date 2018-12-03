package com.example.clair.computedebits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ComputeActivity extends AppCompatActivity {
    EditText qtotView;
    EditText upElevationView;
    ArrayList<EditText> debitViews;
    ArrayList<CheckBox> turbineCheckBoxes;
    ArrayList<Integer> debitMaxs;
    Button button;
    final Context context = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compute);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.compute_title);

        qtotView = findViewById(R.id.debitMax);
        upElevationView = findViewById(R.id.upElevation);

        debitViews = new ArrayList<>();
        debitViews.add((EditText) findViewById(R.id.turbine1_debit));
        debitViews.add((EditText) findViewById(R.id.turbine2_debit));
        debitViews.add((EditText) findViewById(R.id.turbine3_debit));
        debitViews.add((EditText) findViewById(R.id.turbine4_debit));
        debitViews.add((EditText) findViewById(R.id.turbine5_debit));

        turbineCheckBoxes = new ArrayList<>();
        turbineCheckBoxes.add((CheckBox) findViewById(R.id.turbine1_active));
        turbineCheckBoxes.add((CheckBox) findViewById(R.id.turbine2_active));
        turbineCheckBoxes.add((CheckBox) findViewById(R.id.turbine3_active));
        turbineCheckBoxes.add((CheckBox) findViewById(R.id.turbine4_active));
        turbineCheckBoxes.add((CheckBox) findViewById(R.id.turbine5_active));

        debitMaxs = new ArrayList<>();

        button = findViewById(R.id.button_run_calcul);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text;
                String qtotString = qtotView.getText().toString();
                String upElevString = upElevationView.getText().toString();
                if(!qtotString.equals("") &&  !upElevString.equals("")) {
                    int qtot = Integer.parseInt(qtotString);
                    int upElev = Integer.parseInt(upElevString);
                        for (int i = 0; i < 5; i++) {
                            text = debitViews.get(i).getText().toString();
                            if (turbineCheckBoxes.get(i).isChecked() && (text.length() != 0)) {
                                debitMaxs.add(Integer.parseInt(text));
                            } else if (turbineCheckBoxes.get(i).isChecked()) {
                                debitMaxs.add(160);
                            } else {
                                debitMaxs.add(0);
                            }
                        }
                    //Log.d("[ DEBITS_MAX_INIT ]", debitMaxs.toString());
                    PowerPlant powerPlant = new PowerPlant(qtot, upElev, debitMaxs);
                    Intent intent = new Intent(ComputeActivity.this, ResultActivity.class);
                    ArrayList<ArrayList<Integer>> powerPlantRepartition = powerPlant.getRepartitionOpti();
                    int power = 0;
                    for (int i = 0; i < 5; i++) {
                        power += powerPlantRepartition.get(1).get(i);
                    }
                    intent.putExtra("debits", powerPlantRepartition.get(0));
                    intent.putExtra("power", power);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.error_message),Toast.LENGTH_LONG).show();
                }
            }
        });
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
        Intent i = new Intent(ComputeActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }

}
