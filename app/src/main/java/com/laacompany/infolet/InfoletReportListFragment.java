package com.laacompany.infolet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.laacompany.infolet.R;
import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;
import com.laacompany.infolet.transaction.TransactionPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoletReportListFragment extends Fragment {

    private RecyclerView mTransactionReportRecyclerView;
    private TransactionReportAdapter mAdapter;

    List<TransactionPack> mTransactionPacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infolet_report_list, container, false);

        mTransactionReportRecyclerView = view.findViewById(R.id.id_infolet_report_list_recycler_view);
        mTransactionReportRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        processTransactionPacks();

        updateUI();

        return view;
    }



    private class TransactionReportHolder extends RecyclerView.ViewHolder{

        LinearLayout mIncomeLinearLayout;
        LinearLayout mExpenseLinearLayout;
        TextView mDateTextView;
        TextView mTotalIncomeTextView;
        TextView mTotalExpenseTextView;


        public TransactionReportHolder(LayoutInflater inflater, @NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_report,parent,false));

            mIncomeLinearLayout = itemView.findViewById(R.id.id_infolet_report_list_income_linear_layout);
            mExpenseLinearLayout = itemView.findViewById(R.id.id_infolet_report_list_expense_linear_layout);
            mDateTextView = itemView.findViewById(R.id.id_infolet_report_list_date);
            mTotalIncomeTextView = itemView.findViewById(R.id.id_infolet_report_list_total_income);
            mTotalExpenseTextView = itemView.findViewById(R.id.id_infolet_report_list_total_expense);
        }

        public void bind(TransactionPack transactionPack){

            String dateFormat = "EEE, MMM dd, yyyy";
            String dateString = DateFormat.format(dateFormat, transactionPack.getDate()).toString();

            mDateTextView.setText(dateString);

            mTotalIncomeTextView.setText(String.valueOf(transactionPack.getTotalIncome2()));
            mTotalExpenseTextView.setText(String.valueOf(transactionPack.getTotalExpense2()));

            int categories = transactionPack.getCategoryDataIncome().length;
            boolean none = true;
            for (int i = 0; i < categories; i++){

                int price = transactionPack.getCategoryIncomePrice()[i];

                if (price > 0){
                    View child = getLayoutInflater().inflate(R.layout.list_item_report_category,null);
                    TextView mCategoryName = child.findViewById(R.id.id_list_item_report_category_title);
                    TextView mCategoryPrice = child.findViewById(R.id.id_list_item_report_category_price);
                    mCategoryName.setText(transactionPack.getCategoryDataIncome()[i]);
                    mCategoryPrice.setText(String.valueOf(price));
                    mIncomeLinearLayout.addView(child);
                    none = false;
                }

                if(i == categories-1 && none == true){
                    TextView tv1 = new TextView(getActivity());
                    tv1.setText("No Income");
                    tv1.setTextSize(12);
                    mIncomeLinearLayout.addView(tv1);
                }
            }



            categories = transactionPack.getCategoryDataExpense().length;
            none = true;
            for (int i = 0; i < categories; i++){

                int price = transactionPack.getCategoryExpensePrice()[i];

                if (price > 0){
                    View child = getLayoutInflater().inflate(R.layout.list_item_report_category,null);
                    TextView mCategoryName = child.findViewById(R.id.id_list_item_report_category_title);
                    TextView mCategoryPrice = child.findViewById(R.id.id_list_item_report_category_price);
                    mCategoryName.setText(transactionPack.getCategoryDataExpense()[i]);
                    mCategoryPrice.setText(String.valueOf(price));
                    mExpenseLinearLayout.addView(child);
                    none = false;
                }

                if(i == categories-1 && none == true){
                    TextView tv1 = new TextView(getActivity());
                    tv1.setText("No Expense");
                    tv1.setTextSize(12);
                    mExpenseLinearLayout.addView(tv1);
                }
            }
        }
    }

    private class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportHolder>{

        List<TransactionPack> mTransactionPacks;

        public TransactionReportAdapter(List<TransactionPack> transactionPacks){
            mTransactionPacks = transactionPacks;
        }

        @NonNull
        @Override
        public TransactionReportHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new TransactionReportHolder(inflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionReportHolder transactionReportHolder, int i) {
            transactionReportHolder.bind(mTransactionPacks.get(i));
        }

        @Override
        public int getItemCount() {
            return mTransactionPacks.size();
        }
    }

    private void updateUI(){

        mAdapter = new TransactionReportAdapter(mTransactionPacks);
        mTransactionReportRecyclerView.setAdapter(mAdapter);
    }

    private void processTransactionPacks(){

        String categoryDataIncome[] = getResources().getStringArray(R.array.transaction_category_income);
        String categoryDataExpense[] = getResources().getStringArray(R.array.transaction_category_expense);

        List<Transaction> transactions = TransactionLab.get(getActivity()).getTransactions("DESC");
        mTransactionPacks = new ArrayList<>();

        if (transactions.size()>0){

            Date currentDate = null;
            TransactionPack transactionPack = null;

            int transactionSize = transactions.size();
            for (int j = 0; j < transactionSize; j++) {

                if (currentDate == null || !transactions.get(j).getFormattedDate().equals(currentDate)) {
                    currentDate = transactions.get(j).getFormattedDate();
                    transactionPack = new TransactionPack(categoryDataIncome, categoryDataExpense);
                    transactionPack.setDate(currentDate);
                }

                if (transactions.get(j).isIncome()) {
                    for (int i = 0; i < categoryDataIncome.length; i++) {
                        if (categoryDataIncome[i].equals(transactions.get(j).getCategory())) {
                            transactionPack.setCategoryIncomePrice(transactions.get(j).getPrice() + transactionPack.getCategoryIncomePrice()[i], i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < categoryDataExpense.length; i++) {
                        if (categoryDataExpense[i].equals(transactions.get(j).getCategory())) {
                            transactionPack.setCategoryExpensePrice(transactions.get(j).getPrice() + transactionPack.getCategoryExpensePrice()[i], i);
                            break;
                        }
                    }
                }

                if (j == transactionSize - 1 || !transactions.get(j + 1).getFormattedDate().equals(currentDate)) {
                    mTransactionPacks.add(transactionPack);
                }
            }
        }

    }

}

