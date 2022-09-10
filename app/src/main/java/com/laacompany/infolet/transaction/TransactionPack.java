package com.laacompany.infolet.transaction;

import java.util.Date;

public class TransactionPack {

    private int mTotalIncome;
    private int mTotalExpense;
    private Date mDate;
    private String mCategoryDataIncome[];
    private String mCategoryDataExpense[];
    private int mCategoryIncomePrice[];
    private int mCategoryExpensePrice[];

    public TransactionPack(int initIncome, int initExpense)
    {
        mTotalIncome = initIncome;
        mTotalExpense = initExpense;
    }

    public TransactionPack(String[] categotyDataIncome, String[] categotyDataExpense){
        this.mCategoryDataIncome = categotyDataIncome;
        this.mCategoryDataExpense = categotyDataExpense;
        mCategoryIncomePrice = new int[categotyDataIncome.length];
        mCategoryExpensePrice = new int[categotyDataExpense.length];
    }

    public int getTotalIncome() {
        return mTotalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        mTotalIncome = totalIncome;
    }

    public int getTotalExpense() {
        return mTotalExpense;
    }

    public void setTotalExpense(int totalExpense) {
        mTotalExpense = totalExpense;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public int[] getCategoryIncomePrice() {
        return mCategoryIncomePrice;
    }

    public void setCategoryIncomePrice(int categoryIncomePrice, int index) {
        mCategoryIncomePrice[index] = categoryIncomePrice;
    }

    public int[] getCategoryExpensePrice() {
        return mCategoryExpensePrice;
    }

    public void setCategoryExpensePrice(int categoryExpensePrice, int index) {
        mCategoryExpensePrice[index] = categoryExpensePrice;
    }

    public String[] getCategoryDataIncome() {
        return mCategoryDataIncome;
    }

    public String[] getCategoryDataExpense() {
        return mCategoryDataExpense;
    }

    public int getTotalIncome2() {
        int incomes = 0;
        for (int income : mCategoryIncomePrice){
            incomes += income;
        }
        return incomes;
    }

    public int getTotalExpense2() {
        int expenses = 0;
        for (int expense : mCategoryExpensePrice){
            expenses += expense;
        }
        return expenses;
    }
}
