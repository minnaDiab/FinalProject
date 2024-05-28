package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CurrentOrder extends AppCompatActivity {

    private TextView custNum, theDate, theTotal, orderNum;
    public static ArrayList<Item> itemArrayList;
    private ListView lItem;
    Button back, done;
    ItemAdapter adapter;
    String stCustNum, stTheDate, stTheTotal, stOrderNum, stBillNum;
    FirebaseComm Mydb;
    public static Bill currentBill;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        orderNum=findViewById(R.id.orderNum);
        custNum=findViewById(R.id.custNum);
        theDate=findViewById(R.id.date);
        theTotal=findViewById(R.id.total);

        Intent intent=getIntent();
        stOrderNum=intent.getStringExtra("Order num");
        stCustNum=intent.getStringExtra("Cust num");
        stTheDate=intent.getStringExtra("The date");
        stTheTotal=intent.getStringExtra("Total");

        orderNum.setText(stOrderNum);
        custNum.setText(stCustNum);
        theDate.setText(stTheDate);
        theTotal.setText(stTheTotal);
        itemArrayList=OrdersList.itemArrayList;

        adapter=new ItemAdapter(this, itemArrayList);
        lItem.setAdapter(adapter);

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(CurrentOrder.this, OrdersList.class);
                startActivity(intent1);
            }
        });



        done=findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double duTotal= Double.parseDouble(stTheTotal);

                // Get the current date
                Date currentDate = new Date();

                // Define the date format you want
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                // Format the current date
                String date = dateFormat.format(currentDate);

                Bill bill=new Bill(stCustNum,date,duTotal,itemArrayList);
                stBillNum= String.valueOf(bill.getBillNum());
                Mydb.setFireStoreDocument("Bill", bill.BillToHashMap());

                currentBill=new Bill(stCustNum,stTheDate,duTotal,itemArrayList);

                Intent intent1=new Intent(CurrentOrder.this, AfterPreparing.class);


                FirestoreHelper firestoreHelper = new FirestoreHelper();
                firestoreHelper.deleteDocument("Oder", stOrderNum);

            }
        });



    }
}