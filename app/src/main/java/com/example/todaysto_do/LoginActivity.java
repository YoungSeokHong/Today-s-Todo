package com.example.todaysto_do;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;

public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    EditText et_id, et_pw;
    File caccheFile;
    String id, pw;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.id = "1";
        this.pw = "1";

        this.btn_login = (Button)findViewById(R.id.btn_login);
        this.et_id = (EditText)findViewById(R.id.et_id);
        this.et_pw = (EditText)findViewById(R.id.et_pw);

        this.btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(login()){
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean login(){
        String inputId = et_id.getText().toString();
        String inputPw = et_pw.getText().toString();

        if(this.id.equals(inputId) && this.pw.equals(inputPw)) return true;
        else return false;
    }
}