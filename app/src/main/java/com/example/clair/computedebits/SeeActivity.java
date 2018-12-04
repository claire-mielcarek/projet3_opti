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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.clair.computedebits.debitDevider.PowerPlant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SeeActivity extends AppCompatActivity {
    ListView list;
    ArrayList<String> qTots;
    ArrayList<String> lineNumbers;
    ArrayList<String> upElevations;
    ArrayList<ArrayList<Integer>> maxDebits;
    ArrayList<ArrayList<Integer>> repartitionDebits;
    ArrayList<ArrayList<Integer>> repartitionPowers;

    int DEBIT_MAX_DEFAULT = 160;
    int DEBIT_UNUSED_TURBINE = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.see_title);

        list = findViewById(R.id.see_list);
        qTots = new ArrayList<>();
        lineNumbers = new ArrayList<>();
        upElevations = new ArrayList<>();
        maxDebits = new ArrayList<>();
        repartitionPowers = new ArrayList<>();
        repartitionDebits = new ArrayList<>();

        readAndInsert();

        ArrayList<ArrayList<String>> args = new ArrayList<>();
        args.add(lineNumbers);
        args.add(qTots);
        args.add(upElevations);
        list.setAdapter(new ItemAdapter(this, args));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PowerPlant powerPlant = new PowerPlant(
                        Integer.parseInt(qTots.get(i)),
                        Integer.parseInt(upElevations.get(i)),
                        maxDebits.get(i));
                Intent intent = new Intent(SeeActivity.this, ResultActivity.class);
                ArrayList<ArrayList<Integer>> powerPlantRepartition = powerPlant.getRepartitionOpti();
                int power = 0;
                for (int ind = 0; ind < 5; ind++) {
                    power += powerPlantRepartition.get(1).get(ind);
                }
                intent.putExtra("debits", powerPlantRepartition.get(0));
                intent.putExtra("power", power);

                startActivity(intent);
            }
        });

    }

    private void readAndInsert(){
        AssetManager assetManager = getAssets();
        InputStream is = null;

        try {
            is = assetManager.open("CCD_Data.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is != null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String line;
            ArrayList<Integer> currentRepartitionDebits;
            ArrayList<Integer> currentRepartitionPowers;
            ArrayList<Integer> currentMaxDebits;
            int currentDebit;
            StringTokenizer st;
            int lineIndice = 1;
            try {
                line = reader.readLine();
                while (line != null) {

                    currentMaxDebits = new ArrayList<>();
                    currentRepartitionDebits = new ArrayList<>();
                    currentRepartitionPowers = new ArrayList<>();

                    st = new StringTokenizer(line,";");
                    lineNumbers.add(""+lineIndice);
                    qTots.add(st.nextToken());
                    upElevations.add(st.nextToken());

                    for(int i=0; i<5; i++){
                        currentDebit = Integer.parseInt(st.nextToken());
                        if(currentDebit == 0) {
                            currentMaxDebits.add(DEBIT_UNUSED_TURBINE);
                        }
                        else if(currentDebit < DEBIT_MAX_DEFAULT){
                            currentMaxDebits.add(((int) (currentDebit/5) + 1) * 5);
                        }
                        else{
                            currentMaxDebits.add(DEBIT_MAX_DEFAULT);
                        }
                        currentRepartitionDebits.add(currentDebit);
                        currentRepartitionPowers.add(Integer.parseInt(st.nextToken()));
                    }
                    repartitionDebits.add(currentRepartitionDebits);
                    repartitionPowers.add(currentRepartitionPowers);
                    maxDebits.add(currentMaxDebits);
                    //Log.d("[ LINE_PRINT ]", ""+lineIndice + " " + qTots.get(lineIndice-1) + " " + upElevations.get(lineIndice-1));
                    lineIndice++;
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
