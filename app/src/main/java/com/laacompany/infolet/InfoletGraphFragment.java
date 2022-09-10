package com.laacompany.infolet;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;
import com.laacompany.infolet.transaction.TransactionPack;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InfoletGraphFragment extends Fragment {


    private CheckBox mIncomeCheckBox;
    private CheckBox mExpenseCheckBox;
    private CheckBox mBalanceCheckBox;

    private LineChart mLineChart;
    private PieChart mIncomePieChart;
    private PieChart mExpensePieChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infolet_graph, container,false);

        List<Transaction> transactions = TransactionLab.get(getActivity()).getTransactions("ASC");
        List<TransactionPack> transactionPacks = new ArrayList<>();

        if (transactions.size() > 0){

            Date date = null;
            TransactionPack transactionPack = new TransactionPack(0,0);
            transactionPack.setDate(transactions.get(0).getFormattedDate());

            for (int i = 0; i < transactions.size(); i++){
                if (i == 0 || transactions.get(i).getFormattedDate().equals(date)){
                    if (transactions.get(i).isIncome()){
                        transactionPack.setTotalIncome(transactions.get(i).getPrice() + transactionPack.getTotalIncome());
                    } else {
                        transactionPack.setTotalExpense(transactions.get(i).getPrice() + transactionPack.getTotalExpense());
                    }
                } else {
                    transactionPacks.add(transactionPack);
                    date = transactions.get(i).getFormattedDate();

                    if (transactions.get(i).isIncome()){
                        transactionPack = new TransactionPack(transactions.get(i).getPrice(),0);
                    } else {
                        transactionPack = new TransactionPack(0, transactions.get(i).getPrice());
                    }
                    transactionPack.setDate(transactions.get(i).getFormattedDate());
                }
            }
            transactionPacks.add(transactionPack);


        }


        /**
         * LINE CHART
         */

        List<Entry> incomeEntries = new ArrayList<Entry>() , expenseEntries = new ArrayList<Entry>();

        int index = 0;
        for (TransactionPack transactionPack : transactionPacks){
            // turn your data into Entry objects
            incomeEntries.add(new Entry(index, transactionPack.getTotalIncome()));
            expenseEntries.add(new Entry(index, transactionPack.getTotalExpense()));
            index++;
        }


        mLineChart = (LineChart) view.findViewById(R.id.id_infolet_line_chart);

        mLineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });

        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });



        LineDataSet dataSetIncome = new LineDataSet(incomeEntries, "Income"); // add entries to dataset
        dataSetIncome.setColor(Color.RED);
        dataSetIncome.setValueTextColor(Color.RED); // styling, ...

        LineDataSet dataSetExpense = new LineDataSet(expenseEntries, "Expense"); // add entries to dataset
        dataSetExpense.setColor(Color.BLUE);
        dataSetExpense.setValueTextColor(Color.BLUE); // styling, ...

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetIncome);
        dataSets.add(dataSetExpense);

        LineData lineData = new LineData(dataSets);
        mLineChart.setData(lineData);

        mLineChart.invalidate(); // refresh


        mIncomeCheckBox = view.findViewById(R.id.id_infolet_graph_fragment_checkbox_income);
        mIncomeCheckBox.setChecked(true);
        mIncomeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mExpenseCheckBox = view.findViewById(R.id.id_infolet_graph_fragment_checkbox_expense);
        mExpenseCheckBox.setChecked(true);
        mExpenseCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        mBalanceCheckBox = view.findViewById(R.id.id_infolet_graph_fragment_checkbox_balance);
        mBalanceCheckBox.setChecked(true);
        mBalanceCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        String categotyDataIncome[] = getResources().getStringArray(R.array.transaction_category_income);
        String categotyDataExpense[] = getResources().getStringArray(R.array.transaction_category_expense);

        TransactionPack transactionPack = new TransactionPack(categotyDataIncome,categotyDataExpense);

        for (Transaction transaction : transactions){
            if (transaction.isIncome()){
                for (int i = 0; i < categotyDataIncome.length; i++){
                    if (categotyDataIncome[i].equals(transaction.getCategory())){
                        transactionPack.setCategoryIncomePrice(transaction.getPrice() + transactionPack.getCategoryIncomePrice()[i],i);
                        break;
                    }
                }
            } else {
                for (int i = 0; i < categotyDataExpense.length; i++){
                    if (categotyDataExpense[i].equals(transaction.getCategory())){
                        transactionPack.setCategoryExpensePrice(transaction.getPrice() + transactionPack.getCategoryExpensePrice()[i],i);
                        break;
                    }
                }
            }
        }


        /*
         * INCOME PIE CHART
         */

        mIncomePieChart = view.findViewById(R.id.id_infolet_income_pie_chart);

        mIncomePieChart.setUsePercentValues(true);
        mIncomePieChart.getDescription().setEnabled(false);
        mIncomePieChart.setExtraOffsets( 5,10,5,10);

        mIncomePieChart.setDragDecelerationFrictionCoef(0.99f);

        mIncomePieChart.setDrawHoleEnabled(true);
        mIncomePieChart.setHoleColor(Color.WHITE);
        mIncomePieChart.setTransparentCircleRadius(45f);

        mIncomePieChart.animateY(1000, Easing.EaseInOutCubic );


        ArrayList<PieEntry> value = new ArrayList<>();


        for (int i = 0; i < categotyDataIncome.length; i++){
            if (transactionPack.getCategoryIncomePrice()[i]!=0){
                value.add(new PieEntry(transactionPack.getCategoryIncomePrice()[i], transactionPack.getCategoryDataIncome()[i]));
            }
        }

        PieDataSet dataSet = new PieDataSet(value,"Category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(15f);
        dataSet.setColors(getResources().getIntArray(R.array.color_array));


        PieData data = new PieData(dataSet);
        data.setValueTextSize(10);
        data.setValueTextColor(Color.BLACK);



        mIncomePieChart.setData(data);
        mIncomePieChart.invalidate();


        /*
         * INCOME PIE CHART
         */

        mExpensePieChart = view.findViewById(R.id.id_infolet_expense_pie_chart);

        mExpensePieChart.setUsePercentValues(true);
        mExpensePieChart.getDescription().setEnabled(false);
        mExpensePieChart.setExtraOffsets( 5,10,5,10);

        mExpensePieChart.setDragDecelerationFrictionCoef(0.99f);

        mExpensePieChart.setDrawHoleEnabled(true);
        mExpensePieChart.setHoleColor(Color.WHITE);
        mExpensePieChart.setTransparentCircleRadius(45f);

        mExpensePieChart.animateY(1000, Easing.EaseInOutCubic );


        value = new ArrayList<>();

        for (int i = 0; i < categotyDataExpense.length; i++){
            if (transactionPack.getCategoryExpensePrice()[i]!=0){
                value.add(new PieEntry(transactionPack.getCategoryExpensePrice()[i], transactionPack.getCategoryDataExpense()[i]));
            }
        }

        dataSet = new PieDataSet(value,"Category");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(15f);
        dataSet.setColors(getResources().getIntArray(R.array.color_array));


        data = new PieData(dataSet);
        data.setValueTextSize(10);
        data.setValueTextColor(Color.BLACK);



        mExpensePieChart.setData(data);
        mExpensePieChart.invalidate();

        return view;
    }

    

}

