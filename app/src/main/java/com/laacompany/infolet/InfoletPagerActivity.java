package com.laacompany.infolet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;

import java.util.List;
import java.util.UUID;

public class InfoletPagerActivity extends AppCompatActivity {

    private String TAG = "InfoletTag";

    private static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    private ViewPager mViewPager;
    private List<Transaction> mTransactions;

    public static Intent newIntent(Context packageContext, UUID transactionId){
        Intent intent = new Intent(packageContext,InfoletPagerActivity.class);
        intent.putExtra(EXTRA_TRANSACTION_ID,transactionId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infolet_pager);
        UUID transactionID = (UUID) getIntent().getSerializableExtra(EXTRA_TRANSACTION_ID);

        mViewPager = (ViewPager) findViewById(R.id.id_infolet_view_pager);

        mTransactions = TransactionLab.get(this).getTransactions("DESC");


        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Transaction transaction = mTransactions.get(i);
                return InfoletPagerFragment.newInstance(transaction.getId());
            }

            @Override
            public int getCount() {
                return mTransactions.size();
            }
        });

        for (int i = 0; i < mTransactions.size(); i++) {
            if (transactionID.equals(mTransactions.get(i).getId())){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
