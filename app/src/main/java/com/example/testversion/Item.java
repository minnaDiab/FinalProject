package com.example.testversion;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class Item {
    protected String name;
    protected double price;
    protected int amount;
    private int imageId;

    public Item(){
    }


    public Item(String name, double price, int image) {
           this.name = name;
           this.price = price;
           this.amount = 0;
           this.imageId = image;
    }
    public Item(String name, double price, int amount,int image) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.imageId = image;
    }


    public String getName() {
          return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getAmount() {
        return this.amount;
    }

      public int getImage() {return this.imageId;}

      public void setName(String name){
        this.name=name;
      }

      public void setPrice(double price){
        this.price=price;
      }

      public void setAmount(int amount){
        this.amount=amount;
      }

      public void setImage(int image){
        this.imageId=image;
      }

    public Map<String,Object> ItemToHashMap(){
        Map<String,Object> it =new HashMap<>();
        it.put("Name",this.name);
        it.put("price",this.price);
        it.put("Amount",this.amount);
        return it;
    }

    public Item maptoItem(Map<String,Object> map){
        Item tmpItem=new Item();
        tmpItem.setName(map.get("Name").toString());
        tmpItem.setPrice(Double.parseDouble(map.get("Price").toString()));
        tmpItem.setAmount(Integer.parseInt(map.get("Amount").toString()));
        return tmpItem;
    }

}


