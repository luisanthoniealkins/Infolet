package com.laacompany.infolet.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.laacompany.infolet.database_transaction.TransactionBaseHelper;
import com.laacompany.infolet.database_transaction.TransactionCursorWrapper;
import com.laacompany.infolet.database_transaction.TransactionDBSchema.TransactionTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionLab {

    /**

     DATABASE

     */
    private static TransactionLab sTransactionLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TransactionLab get(Context context){
        if (sTransactionLab == null){
            sTransactionLab = new TransactionLab(context);
        }

        return sTransactionLab;
    }


    public TransactionLab(Context context){
        mContext = context;
        mDatabase = new TransactionBaseHelper(mContext).getWritableDatabase();

    }

    public List<Transaction> getTransactions(String sort){

        List<Transaction> transactions = new ArrayList<>();

        TransactionCursorWrapper cursor = queryTransactions(null,null, sort);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                transactions.add(cursor.getTransaction());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return transactions;
    }

    public Transaction getTransaction(UUID mId){

        TransactionCursorWrapper cursor = queryTransactions(TransactionTable.Cols.UUID + " = ?", new String[] {mId.toString()}, "DESC");

        try {
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTransaction();
        } finally {
            cursor.close();
        }

    }

    public void addTransaction(Transaction transaction){
        ContentValues values = getContentValues(transaction);

        mDatabase.insert(TransactionTable.NAME,null,values);

    }

    public void deleteTransaction(Transaction transaction){

        mDatabase.delete(TransactionTable.NAME,TransactionTable.Cols.UUID+ " = ?", new String[]{transaction.getId().toString()});
    }


    //DATABASE
    private static ContentValues getContentValues(Transaction transaction){
        ContentValues values = new ContentValues();

        values.put(TransactionTable.Cols.UUID, transaction.getId().toString());
        values.put(TransactionTable.Cols.TITLE, transaction.getTitle());
        values.put(TransactionTable.Cols.CATEGORY, transaction.getCategory());
        values.put(TransactionTable.Cols.CATEGORYINDEX, transaction.getCategoryIndex());
        values.put(TransactionTable.Cols.DATE, transaction.getDate().getTime());
        values.put(TransactionTable.Cols.TYPE, transaction.getType());
        values.put(TransactionTable.Cols.PRICE, String.valueOf(transaction.getPrice()));
        values.put(TransactionTable.Cols.NOTE,transaction.getNote());

        return values;

    }

    public void updateTransaction(Transaction transaction){
        String uuidString = transaction.getId().toString();
        ContentValues values = getContentValues(transaction);

        mDatabase.update(TransactionTable.NAME, values, TransactionTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private TransactionCursorWrapper queryTransactions(String whereClause, String[] whereArgs, String sort) {
        Cursor cursor = mDatabase.query(
                TransactionTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                "date " + sort // orderBy
        );
        return new TransactionCursorWrapper(cursor);
    }

    public List<Transaction> getTransactions(String type, String sort){

        List<Transaction> transactions = new ArrayList<>();

        TransactionCursorWrapper cursor = queryTransactions(TransactionTable.Cols.TYPE + " = ?", new String[] {type}, sort);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                transactions.add(cursor.getTransaction());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return transactions;
    }



}
