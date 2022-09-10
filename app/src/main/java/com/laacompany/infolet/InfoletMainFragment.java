package com.laacompany.infolet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class InfoletMainFragment extends Fragment {

    private static final String FILE_NAME = "wallet.txt";

    private TextView mBalanceTextView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_infolet_main_alternative,container,false);

        mBalanceTextView = v.findViewById(R.id.id_infolet_fragment_main_balance);
        mBalanceTextView.setText(getBalance());

        return v;

    }

    public String getBalance(){

        List<Transaction> transactions = TransactionLab.get(getActivity()).getTransactions("DESC");

        int incomeCount = 0, expenseCount = 0;

        for (Transaction transaction : transactions){
            if (transaction.isIncome()) {
                incomeCount += transaction.getPrice();
            } else {
                expenseCount += transaction.getPrice();
            }
        }

        int balance = Integer.parseInt(loadFile()) + incomeCount - expenseCount;

        NumberFormat currencyFormat = new DecimalFormat("#,###");
        String currencyString = currencyFormat.format(balance);

        return currencyString;
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

}
