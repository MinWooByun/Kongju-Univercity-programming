package com.example.ui;

public class fragmentListItem {

    private String r_name;
    private int p_num;
    private int e_pay;
    private String r_details;
    private String r_id;
    private int state;
    private float s_state;
    private float s_kindness;
    private float s_term;
    private int u_check;

    public String getRName() {
        return r_name;
    }
    public int getPnum() { return p_num; }
    public int getEpay() {
        return e_pay;
    }
    public String getRdetails() {
        return r_details;
    }
    public String getRid() { return r_id;}
    public int getState(){return state;}
    public float getSstate(){return s_state;}
    public float getSkindness(){return s_kindness;}
    public float getSterm(){return s_term;}
    public int getUcheck(){return u_check;}

    public void setRName(String r_name) {this.r_name = r_name; }
    public void setPnum(int p_num) {
        this.p_num = p_num;
    }
    public void setEpay(int e_pay) {
        this.e_pay = e_pay;
    }
    public void setRdetails(String r_details) {this.r_details = r_details; }
    public void setRid(String r_id) {this.r_id = r_id;}
    public void setState(int state) {
        this.state = state;
    }
    public void setSstate(float s_state) {
        this.s_state = s_state;
    }
    public void setSkindness(float s_kindness) {
        this.s_kindness = s_kindness;
    }
    public void setSterm(float s_term) {
        this.s_term = s_term;
    }
    public void setUcheck(int u_check) {this.u_check=u_check;}

    fragmentListItem (String r_name, int p_num, int e_pay , String r_details, String r_id,
    int state, float s_state, float s_kindness, float s_term, int u_check){
        this.r_name = r_name;
        this.p_num = p_num;
        this.e_pay = e_pay;
        this.r_details = r_details;
        this.r_id = r_id;
        this.state=state;
        this.s_state=s_state;
        this.s_kindness=s_kindness;
        this.s_term=s_term;
        this.u_check=u_check;
    }
}
