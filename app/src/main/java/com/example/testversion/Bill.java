package com.example.testversion;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bill {
    private static int countBill=0;
    protected int billNum;
    protected String custNum;
    protected String billDate;
    protected ArrayList<Item> items;
    protected double sumOfBill;

    public Bill(){}

    public Bill(String custNum, String billDate, double sumOfBill){
        this.countBill ++;
        this.billNum=countBill;
        this.custNum=custNum;
        this.billDate=billDate;
        this.sumOfBill=sumOfBill;
        this.items=new ArrayList<>();
    }

    public Bill(String custNum, String billDate, double sumOfBill, ArrayList<Item> items){
        this.countBill ++;
        this.billNum=countBill;
        this.custNum=custNum;
        this.billDate=billDate;
        this.sumOfBill=sumOfBill;
        this.items=items;
    }

    public String getCustNum() {
        return custNum;
    }

    public void setCustNum(String custNum) {
        this.custNum = custNum;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public double getSumOfBill() {
        return sumOfBill;
    }

    public void setSumOfBill(double sumOfBill) {
        this.sumOfBill = sumOfBill;
    }

    public int getBillNum(){ return this.billNum; }

    public void setBillNum(int billNum){ this.billNum=billNum; }

    public Map<String,Object> BillToHashMap(){
        Map<String,Object> bill =new HashMap<>();
        bill.put("Bill Num",this.billNum);
        bill.put("Cust Num",this.custNum);
        bill.put("Bill Date",this.billDate);
        bill.put("Items",this.items);
        bill.put("Sum Of Bill",this.sumOfBill);
        return bill;
    }

    public Bill maptoBill(Map<String,Object> map){
        Bill tmpBill=new Bill();
        tmpBill.setBillNum(Integer.parseInt(map.get("Bill Num").toString()));
        tmpBill.setCustNum(map.get("Cust Num").toString());
        tmpBill.setBillDate(map.get("Bill Date").toString());
        tmpBill.setSumOfBill(Integer.parseInt(map.get("Sum Of Bill").toString()));
        tmpBill.setItems((ArrayList<Item>) map.get("Items"));
        return tmpBill;
    }


}
