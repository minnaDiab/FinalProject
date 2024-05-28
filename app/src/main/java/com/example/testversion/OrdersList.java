package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class OrdersList extends AppCompatActivity {
    FirebaseComm Mydb;
    private ArrayList<Order> orderArrayList;
    private ListView lOrder;
    Button back;
    OrderAdapter adapter;
    ArrayList<Map<String,Object>>arr ;
    public static ArrayList<Item> itemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        itemArrayList=new ArrayList<>();
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrdersList.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lOrder=findViewById(R.id.ordersList);
        orderArrayList=new ArrayList<>();
        Mydb=new FirebaseComm();
        Mydb.setFireStoreResult((FirebaseComm.FireStoreResult) this);
       // String stPhone=RegisterActivity.currentCust.getPhone();

        Mydb.getAllDocumentsInCollection("Orders");
        arr=Mydb.arr;
        //insert the orders from the firestore to the list
        if(arr!=null) {
            for (int i = 0; i < arr.size(); i++) {
                Order order = new Order();
                order.maptoOrder(arr.get(i));
                orderArrayList.add(order);
            }

            adapter=new OrderAdapter(this, orderArrayList);
            lOrder.setAdapter(adapter);


            //transfer the current order's information to the current order page
            lOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent();
                    Order o=orderArrayList.get(i);
                    intent.putExtra("Order num",o.getOrderNum());
                    intent.putExtra("Cust num", o.getCustNum());
                    intent.putExtra("The date", o.getOrderDate());
                    intent.putExtra("Total", o.getSumOfOrder());
                    itemArrayList=o.getItems();
                    intent=new Intent(OrdersList.this, CurrentOrder.class);
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
