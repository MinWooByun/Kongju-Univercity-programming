package com.example.ui;

public  class ListItem {
    private int number;
    private String title;
    private int tag;

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public int getTag() {return tag;}

    public void setNumber(int number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag() {this.tag = tag;}

    ListItem (int number, String title, int tag){
        this.number = number;
        this.title = title;
        this.tag = tag;
    }
}
