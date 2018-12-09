package com.example.clair.computedebits;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class GraphicActivity extends AppCompatActivity {
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
        setContentView(R.layout.graph);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.graph_title);

        list = findViewById(R.id.see_list);
        qTots = new ArrayList<>();
        lineNumbers = new ArrayList<>();
        upElevations = new ArrayList<>();
        maxDebits = new ArrayList<>();
        repartitionPowers = new ArrayList<>();
        repartitionDebits = new ArrayList<>();
        int debit_max = 0;
        int debit_min = 200;

        readAndInsert();

        LineGraphSeries<DataPoint> serieTurbine1 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> serieTurbine2 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> serieTurbine3 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> serieTurbine4 = new LineGraphSeries<DataPoint>();
        LineGraphSeries<DataPoint> serieTurbine5 = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < repartitionDebits.size(); i++){
            PowerPlant powerPlant = new PowerPlant(
                    Integer.parseInt(qTots.get(i)),
                    Integer.parseInt(upElevations.get(i)),
                    maxDebits.get(i));
            ArrayList<ArrayList<Integer>> powerPlantRepartition = powerPlant.getRepartitionOpti();

            int powerTurbine1 = powerPlantRepartition.get(0).get(0);
            int powerTurbine2 = powerPlantRepartition.get(0).get(1);
            int powerTurbine3 = powerPlantRepartition.get(0).get(2);
            int powerTurbine4 = powerPlantRepartition.get(0).get(3);
            int powerTurbine5 = powerPlantRepartition.get(0).get(4);
            serieTurbine1.appendData(new DataPoint(i+1,powerTurbine1),true,repartitionPowers.size());
            serieTurbine2.appendData(new DataPoint(i+1,powerTurbine2),true,repartitionPowers.size());
            serieTurbine3.appendData(new DataPoint(i+1,powerTurbine3),true,repartitionPowers.size());
            serieTurbine4.appendData(new DataPoint(i+1,powerTurbine4),true,repartitionPowers.size());
            serieTurbine5.appendData(new DataPoint(i+1,powerTurbine5),true,repartitionPowers.size());

            if (powerTurbine1>debit_max){
                debit_max = powerTurbine1;
            }
            if (powerTurbine2>debit_max){
                debit_max = powerTurbine2;
            }
            if (powerTurbine3>debit_max){
                debit_max = powerTurbine3;
            }
            if (powerTurbine4>debit_max){
                debit_max = powerTurbine4;
            }
            if (powerTurbine5>debit_max){
                debit_max = powerTurbine5;
            }

            if (powerTurbine1<debit_min){
                debit_min = powerTurbine1;
            }
            if (powerTurbine2<debit_min){
                debit_min = powerTurbine2;
            }
            if (powerTurbine3<debit_min){
                debit_min = powerTurbine3;
            }
            if (powerTurbine4<debit_min){
                debit_min = powerTurbine4;
            }
            if (powerTurbine5<debit_min){
                debit_min = powerTurbine5;
            }
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);

        serieTurbine1.setColor(Color.RED);
        serieTurbine2.setColor(Color.BLACK);
        serieTurbine3.setColor(Color.GREEN);
        serieTurbine4.setColor(Color.BLUE);
        serieTurbine5.setColor(Color.CYAN);

        serieTurbine1.setTitle("Turbine 1");
        serieTurbine2.setTitle("Turbine 2");
        serieTurbine3.setTitle("Turbine 3");
        serieTurbine4.setTitle("Turbine 4");
        serieTurbine5.setTitle("Turbine 5");

        graph.addSeries(serieTurbine1);
        graph.addSeries(serieTurbine2);
        graph.addSeries(serieTurbine3);
        graph.addSeries(serieTurbine4);
        graph.addSeries(serieTurbine5);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(20);
        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(debit_min-1);
        graph.getViewport().setMaxY(debit_max+1);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
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
                while (lineIndice <= 20) {

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
        Intent i = new Intent(GraphicActivity.this, MainActivity.class);
        startActivity(i);
        return true;
    }
}
