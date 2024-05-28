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

public class BillAdapter extends ArrayAdapter<Bill> {

    private Context context;
    private ArrayList<Bill> data;

    public BillAdapter(@NonNull BillList context, ArrayList<Bill> data){
        super(context,R.layout.bill,data);
        this.context=context;
        this.data=data;
    }


    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View listBillView=convertView;

        if(listBillView==null) {
            listBillView = inflater.inflate(R.layout.item, null);
        }
       // TextView text1=listBillView.findViewById(R.id.textViewBL);
        TextView text2=listBillView.findViewById(R.id.date);
        TextView text3=listBillView.findViewById(R.id.total);
        Bill currentBill=data.get(position);
       // text1.setText(String.valueOf(currentBill.getCustNum()));
        text2.setText(currentBill.getBillDate());
        text3.setText(String.valueOf(currentBill.getSumOfBill()));

        return listBillView;
    }
}
