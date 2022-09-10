package com.laacompany.infolet.wallet;

import java.util.Date;
import java.util.UUID;

public class Wallet {

    UUID mID;
    int mBalance;
    int mIncome;
    int mExpense;
    Date mDate;

    public Wallet(){
        mBalance = 0;
        mIncome = 0;
        mExpense = 0;
        mDate = new Date();
    }

    public UUID getID() {
        return mID;
    }

    public void setID(UUID ID) {
        mID = ID;
    }

    public int getBalance() {
        return mBalance;
    }

    public void setBalance(int balance) {
        this.mBalance = balance;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int getIncome() {
        return mIncome;
    }

    public void setIncome(int income) {
        mIncome = income;
    }

    public int getExpense() {
        return mExpense;
    }

    public void setExpense(int expense) {
        mExpense = expense;
    }
}
