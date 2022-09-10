package com.laacompany.infolet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.laacompany.infolet.picker.BalancePickerFragment;
import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;
import com.laacompany.infolet.wallet.Wallet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class InfoletWalletFragment extends Fragment {

    private static final String FILE_NAME = "wallet.txt";

    private static final String DIALOG_BALANCE = "dialog_balance";

    private static final int REQUEST_BALANCE = 2;

    private ImageButton mUpdateButton;
    private TextView mBalanceTextView;
    private TextView mIncomeTextView;
    private TextView mExpenseTextView;
    private TextView mDateTextView;
    private TextView mTimeTextView;

    int incomeCount = 0, expenseCount = 0;

    public static InfoletWalletFragment newInstance(){

        InfoletWalletFragment fragment = new InfoletWalletFragment();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_infolet_wallet,container,false);

        mUpdateButton = (ImageButton) v.findViewById(R.id.id_infolet_wallet_fragment_button_update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getFragmentManager();
                BalancePickerFragment dialog = BalancePickerFragment.newInstance(Integer.parseInt(loadFile()));
                dialog.setTargetFragment(InfoletWalletFragment.this, REQUEST_BALANCE);
                dialog.show(manager, DIALOG_BALANCE);

            }
        });

        TransactionLab transactionLab = TransactionLab.get(getActivity());
        List<Transaction> transactions = transactionLab.getTransactions("DESC");

        long lastTransactionTime = -1;



        for (Transaction transaction : transactions){
            if (transaction.isIncome()) {
                incomeCount += transaction.getPrice();
            } else {
                expenseCount += transaction.getPrice();
            }

            if (lastTransactionTime == -1 || lastTransactionTime < transaction.getDate().getTime()){
                lastTransactionTime = transaction.getDate().getTime();
            }
        }

        mBalanceTextView = (TextView) v.findViewById(R.id.id_infolet_wallet_fragment_balance_field);
        NumberFormat currencyFormat = new DecimalFormat("#,###");
        String currencyString = currencyFormat.format(incomeCount - expenseCount + Integer.parseInt(loadFile()));
        mBalanceTextView.setText(currencyString);

        mIncomeTextView = (TextView) v.findViewById(R.id.id_infolet_wallet_fragment_today_income_field);
        currencyFormat = new DecimalFormat("#,###");
        currencyString = currencyFormat.format(incomeCount);
        mIncomeTextView.setText(currencyString);

        mExpenseTextView = (TextView) v.findViewById(R.id.id_infolet_wallet_fragment_today_expense_field);
        currencyFormat = new DecimalFormat("#,###");
        currencyString = currencyFormat.format(expenseCount);
        mExpenseTextView.setText(currencyString);


        Date date = new Date();
        date.setTime(lastTransactionTime);


        mDateTextView = (TextView) v.findViewById(R.id.id_infolet_wallet_fragment_date_field);
        String dateFormat = "EEEE\nMMM dd, yyyy";
        String dateString = DateFormat.format(dateFormat,date).toString();
        mDateTextView.setText(dateString);


        mTimeTextView = (TextView) v.findViewById(R.id.id_infolet_wallet_fragment_time_field);
        String timeFormat = "HH : mm";
        String timeString = DateFormat.format(timeFormat,date).toString();
        mTimeTextView.setText(timeString);

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_BALANCE){
            int newBalance =  data.getIntExtra(BalancePickerFragment.EXTRA_SET_BALANCE,0);
            saveFile(newBalance);
            NumberFormat currencyFormat = new DecimalFormat("#,###");
            String currencyString = currencyFormat.format(incomeCount - expenseCount + Integer.parseInt(loadFile()));
            mBalanceTextView.setText(currencyString);
        }
    }

    private void saveFile(int balance){
        String text = String.valueOf(balance);
        FileOutputStream fos = null;

        try {
            fos = getActivity().openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String loadFile(){
        FileInputStream fis = null;

        StringBuilder sb = new StringBuilder();

        try {
            fis = getActivity().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String text;

            while ((text = br.readLine()) != null){
                sb.append(text);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String text = sb.toString();

        if (text.isEmpty()){
            saveFile(0);
            text = "0";
        }

        return text;
    }



}
