package com.example.todaysto_do;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class SecurityActivity extends AppCompatActivity {
    private static boolean onSecur;
    private int[] numBtnIDs = {R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four};
    private String num, pw;

    private ImageView iv_back_login, iv_del;
    private Button[] numBtns = new Button[numBtnIDs.length];
    private EditText et_pw_simple;

    private OnclickListener onclickListener;
    private CacheSecur cacheSecur;
    private CachePW cachePW;
    private CacheLogin cacheLogin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        cacheSecur = new CacheSecur(this);
        cacheLogin = new CacheLogin(this);
        cachePW = new CachePW(this);

        try {
            pw = cachePW.Read();
            System.out.println(pw);
            System.out.println("------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pw.length() != 4) {
            Intent intent = new Intent(SecurityActivity.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        try {
                onSecur = Boolean.parseBoolean(cacheSecur.Read());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!onSecur){
            Intent intent = new Intent(SecurityActivity.this, MainActivity.class);
            startActivity(intent);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        onclickListener = new OnclickListener();

        this.iv_back_login = (ImageView)findViewById(R.id.iv_back_login);
        this.iv_del = (ImageView)findViewById(R.id.iv_del);
        this.et_pw_simple = (EditText)findViewById(R.id.et_pw_simple);
        for(int i=0; i<numBtnIDs.length; i++) numBtns[i] = findViewById(numBtnIDs[i]);

        this.iv_back_login.setOnClickListener(this.onclickListener);
        this.et_pw_simple.setOnClickListener(this.onclickListener);
        this.iv_del.setOnClickListener(this.onclickListener);
        for(Button button : numBtns) button.setOnClickListener(this.onclickListener);
    }

    private class OnclickListener implements View.OnClickListener{
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.iv_back_login:
                    try {
                        cacheLogin.Write("false");
                        cacheSecur.Write("false");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(SecurityActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                    break;
                case R.id.iv_del:
                    if(num == null || num.equals("")) break;
                    num = deleteNum(Integer.parseInt(num));
                    et_pw_simple.setText(num);
                    break;
                default:
                    num = et_pw_simple.getText().toString() + inputNum(v.getId());
                    et_pw_simple.setText(checkNum(num, v.getId()));
                    break;
            }
        }
    }

    private String deleteNum(int num){
        if(num != 0) num /= 10;
        if(num == 0) return "";
        else return String.valueOf(num);
    }

    private int inputNum(int id){
        for(int i=0; i<numBtnIDs.length; i++) if(id == numBtnIDs[i]) return i+1;
        return 0;
    }

    private String checkNum(String num, int id){
        if(num.length() >= 4){
            if(num.equals(pw)){
                Toast.makeText(getApplicationContext(), "접속에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SecurityActivity.this, MainActivity.class);
                finish();
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            }
            return "";
        }else{
            return et_pw_simple.getText().toString() + inputNum(id);
        }
    }
}