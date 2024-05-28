package com.example.testversion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Map;

public class BillList extends AppCompatActivity {
    Firebase Mydb;
    private ArrayList<Bill> billArrayList;
    private ListView lBill;
    Button back;
    BillAdapter adapter;
    ArrayList<Map<String,Object>>arr ;
    public static ArrayList<Item> itemArrayList;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        itemArrayList=new ArrayList<>();
        back=findViewById(R.id.back1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BillList.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lBill=findViewById(R.id.billsList);
        arr=new ArrayList<>();
        Mydb=new Firebase();

        String stPhone=RegisterActivity.currentCust.getPhone();
        arr=new ArrayList<>();
        arr=Mydb.getDocumentWhereEqualWithLimit("Bills","Cust Num",stPhone,100);

        //insert the orders from the firestore to the list
        if(arr!=null) {
            for (int i = 0; i < arr.size(); i++) {
                Bill bill = new Bill();
                bill.maptoBill(arr.get(i));
                billArrayList.add(bill);
            }

            adapter=new BillAdapter(this, billArrayList);
            lBill.setAdapter(adapter);


            //transfer the current order's information to the current order page
            lBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent();
                    Bill o=billArrayList.get(i);
                    intent.putExtra("Bill num",o.getBillNum());
                    intent.putExtra("Cust num", o.getCustNum());
                    intent.putExtra("The date", o.getBillDate());
                    intent.putExtra("Total", o.getSumOfBill());
                    itemArrayList=o.getItems();
                    intent=new Intent(BillList.this, CurrentBill.class);
                    startActivityForResult(intent,0);
                }
            });

        }
        else {
            arr=new ArrayList<>();
            Toast.makeText(this, "No records find!", Toast.LENGTH_SHORT).show();
        }


    }
}

