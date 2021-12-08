package com.example.todaysto_do;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;

public class SecurityPopupActivity extends Activity {
    private int[] numBtnIDs = {R.id.btn_one, R.id.btn_two, R.id.btn_three, R.id.btn_four};
    private String num;

    private ImageView iv_del;
    private Button[] numBtn = new Button[numBtnIDs.length];
    private Button btn_ok, btn_cancel;
    private EditText et_pw_simple;

    private OnclickListener onclickListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_security);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);

        onclickListener = new OnclickListener();

        this.btn_ok = (Button)findViewById(R.id.btn_ok);
        this.btn_cancel = (Button)findViewById(R.id.btn_cancel);
        this.iv_del = (ImageView)findViewById(R.id.iv_del);
        this.et_pw_simple = (EditText)findViewById(R.id.et_pw_simple);
        for(int i=0; i<numBtnIDs.length; i++) numBtn[i] = findViewById(numBtnIDs[i]);

        this.btn_ok.setOnClickListener(this.onclickListener);
        this.btn_cancel.setOnClickListener(this.onclickListener);
        this.et_pw_simple.setOnClickListener(this.onclickListener);
        this.iv_del.setOnClickListener(this.onclickListener);
        for(Button button : numBtn) button.setOnClickListener(this.onclickListener);
    }

    private class OnclickListener implements View.OnClickListener{
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_ok:
                    String tmpt = et_pw_simple.getText().toString();
                    if(tmpt.length() < 4) {
                        Toast.makeText(getApplicationContext(), "비밀번호는 4자리 수를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    saveNum(tmpt);
                    Toast.makeText(getApplicationContext(), "비밀번호설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                case R.id.btn_cancel:
                    finish();
                    break;
                case R.id.iv_del:
                    if(num == null || num.equals("")) break;
                    num = deleteNum(Integer.parseInt(num));
                    et_pw_simple.setText(num);
                    break;
                default:
                    num = et_pw_simple.getText().toString() + inputNum(v.getId());
                    et_pw_simple.setText(num);
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

    private void saveNum(String num){
        Intent intent = new Intent();
        intent.putExtra("result", num);
        setResult(RESULT_OK, intent);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
}