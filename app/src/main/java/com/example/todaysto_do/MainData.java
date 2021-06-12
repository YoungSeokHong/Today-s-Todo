package com.example.todaysto_do;

import java.util.Calendar;

public class MainData {

    private int iv_profile;
    private String tv_day;
    private String tv_content;
    private Calendar dDay;

    public MainData(){
        this.iv_profile = iv_profile;
        this.tv_day = tv_day;
        this.tv_content = tv_content;
        this.dDay = Calendar.getInstance();
    }

    public String getTv_day() {return tv_day;}
    public void setTv_day(String tv_day) {this.tv_day = tv_day;}

    public int getIv_profile() {return iv_profile;}
    public void setIv_profile(int iv_profile) {this.iv_profile = iv_profile;}

    public String getTv_content() {return tv_content;}
    public void setTv_content(String tv_content) {this.tv_content = tv_content;}

    public Calendar getDDay() {return dDay;}
    public void setDDay(int year, int month, int day) {this.dDay.set(year, month, day);}
}
