package com.example.todaysto_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SecurityActivity extends AppCompatActivity {
    ImageView iv_back_login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        this.iv_back_login = (ImageView)findViewById(R.id.iv_back_login);

        iv_back_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SecurityActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}