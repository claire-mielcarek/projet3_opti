package com.example.clair.computedebits;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class ComputeActivity extends AppCompatActivity {
    EditText qtotView;
    EditText upElevationView;
    ArrayList<EditText> debitViews;
    ArrayList<CheckBox> turbineCheckBoxes;
    ArrayList<Integer> debitMaxs;
    Button button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compute);

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
                int qtot = Integer.parseInt(qtotView.getText().toString());
                int upElev = Integer.parseInt(upElevationView.getText().toString());
                for(int i=0; i<5; i++){
                    text = debitViews.get(i).getText().toString();
                    if (turbineCheckBoxes.get(i).isChecked() && (text.length() != 0)){
                        debitMaxs.add(Integer.parseInt(text));
                    }
                    else if (turbineCheckBoxes.get(i).isChecked()){
                        debitMaxs.add(160);
                    }
                    else{
                        debitMaxs.add(0);
                    }
                }
                Log.d("[ DEBITS_MAX_INIT ]", debitMaxs.toString());
                PowerPlant powerPlant = new PowerPlant(qtot, upElev, debitMaxs);
                Log.d("[ REPARTITION_DEBIT ]", powerPlant.getRepartitionOpti().toString());
            }
        });

    }
}
