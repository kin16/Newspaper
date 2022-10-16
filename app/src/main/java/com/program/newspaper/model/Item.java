package com.program.newspaper.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;


// класс, описывающий одну новость
public class Item {
    private String title, text;
    private int id;
    private Date date;

    public Item(){

    }

    public Item(String title, String text) {
        this.id = generateId();
        this.title = title;
        this.text = text;
        this.date = generateDate();
    }

    // Генерация id
    private int generateId(){
        return ThreadLocalRandom.current().nextInt(0, 10000 + 1);
    }

    // Получение текущей даты
    private Date generateDate(){
        return Calendar.getInstance().getTime();
    }

    public int getId(){return id;}

    public void setId(int id){this.id = id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate(){return date;}

    public void setDate(Date date){this.date = date;}
}
