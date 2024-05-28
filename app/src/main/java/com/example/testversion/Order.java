package com.example.testversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private static int countOrder=0;
    protected int orderNum;
    protected String custNum;
    protected String orderDate;
    protected ArrayList<Item> items;
    protected double sumOfOrder;


    public Order(){}


    public Order(String custNum, String orderDate, double sumOfOrder){
        this.countOrder++;
        this.orderNum=countOrder;
        this.custNum=custNum;
        this.orderDate=orderDate;
        this.sumOfOrder=sumOfOrder;
        this.items=new ArrayList<>();
    }

    public Order(String custNum, String orderDate, double sumOfOrder, ArrayList<Item> items){
        this.countOrder++;
        this.orderNum=countOrder;
        this.custNum=custNum;
        this.orderDate=orderDate;
        this.sumOfOrder=sumOfOrder;
        this.items=items;
    }


    public String getCustNum() {
        return custNum;
    }

    public void setCustNum(String custNum) {
        this.custNum = custNum;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getSumOfOrder() {
        return sumOfOrder;
    }

    public void setSumOfOrder(double sumOfOrder) {
        this.sumOfOrder = sumOfOrder;
    }

    public int getOrderNum (){ return this.orderNum; }

    public void setOrderNum(int orderNum){ this.orderNum=orderNum; }

    public Map<String,Object> OrderToHashMap(){
        Map<String,Object> or =new HashMap<>();
        or.put("Order Num",this.orderNum);
        or.put("Cust Num",this.custNum);
        or.put("Items",this.items);
        or.put("Order Date",this.orderDate);
        or.put("Sum of Order",this.sumOfOrder);
        return or;
    }

    public Order maptoOrder(Map<String,Object> map){
        Order tmpOrder=new Order();
        tmpOrder.setOrderNum(Integer.parseInt(map.get("Order Num").toString()));
        tmpOrder.setCustNum(map.get("Cust Num").toString());
        tmpOrder.setOrderDate(map.get("Bill Date").toString());
        tmpOrder.setSumOfOrder(Integer.parseInt(map.get("Sum Of Bill").toString()));
        tmpOrder.setItems((ArrayList<Item>) map.get("Items"));
        return tmpOrder;
    }

}
