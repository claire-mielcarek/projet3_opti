package com.example.clair.computedebits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private ArrayList<Integer> datas;
    private Context context;

    public ItemAdapter(Context context, ArrayList<Integer> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int lineIndice = datas.get(i);

        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.see_item, viewGroup, false);
        }

        TextView line = (TextView) view.findViewById(R.id.line_number);
        line.setText(""+lineIndice);
        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public int getCount() {
        return datas.size();
    }
}

