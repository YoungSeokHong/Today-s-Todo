package com.example.todaysto_do;

import java.io.Serializable;
import java.util.Calendar;

public class MainData implements Serializable {

    private int icon;
    private String day;
    private String content;
    private Calendar dDay;

    public MainData(){
        this.icon = icon;
        this.day = day;
        this.content = content;
        this.dDay = Calendar.getInstance();
    }

    public String getDay() {return day;}
    public void setDay(String day) {this.day = day;}

    public int getIcon() {return icon;}
    public void setIcon(int icon) {this.icon = icon;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public Calendar getDDay() {return dDay;}
    public void setDDay(int year, int month, int day) {this.dDay.set(year, month, day);}
}
