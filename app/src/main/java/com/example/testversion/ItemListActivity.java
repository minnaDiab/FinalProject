package com.example.testversion;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ItemListActivity extends AppCompatActivity implements BitmapHelper{

    private ArrayList<Item> itemArrayList, newItemAr;
    private ListView lItem;
    Button done, back;
    ItemAdapter adapter;
    FirebaseComm Mydb;
    Item item;
    Order order;
    String stName, stPrice, stAmount,stOrderNum;
    double total = 0;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list2);
        lItem = findViewById(R.id.itemsList);

        done = findViewById(R.id.done);
        back = findViewById(R.id.back);
        item=new Item();
        itemArrayList = new ArrayList<>();
        initItemList();

        adapter=new ItemAdapter(this,itemArrayList);
        lItem.setAdapter(adapter);


        //opens a dialog to add the amount and make a new list for the order
        lItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                stName = itemArrayList.get(i).getName();
                stPrice = String.valueOf(itemArrayList.get(i).getPrice());

                Context context = view.getContext();
                Dialog d = new Dialog(context);
                d.setContentView(R.layout.custom_dialog);
                EditText etAmount = d.findViewById(R.id.amount);
                Button btnBack = d.findViewById(R.id.back);
                TextView nameD = d.findViewById(R.id.nameD);
                TextView priceD =d.findViewById(R.id.priceD);
                nameD.setText(stName);
                priceD.setText(stPrice);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                Button btnAdd = d.findViewById(R.id.add);
                newItemAr = new ArrayList<>();

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stAmount = etAmount.getText().toString().trim();
                        item.setAmount(Integer.parseInt(stAmount));
                        item.setName(stName);
                        item.setPrice(Double.parseDouble(stPrice));

                        newItemAr.add(item);

                        total += ((item.getAmount()) * (item.getPrice()));
                        d.dismiss();
                    }
                });

                d.setCancelable(true);
                d.show();

            }

        });



        //making new order
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the current date
                Date currentDate = new Date();

                // Define the date format you want
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                // Format the current date
                String date = dateFormat.format(currentDate);

                String custPhone=RegisterActivity.currentCust.getPhone();

                order = new Order(custPhone, date, total, newItemAr);
                stOrderNum= String.valueOf(order.getOrderNum());
                Mydb=new FirebaseComm();
                Mydb.setFireStoreDocument("Orders", order.OrderToHashMap());

                Toast.makeText(ItemListActivity.this, "The order has been saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity( new Intent(ItemListActivity.this, RegisterActivity.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initItemList() {
        itemArrayList.add(new Item("Black back bag",150,R.drawable.black_back_bag));
        itemArrayList.add(new Item("Blue back bag",150,R.drawable.blue_back_bag));
        itemArrayList.add(new Item("Pink back bag",150,R.drawable.pink_back_bag));
        itemArrayList.add(new Item("Football back bag",180,R.drawable.football_back_bag));
        itemArrayList.add(new Item("Mickey Mouse back bag",180,R.drawable.mickey_mouse_back_bag));
        itemArrayList.add(new Item("Minnie Mouse back bag",180,R.drawable.minnie_mouse_back_bag));
        itemArrayList.add(new Item("Princesses back bag",180,R.drawable.princesses_back_bag));
        itemArrayList.add(new Item("Spider Man back bag",180,R.drawable.spider_man_back_bag));
        itemArrayList.add(new Item("Unicorn back bag",180,R.drawable.unicorn_back_bag));
        itemArrayList.add(new Item("Black lunch box",45,R.drawable.black_lunch_box));
        itemArrayList.add(new Item("Blue lunch box",45,R.drawable.blue_lunch_box));
        itemArrayList.add(new Item("Pink lunch box",45,R.drawable.pink_lunch_box));
        itemArrayList.add(new Item("Black pencil case",40,R.drawable.black_pencil_case));
        itemArrayList.add(new Item("Blue pencil case",40,R.drawable.blue_pencil_case));
        itemArrayList.add(new Item("Pink pencil case",40,R.drawable.pink_pencil_case));
        itemArrayList.add(new Item("Board pen",30,R.drawable.board_pen));
        itemArrayList.add(new Item("Highlighters",12,R.drawable.highlighters));
        itemArrayList.add(new Item("Pencils",10,R.drawable.pencils));
        itemArrayList.add(new Item("Black pens",6,R.drawable.black_pens));
        itemArrayList.add(new Item("Color pencils",12,R.drawable.color_pencils));
        itemArrayList.add(new Item("Marker pens",10,R.drawable.marker_pens));
        itemArrayList.add(new Item("Sharpner and eraser",5,R.drawable.sharpner_and_eraser));
        itemArrayList.add(new Item("Black eraser",20,R.drawable.black_eraser));
        itemArrayList.add(new Item("White eraser",20,R.drawable.white_eraser));
        itemArrayList.add(new Item("Metal bar",4,R.drawable.metal_bar));
        itemArrayList.add(new Item("Tape",8,R.drawable.tape));
        itemArrayList.add(new Item("Scissors",5,R.drawable.scissors));
        itemArrayList.add(new Item("Glue",6,R.drawable.glue));
        itemArrayList.add(new Item("Orange ring binder",25,R.drawable.orange_ring_binder));
        itemArrayList.add(new Item("Gray ring binder",25,R.drawable.gray_ring_binder));
        itemArrayList.add(new Item("Black ring binder",25,R.drawable.black_ring_binder));
        itemArrayList .add(new Item("Acrylic paint",50,R.drawable.acrylic_paint));
        itemArrayList.add(new Item("Glitter glue",20,R.drawable.glitter_glue));
        itemArrayList.add(new Item("Artist brushes",15,R.drawable.artist_brushes));
        itemArrayList.add(new Item("Wooden bulletin board",40,R.drawable.wooden_bulletin_board));
        itemArrayList.add(new Item("Wooden drawing stand",70,R.drawable.wooden_drawing_stand));
        itemArrayList.add(new Item("Artist tools",40,R.drawable.artist_tools));
        itemArrayList.add(new Item("Canvas painting board",14,R.drawable.canvas_painting_board));
        itemArrayList.add(new Item("Board with a stand",100,R.drawable.board_with_stand));
        itemArrayList.add(new Item("Stiky note",12,R.drawable.stiky_note));
        itemArrayList.add(new Item("Metal box",70,R.drawable.metal_box));
        itemArrayList.add(new Item("Small metal organizing paper shelf",30,R.drawable.small_metal_organizing_paper_shelf));
        itemArrayList.add(new Item("Metal mesh desk organizer",15,R.drawable.metal_mesh_desk_organizer));

    }

    @Override
    public Bitmap ByteArrayToBmp(byte[] b) {
        return (BitmapFactory.decodeByteArray(b, 0, b.length));
    }

    @Override
    public byte[] BmpToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return b;    }

}