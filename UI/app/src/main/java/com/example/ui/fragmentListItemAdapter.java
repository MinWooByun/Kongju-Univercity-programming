package com.example.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class fragmentListItemAdapter extends BaseAdapter {
    ArrayList<fragmentListItem> items = null;
    Context context;
    String r_id;
    public fragmentListItemAdapter(Context context, ArrayList<fragmentListItem> items){
        this.items = (ArrayList<fragmentListItem>) items;
        this.context =context;
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getR_id(int position) { return items.get(position).getRid();    }

    public String getR_name(int position) { return items.get(position).getRName();    }

    public int getP_num(int position) { return items.get(position).getPnum();    }

    public int getE_pay(int position) { return items.get(position).getEpay();    }

    public String getR_details(int position) { return items.get(position).getRdetails();    }

    public int get_State(int position) { return items.get(position).getState();}

    public int getS_State(int position) { return items.get(position).getSstate();}
    public int getS_Kindness(int position) { return items.get(position).getSkindness();}
    public int getS_Term(int position) { return items.get(position).getSterm();}

    public int getU_check(int position) {return items.get(position).getUcheck();}
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.proposal, parent, false);
        return view;
    }

}
