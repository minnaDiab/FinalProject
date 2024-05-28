package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class NewBill extends AppCompatActivity {
    private Button home;
    private TextView custnum, theDate, theTotal, billNum;
    private ArrayList<Item> itemArrayList;
    private ListView listView;
    private ItemAdapter adapter;
    String stCustNum, stTheDate, stTheTotal, stBillNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bill);

        home=findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(NewBill.this,MainActivity.class);
                startActivity(intent);
            }
        });

        billNum=findViewById(R.id.billNum);
        custnum=findViewById(R.id.custNum);
        theDate=findViewById(R.id.date);
        theTotal=findViewById(R.id.total);
        listView=findViewById(R.id.theBill);
        itemArrayList=new ArrayList<>();

        stBillNum= String.valueOf(CurrentOrder.currentBill.billNum);
        stCustNum=CurrentOrder.currentBill.custNum;
        stTheDate=CurrentOrder.currentBill.billDate;
        stTheTotal= String.valueOf(CurrentOrder.currentBill.sumOfBill);
        itemArrayList=CurrentOrder.currentBill.items;

        billNum.setText(stBillNum);
        custnum.setText(stCustNum);
        theDate.setText(stTheDate);
        theTotal.setText(stTheTotal);

        adapter=new ItemAdapter(this, itemArrayList);
        listView.setAdapter(adapter);


    }
}