package com.example.testversion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> data;

    public ItemAdapter(@NonNull Context context, ArrayList<Item> data) {
        super(context, R.layout.item,data);
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View listItemView=convertView;

        if(listItemView==null) {
            listItemView = inflater.inflate(R.layout.item, null);
        }
            ImageView imageView=listItemView.findViewById(R.id.imageView);
            TextView text1=listItemView.findViewById(R.id.name);
            //TextView text2=listItemView.findViewById(R.id.amount);
            TextView text3=listItemView.findViewById(R.id.price);
            Item currentItem=data.get(position);
            imageView.setImageResource(currentItem.getImage());
            text1.setText(currentItem.getName());
            //text2.setText(currentItem.getAmount()+"");
            text3.setText(String.valueOf(currentItem.getPrice()));

        return listItemView;
    }
}
