package com.example.clair.computedebits;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class ItemAdapter extends BaseAdapter {
    private ArrayList<ArrayList<String>> datas;
    private Context context;
    private int LINE = 0;
    private int QTOT = 1;
    private int UP_ELEVATION = 2;

    public ItemAdapter(Context context, ArrayList<ArrayList<String>> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        String lineText = datas.get(LINE).get(i);
        String qtotText = datas.get(QTOT).get(i);
        String upElevationText = datas.get(UP_ELEVATION).get(i);
        Log.d("[ LIST_ADAPTER ]", "getView");

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.see_item, viewGroup, false);
            Log.d("[ LIST_ADAPTER ]", "View created");
        }

        TextView lineView = view.findViewById(R.id.itemLineNumber);
        TextView qtotView = view.findViewById(R.id.itemQtot);
        TextView upElevationView = view.findViewById(R.id.itemUpElevation);

        lineView.setText(""+lineText);
        qtotView.setText(""+qtotText);
        upElevationView.setText(""+upElevationText);

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
        return datas.get(0).size();
    }
}

