package com.laacompany.infolet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class InfoletTransactionListFragment extends Fragment {

    private static final String TAG = "InfoletLog";

    private RecyclerView mTransactionRecyclerView;
    private TransactionAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infolet_transaction_list,container,false);

        mTransactionRecyclerView = (RecyclerView) view.findViewById(R.id.id_infolet_transaction_list_recycler_view);
        mTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_infolet_transaction_list, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_infolet_fragment_transaction_list_menu_new_transaction:
                Transaction transaction = new Transaction();
                TransactionLab.get(getActivity()).addTransaction(transaction);
                Intent intent = InfoletPagerActivity.newIntent(getActivity(), transaction.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private class TransactionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Transaction mTransaction;

        private TextView mTitleTextView;
        private TextView mCategoryTextView;
        private TextView mTypeTextView;
        private TextView mDateTextView;
        private TextView mPriceTextView;
        private TextView mTimeTextView;

        public TransactionHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_transaction, parent, false));

            mTitleTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_title);
            mCategoryTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_category);
            mTypeTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_type);
            mDateTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_date);
            mPriceTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_price);
            mTimeTextView = itemView.findViewById(R.id.id_list_item_transaction_fragment_textview_time);

            itemView.setOnClickListener(this);
        }

        public void bind(Transaction transaction){
            mTransaction = transaction;

            mTitleTextView.setText(mTransaction.getTitle());

            mTypeTextView.setText(mTransaction.getType());

            mCategoryTextView.setText(mTransaction.getCategory());

            String dateFormat = "EEE, MMM dd, yyyy";
            String dateString = DateFormat.format(dateFormat, mTransaction.getDate()).toString();

            mDateTextView.setText(dateString);

            String timeFormat = "HH : mm";
            String timeString = DateFormat.format(timeFormat, mTransaction.getDate()).toString();

            mTimeTextView.setText(timeString);

            NumberFormat currencyFormat = new DecimalFormat("#,###");
            String currencyString = "Rp. " + currencyFormat.format(mTransaction.getPrice());

            mPriceTextView.setText(currencyString);

        }

        @Override
        public void onClick(View v) {
            Intent intent = InfoletPagerActivity.newIntent(getActivity(), mTransaction.getId());
            startActivity(intent);
        }
    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionHolder>{

        private List<Transaction> mTransactions;

        public TransactionAdapter(List<Transaction> transactions){
            mTransactions = transactions;
        }

        @NonNull
        @Override
        public TransactionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                return new TransactionHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionHolder transactionHolder, int i) {
            Transaction transaction = mTransactions.get(i);
            transactionHolder.bind(transaction);
        }

        @Override
        public int getItemCount() {
            return mTransactions.size();
        }

        public void setTransactions(List<Transaction> transactions) {
            mTransactions = transactions;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

    private void updateUI(){
        TransactionLab transactionLab = TransactionLab.get(getActivity());
        List<Transaction> transactions = transactionLab.getTransactions("DESC");

        if (mAdapter == null) {
            mAdapter = new TransactionAdapter(transactions);
            mTransactionRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTransactions(transactions);
            mAdapter.notifyDataSetChanged();
        }



    }


}
