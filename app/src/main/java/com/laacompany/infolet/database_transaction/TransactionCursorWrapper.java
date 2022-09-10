package com.laacompany.infolet.database_transaction;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.laacompany.infolet.database_transaction.TransactionDBSchema.TransactionTable;
import com.laacompany.infolet.transaction.Transaction;

import java.util.Date;
import java.util.UUID;

public class TransactionCursorWrapper extends CursorWrapper {

    public TransactionCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Transaction getTransaction(){
        String uuidString = getString(getColumnIndex(TransactionTable.Cols.UUID));
        String title = getString(getColumnIndex(TransactionTable.Cols.TITLE));
        String category = getString(getColumnIndex(TransactionTable.Cols.CATEGORY));
        int categoryIndex = getInt(getColumnIndex(TransactionTable.Cols.CATEGORYINDEX));
        long date = getLong(getColumnIndex(TransactionTable.Cols.DATE));
        String type = getString(getColumnIndex(TransactionTable.Cols.TYPE));
        int price = getInt(getColumnIndex(TransactionTable.Cols.PRICE));
        String note = getString(getColumnIndex(TransactionTable.Cols.NOTE));


        Transaction transaction = new Transaction(UUID.fromString(uuidString));
        transaction.setName(title);
        transaction.setCategory(category);
        transaction.setCategoryIndex(categoryIndex);
        transaction.setDate(new Date(date));
        transaction.setType(type);
        transaction.setPrice(price);
        transaction.setNote(note);
        return transaction;
    }
}
