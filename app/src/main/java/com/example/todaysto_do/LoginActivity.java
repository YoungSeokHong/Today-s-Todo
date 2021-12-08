package com.example.todaysto_do;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private static boolean login;
    private static final String LOGIN_SUCCESS = "로그인에 성공하였습니다.";
    private static final String LOGIN_FAIL = "로그인에 실패하였습니다.";
    private Button btn_login;
    private EditText et_id, et_pw;
    private String id, pw;
    private CacheLogin cacheLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cacheLogin = new CacheLogin(this);

        try {
                this.login = Boolean.parseBoolean(cacheLogin.Read());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(login){
            Intent intent = new Intent(LoginActivity.this, SecurityActivity.class);
            intent.putExtra("id", id);
            finish();
            startActivity(intent);
        }


        this.id = "test";
        this.pw = "test";

        this.btn_login = (Button)findViewById(R.id.btn_login);
        this.et_id = (EditText)findViewById(R.id.et_id);
        this.et_pw = (EditText)findViewById(R.id.et_pw);

        this.btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loginSuccess(login());
            }
        });
    }

    private boolean login(){
        String inputId = et_id.getText().toString();
        String inputPw = et_pw.getText().toString();
        if(this.id.equals(inputId) && this.pw.equals(inputPw)) return true;
        else return false;
    }

    private void loginSuccess(boolean login){
        if(login) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("id", id);

            try {
                cacheLogin.Write("true");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), LOGIN_SUCCESS, Toast.LENGTH_SHORT).show();

            et_id.setText("");
            et_pw.setText("");
            et_id.clearFocus();
            et_pw.clearFocus();
            finish();
            startActivity(intent);
        } else Toast.makeText(getApplicationContext(), LOGIN_FAIL, Toast.LENGTH_SHORT).show();
    }
}