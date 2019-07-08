package com.example.irregularview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private GoogleDrawView googleDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleDrawView = findViewById(R.id.chrome);

        googleDrawView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String colorName = (String) googleDrawView.getTag(googleDrawView.getId());
                Toast.makeText(MainActivity.this, colorName, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
