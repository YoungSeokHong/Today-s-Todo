package com.example.todaysto_do;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarActivity extends AppCompatActivity {
    private ArrayList<MainData> todoList, doneList, tmptList;

    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private ImageView iv_back_main;
    private RecyclerView recyclerView;
    private CalendarView cv_day;
    private CalendarAdapter mainAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog dialog_new_todo;
    private long dDay;
    private String content;
    private static Calendar mainCalendar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        this.todoList = (ArrayList<MainData>) intent.getSerializableExtra("todoList");
        this.doneList = (ArrayList<MainData>) intent.getSerializableExtra("doneList2");

        // set components
        this.iv_back_main = (ImageView) findViewById(R.id.iv_back_main);
        this.cv_day = (CalendarView) findViewById(R.id.cv_day);
        this.recyclerView = (RecyclerView) findViewById(R.id.rv_tmpt);

        // set RecyclerView
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);

        this.tmptList = new ArrayList<>();
        this.mainAdapter = new CalendarAdapter(tmptList);
        this.recyclerView.setAdapter(mainAdapter);

        mainCalendar = Calendar.getInstance();
        renewRecyclerView(mainCalendar.get(Calendar.YEAR), mainCalendar.get(Calendar.MONTH), mainCalendar.get(Calendar.DAY_OF_MONTH));
        // set onClickListener
        this.iv_back_main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                intent.putExtra("todoList", todoList);
                intent.putExtra("doneList3", todoList);
                finish();
                startActivity(intent);
            }
        });

        this.cv_day.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                mainCalendar.set(year, month, dayOfMonth);
                renewRecyclerView(year, month, dayOfMonth);
                dDay = getDDay(year, month, dayOfMonth);
            }
        });
    }

    public void renewRecyclerView(int year, int month, int dayOfMonth) {
        tmptList.clear();
        for (MainData data : todoList) {
            Calendar tmptDay = data.getDDay();
            if (tmptDay.get(Calendar.YEAR) == year && tmptDay.get(Calendar.MONTH) == month && tmptDay.get(Calendar.DAY_OF_MONTH) == dayOfMonth) {
                tmptList.add(data);
            }
        }
        mainAdapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option_cal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_new:
                showAddTodo();
                break;
            case R.id.option_renew:
                Calendar now = Calendar.getInstance();
                this.cv_day.setDate(now.getTimeInMillis());
                renewRecyclerView(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                break;
            case R.id.option_remove:
                for (MainData data : tmptList) {
                    for(int i=0; i<todoList.size(); i++){
                        if(data.getContent().equals(todoList.get(i).getContent())) todoList.remove(i);
                    }
                }
                tmptList.clear();
                mainAdapter.notifyDataSetChanged();
                break;
        }

        return false;

    }

    private void showAddTodo() {
        this.dialog_new_todo = new Dialog(CalendarActivity.this);
        this.dialog_new_todo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog_new_todo.setContentView(R.layout.dialog_new);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_new_todo.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_new_todo.getWindow().setAttributes(lp);

        EditText et_content = (EditText) dialog_new_todo.findViewById(R.id.et_content);
        Button btn_ok = (Button) dialog_new_todo.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog_new_todo.findViewById(R.id.btn_cancel);

        MainData mainData = new MainData();

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
                mainData.setDDay(mainCalendar.get(Calendar.YEAR), mainCalendar.get(Calendar.MONTH), mainCalendar.get(Calendar.DAY_OF_MONTH));
                mainData.setContent(content);
                mainData.setDay(s);
                if (dDay > 7) mainData.setIcon(R.drawable.img_main3);
                else if (dDay > 1) mainData.setIcon(R.drawable.img_main2);
                else mainData.setIcon(R.drawable.img_main);

                todoList.add(mainData);
                tmptList.add(mainData);
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

    // D-day 계산
    private long getDDay(int year, int month, int day) {
        final Calendar dDayCalendar = Calendar.getInstance();
        dDayCalendar.set(year, month, day);

        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dDay - today;

        return result;
    }
}