package com.example.drashtimuni.hydrologicaldatamanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * @author Drashti Pathak
 */
public class DescriptionOfApp extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_of_app);
        button = findViewById(R.id.second_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (DescriptionOfApp.this,MainFeaturesOfApp.class);
                startActivity(intent);
            }
        });
    }
}