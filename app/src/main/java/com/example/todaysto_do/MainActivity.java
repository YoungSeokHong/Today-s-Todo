package com.example.todaysto_do;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    // Attributes
    private static boolean onSecur;
    private static String simplePw;
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private long backKeyPressedTime = 0;
    private String content;
    private long dDay;

    // Components
    private ArrayList<MainData> todoList, doneList, noneList;
    private TextView tv_showDone, tv_showFriend, tv_security, tv_sync, tv_logout, tv_title_todo;
    private ImageView iv_menu, iv_calendar;
    private Switch sw_security, sw_sync;
    private Button btn_add;
    private Dialog dialog_new_todo, dialog_remove;
    private View naviView;

    private OnclickListener onclickListener;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private TodoAdapter mainAdapter;
    private LinearLayoutManager linearLayoutManager;
    private CacheSecur cacheSecur;
    private CacheLogin cacheLogin;
    private CachePW cachePW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        cacheSecur = new CacheSecur(this);
        cacheLogin = new CacheLogin(this);
        cachePW = new CachePW(this);
        try {
            this.onSecur = Boolean.parseBoolean(cacheSecur.Read());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        this.tv_title_todo = (TextView) findViewById(R.id.tv_title_todo);
        this.sw_security = (Switch) findViewById(R.id.sw_security);
        this.sw_sync = (Switch) findViewById(R.id.sw_sync);

        if (onSecur) this.sw_security.setChecked(true);
        else this.sw_security.setChecked(false);

        // set RecyclerView
        this.linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.setNestedScrollingEnabled(false);

        this.todoList = new ArrayList<>();
        this.doneList = new ArrayList<>();
        this.noneList = new ArrayList<>();

        try {
            Intent intent = getIntent();
            ArrayList<MainData> tmpt = (ArrayList<MainData>) intent.getSerializableExtra("todoList");
            if (tmpt != null) todoList = tmpt;
            sortTodo();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        this.mainAdapter = new TodoAdapter(todoList);
        this.recyclerView.setAdapter(mainAdapter);


        // set OnclickListeners
        this.btn_add.setOnClickListener(onclickListener);
        this.iv_menu.setOnClickListener(onclickListener);
        this.tv_showDone.setOnClickListener(onclickListener);
        this.tv_showFriend.setOnClickListener(onclickListener);
        this.tv_security.setOnClickListener(onclickListener);
        this.tv_sync.setOnClickListener(onclickListener);
        this.tv_logout.setOnClickListener(onclickListener);
        this.iv_calendar.setOnClickListener(onclickListener);

        String tmpt = "To-do (" + todoList.size() + ")";
        tv_title_todo.setText(tmpt);

        // set OnCheckedChangeListener
        this.sw_security.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (simplePw == null) {
                        setSimplePw();
                        System.out.println(simplePw);
                        if (simplePw == null) {
                            sw_security.setChecked(false);
                            return;
                        }
                    }
                    onSecur = true;
                    try {
                        cacheSecur.Write(String.valueOf(onSecur));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    onSecur = false;

                    try {
                        cacheSecur.Write(String.valueOf(onSecur));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // set SwipeListener
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int tmptIndex = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.RIGHT) {
                    todoList.get(tmptIndex).setIcon(R.drawable.img_done);
                    doneList.add(todoList.get(tmptIndex));
                    todoList.remove(tmptIndex);
                    mainAdapter.notifyItemRemoved(tmptIndex);
                } else {
                    removeTodo(tmptIndex);
                }
                String tmpt = "To-do (" + todoList.size() + ")";
                tv_title_todo.setText(tmpt);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void removeTodo(int tmptIndex) {
        this.dialog_remove = new Dialog(MainActivity.this);
        this.dialog_remove.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog_remove.setContentView(R.layout.dialog_remove);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_remove.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog_remove.getWindow().setAttributes(lp);

        TextView tv_content_remove = (TextView) dialog_remove.findViewById(R.id.tv_content_remove);
        Button btn_yes = (Button) dialog_remove.findViewById(R.id.btn_yes);
        Button btn_cancel = (Button) dialog_remove.findViewById(R.id.btn_cancel);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                todoList.remove(tmptIndex);
                mainAdapter.notifyItemRemoved(tmptIndex);
                dialog_remove.dismiss();
                String tmpt = "To-do (" + todoList.size() + ")";
                tv_title_todo.setText(tmpt);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAdapter.notifyDataSetChanged();
                dialog_remove.dismiss();
            }
        });

        dialog_remove.show();
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
                mainData.setContent(content);
                mainData.setDay(s);
                if (dDay > 7) mainData.setIcon(R.drawable.img_main3);
                else if (dDay > 1) mainData.setIcon(R.drawable.img_main2);
                else mainData.setIcon(R.drawable.img_main);

                todoList.add(mainData);
                String tmpt = "To-do (" + todoList.size() + ")";
                tv_title_todo.setText(tmpt);
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

    // D-day 계산
    private long getDDay(int year, int month, int day) {
        final Calendar dDayCalendar = Calendar.getInstance();
        dDayCalendar.set(year, month, day);

        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dDay - today;

        return result;
    }

    // RecyclerView 정렬
    private void sortTodo() {
        Collections.sort(todoList, cmpAsc);
    }

    private Comparator<MainData> cmpAsc = new Comparator<MainData>() {
        @Override
        public int compare(MainData o1, MainData o2) {
            return o1.getDDay().compareTo(o2.getDDay());
        }
    };

    // OnClickListener
    private class OnclickListener implements View.OnClickListener {
        @SuppressLint("NonConstantResourceId")
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add:
                    showAddTodo();
                    break;
                case R.id.iv_navi:
                    drawerLayout.openDrawer(naviView);
                    break;
                case R.id.iv_calendar:
                    Intent intentCal = new Intent(MainActivity.this, CalendarActivity.class);
                    intentCal.putExtra("todoList", todoList);
                    intentCal.putExtra("doneList2", doneList);
                    finish();
                    startActivity(intentCal);
                    break;
                case R.id.tv_showDone:
                    Intent intentDone = new Intent(MainActivity.this, DoneActivity.class);
                    intentDone.putExtra("doneList", doneList);
                    startActivity(intentDone);
                    break;
                case R.id.tv_showFriend:
                    Toast.makeText(getApplicationContext(), "아직 준비 중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_security:
                    setSimplePw();
                    break;
                case R.id.tv_sync:
                    Toast.makeText(getApplicationContext(), "아직 준비 중인 서비스 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_logout:
                    try {
                        cacheLogin.Write("false");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
        }
    }

    private void setSimplePw() {
        Intent intentSecur = new Intent(MainActivity.this, SecurityPopupActivity.class);
        startActivityForResult(intentSecur, 1);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                this.simplePw = data.getStringExtra("result");
                try {
                    System.out.println(simplePw);
                    cachePW.Write(simplePw);
                    System.out.println(cachePW.Read());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (simplePw != null) {
                sw_security.setChecked(true);
            }
        }
    }
}