package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentBill extends AppCompatActivity {

    private TextView custNum, theDate, theTotal, billNum;
    private ArrayList<Item> itemArrayList;
    private ListView lItem;
    Button back;
    ItemAdapter adapter;
    String stCustNum, stTheDate, stTheTotal, stBillNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_bill);

        billNum=findViewById(R.id.billNum);
        custNum=findViewById(R.id.custNum);
        theDate=findViewById(R.id.date);
        theTotal=findViewById(R.id.total);
        lItem=findViewById(R.id.theBill);

        Intent intent=getIntent();
        stBillNum=intent.getStringExtra("Bill num");
        stCustNum=intent.getStringExtra("Cust num");
        stTheDate=intent.getStringExtra("The date");
        stTheTotal=intent.getStringExtra("Total");

        billNum.setText(stBillNum);
        custNum.setText(stCustNum);
        theDate.setText(stTheDate);
        theTotal.setText(stTheTotal);
        itemArrayList=BillList.itemArrayList;

        adapter=new ItemAdapter(this, itemArrayList);
        lItem.setAdapter(adapter);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(CurrentBill.this, BillList.class);
                startActivity(intent1);
            }
        });
    }
}