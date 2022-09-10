package com.laacompany.infolet.transaction;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private String mName;
    private String mCategory;
    private int mCategoryIndex;
    private Date mDate;
    private boolean mIncome;
    private boolean mExpense;
    private int mPrice;
    private String mNote;

    public Transaction(){
        this(UUID.randomUUID());
    }

    public Transaction(UUID id){
        mId = id;
        mDate = new Date();
        mPrice = 0;
        mIncome = true;
        mExpense = false;
        mCategoryIndex = 0;
    }


    //GETTERS AND SETTERS
    private UUID mId;

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public int getCategoryIndex() {
        return mCategoryIndex;
    }

    public void setCategoryIndex(int categoryIndex) {
        mCategoryIndex = categoryIndex;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public void setType(String string){
        if (string.equals("Income")){
            mIncome = true;
            mExpense = false;
        } else if (string.equals("Expense")){
            mIncome = false;
            mExpense = true;
        }
    }

    public String getType(){
        if (isIncome()){
            return "Income";
        } else if (isExpense()){
            return "Expense";
        }
        return "Income";
    }

    public boolean isIncome() {
        return mIncome;
    }

    public boolean isExpense() {
        return mExpense;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String note) {
        mNote = note;
    }

    public Date getFormattedDate(){
        String dateFormat = "MMM dd yyyy";
        String dateString = DateFormat.format(dateFormat, getDate()).toString();

        SimpleDateFormat f = new SimpleDateFormat("MMM dd yyyy");

        Date d = null;
        try {
            d = f.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }
}

