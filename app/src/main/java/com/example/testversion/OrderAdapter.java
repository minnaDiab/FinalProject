package com.example.testversion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private ArrayList<Order> data;

    public OrderAdapter(@NonNull Context context, ArrayList<Order> data){
        super(context, R.layout.order, data);
        this.context=context;
        this.data=data;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View listOrderView=convertView;

        if(listOrderView==null) {
            listOrderView = inflater.inflate(R.layout.item, null);
        }
        // TextView text1=listOrderView.findViewById(R.id.textViewBL);
        TextView text2=listOrderView.findViewById(R.id.date);
        TextView text3=listOrderView.findViewById(R.id.total);
        Order currentOrder=data.get(position);
        // text1.setText(String.valueOf(currentOrder.getCustNum()));
        text2.setText(currentOrder.getOrderDate());
        text3.setText(String.valueOf(currentOrder.getSumOfOrder()));

        return listOrderView;
    }

}
