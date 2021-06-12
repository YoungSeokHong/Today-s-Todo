package com.example.todaysto_do;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private ArrayList<MainData> todoList, doneList, noneList;
    private TextView tv_showDone, tv_showFriend, tv_security, tv_sync, tv_logout;
    private ImageView iv_menu, iv_calendar;
    private Button btn_add;
    private Dialog dialog_new_todo;
    private View naviView;

    private OnclickListener onclickListener;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String content;
    private long dDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onclickListener = new OnclickListener();

        // set components
        this.drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        this.naviView = (View) findViewById(R.id.navi);
        this.recyclerView = (RecyclerView) findViewById(R.id.rv_todo);
        this.btn_add = (Button) findViewById(R.id.btn_add);
        this.iv_menu = (ImageView) findViewById(R.id.iv_navi);
        this.iv_calendar = (ImageView) findViewById(R.id.iv_calendar);
        this.tv_showDone = (TextView) findViewById(R.id.tv_showDone);
        this.tv_showFriend = (TextView) findViewById(R.id.tv_showFriend);
        this.tv_security = (TextView) findViewById(R.id.tv_security);
        this.tv_sync = (TextView) findViewById(R.id.tv_sync);
        this.tv_logout = (TextView) findViewById(R.id.tv_logout);

        // set RecyclerView
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setNestedScrollingEnabled(false);

        this.todoList = new ArrayList<>();
        this.doneList = new ArrayList<>();
        this.noneList = new ArrayList<>();
        this.mainAdapter = new MainAdapter(todoList);
        this.recyclerView.setAdapter(mainAdapter);

        // set OnclickListeners
        this.btn_add.setOnClickListener(onclickListener);
        this.iv_menu.setOnClickListener(onclickListener);
        this.tv_showDone.setOnClickListener(onclickListener);
        this.tv_showFriend.setOnClickListener(onclickListener);
        this.tv_security.setOnClickListener(onclickListener);
        this.tv_sync.setOnClickListener(onclickListener);
        this.tv_logout.setOnClickListener(onclickListener);

    }

    private void showAddTodo() {
        this.dialog_new_todo = new Dialog(MainActivity.this);
        this.dialog_new_todo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog_new_todo.setContentView(R.layout.dialog_new_todo);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_new_todo.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_new_todo.getWindow().setAttributes(lp);

        EditText et_content = (EditText) dialog_new_todo.findViewById(R.id.et_content);
        CalendarView cv_day = (CalendarView) dialog_new_todo.findViewById(R.id.cv_day);
        Button btn_ok = (Button) dialog_new_todo.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog_new_todo.findViewById(R.id.btn_cancel);

        MainData mainData = new MainData();
        cv_day.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                dDay = getDDay(year, month, day);
                mainData.setDDay(year, month, day);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String strFormat;
                if (dDay > 0) strFormat = "D-%d";
                else if (dDay == 0) strFormat = "D-Day";
                else {
                    Toast.makeText(getApplicationContext(), "현재보다 이전의 날짜는 선택하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                content = et_content.getText().toString();
                if (content.equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = String.format(strFormat, dDay);
                mainData.setTv_content(content);
                mainData.setTv_day(s);
                if (dDay > 7) mainData.setIv_profile(R.drawable.img_main3);
                else if (dDay > 1) mainData.setIv_profile(R.drawable.img_main2);
                else mainData.setIv_profile(R.drawable.img_main);

                todoList.add(mainData);
                sortTodo();
                mainAdapter.notifyDataSetChanged();
                dialog_new_todo.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_new_todo.dismiss();
            }
        });

        dialog_new_todo.show();
    }

    private long getDDay(int year, int month, int day) {
        final Calendar dDayCalendar = Calendar.getInstance();
        dDayCalendar.set(year, month, day);

        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dDay - today;

        return result;
    }


    private void sortTodo() {
        Collections.sort(todoList, cmpAsc);
    }

    private Comparator<MainData> cmpAsc = new Comparator<MainData>() {
        @Override
        public int compare(MainData o1, MainData o2) {
            return o1.getDDay().compareTo(o2.getDDay());
        }
    };

    private class OnclickListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    showAddTodo();
                    break;
                case R.id.iv_navi:
                    drawerLayout.openDrawer(naviView);
                    break;
                case R.id.tv_showDone:
                    break;
                case R.id.tv_showFriend:
                    Toast.makeText(getApplicationContext(), "아직 준비 중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_security:
                    break;
                case R.id.tv_sync:
                    Toast.makeText(getApplicationContext(), "아직 준비 중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_logout:
                    finish();
                    break;
                default:
                    break;
            }

        }
    }

}