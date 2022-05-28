package com.example.ui;

public  class ListItem {
    private int number;
    private String title;

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }


    public void setNumber(int number) {
        this.number = number;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    ListItem (int number, String title){
        this.number = number;
        this.title = title;
    }
}
