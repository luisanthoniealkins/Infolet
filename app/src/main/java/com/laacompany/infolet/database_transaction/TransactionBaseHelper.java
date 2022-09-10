package com.laacompany.infolet.database_transaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.laacompany.infolet.database_transaction.TransactionDBSchema.TransactionTable;

public class TransactionBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "transactionBase.db";

    public TransactionBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TransactionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TransactionTable.Cols.UUID + ", " +
                TransactionTable.Cols.TITLE + ", " +
                TransactionTable.Cols.CATEGORY + ", " +
                TransactionTable.Cols.CATEGORYINDEX + ", " +
                TransactionTable.Cols.DATE + ", " +
                TransactionTable.Cols.TYPE + ", " +
                TransactionTable.Cols.PRICE + ", " +
                TransactionTable.Cols.NOTE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
