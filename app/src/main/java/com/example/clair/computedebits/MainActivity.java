package com.example.clair.computedebits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.clair.computedebits.R;

public class MainActivity extends AppCompatActivity {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        Button buttonSee = findViewById(R.id.button_see);
        buttonSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SeeActivity.class);
                startActivity(i);
            }
        });

        Button buttonCompute = findViewById(R.id.button_compute);
        buttonCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ComputeActivity.class);
                startActivity(i);
            }
        });

        ImageView info = findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), Html.fromHtml(getApplicationContext().getResources().getString(R.string.credit_message)),Toast.LENGTH_LONG).show();
            }
        });
    }
}
