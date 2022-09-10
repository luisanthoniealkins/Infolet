package com.laacompany.infolet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.laacompany.infolet.picker.DatePickerFragment;
import com.laacompany.infolet.picker.TimePickerFragment;
import com.laacompany.infolet.transaction.Transaction;
import com.laacompany.infolet.transaction.TransactionLab;

import java.util.Date;
import java.util.UUID;

public class InfoletPagerFragment extends Fragment{

    private static final String ARGS_TRANSACTION_ID = "args_transaction_id";
    private static final String DIALOG_DATE = "dialog_date";
    private static final String DIALOG_TIME = "dialog_time";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Transaction mTransaction;

    // VIEW
    private EditText mTitleEditText;
    private Spinner mCategorySpinner;
    private Button mDateButton;
    private Button mTimeButton;
    private Spinner mTypeSpinner;
    private EditText mPriceEditText;
    private EditText mNoteEditText;
    private Button mOKButton;


    ArrayAdapter<CharSequence> mAdapterCategory;

    public static InfoletPagerFragment newInstance(UUID transactionID){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TRANSACTION_ID,transactionID);

        InfoletPagerFragment fragment = new InfoletPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID transactionID = (UUID) getArguments().getSerializable(ARGS_TRANSACTION_ID);

        mTransaction = TransactionLab.get(getActivity()).getTransaction(transactionID);
        setHasOptionsMenu(true);
    }

    //DATABASE
    @Override
    public void onPause() {
        super.onPause();

        TransactionLab.get(getActivity()).updateTransaction(mTransaction);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_infolet_transaction_pager, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.id_infolet_fragment_transaction_pager_delete_transaction:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setCancelable(true);
                builder.setTitle("Delete Transaction");
                builder.setMessage("Are you sure you want to delete?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TransactionLab.get(getActivity()).deleteTransaction(mTransaction);
                        getActivity().finish();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_infolet_transaction_pager,container,false);

        mTitleEditText = v.findViewById(R.id.id_infolet_fragment_edittext_title);
        mTitleEditText.setText(mTransaction.getTitle());
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTransaction.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTypeSpinner = v.findViewById(R.id.id_infolet_fragment_spinner_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);

        int posType = 0;
        String[] stringType = getResources().getStringArray(R.array.transaction_type);
        for (int i = 0; i < stringType.length; i++){
            if (stringType[i].equals(mTransaction.getType())){
                posType = i;
                break;
            }
        }

        mTypeSpinner.setSelection(posType);

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = parent.getItemAtPosition(position).toString();
                if (!type.equals(mTransaction.getType())){

                    mTransaction.setType(type);

                    mTransaction.setCategoryIndex(0);
                    if (mTransaction.isIncome()){
                        mAdapterCategory = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_category_income, android.R.layout.simple_spinner_item);
                    } else {
                        mAdapterCategory = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_category_expense, android.R.layout.simple_spinner_item);
                    }
                    setCategoryText();
                    mCategorySpinner.setAdapter(mAdapterCategory);
                    mCategorySpinner.setSelection(0);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCategorySpinner = v.findViewById(R.id.id_infolet_fragment_spinner_category);
        if (posType == 0){
            mAdapterCategory = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_category_income, android.R.layout.simple_spinner_item);
        } else {
            mAdapterCategory = ArrayAdapter.createFromResource(getActivity(),R.array.transaction_category_expense, android.R.layout.simple_spinner_item);
        }
        mAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(mAdapterCategory);

        int posCategory = mTransaction.getCategoryIndex();

        mCategorySpinner.setSelection(posCategory);

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTransaction.setCategoryIndex(position);
                setCategoryText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDateButton = v.findViewById(R.id.id_infolet_fragment_button_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mTransaction.getDate());
                dialog.setTargetFragment(InfoletPagerFragment.this,REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mTimeButton = v.findViewById(R.id.id_infolet_fragment_button_time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mTransaction.getDate());
                dialog.setTargetFragment(InfoletPagerFragment.this,REQUEST_TIME);
                dialog.show(manager,DIALOG_TIME);
            }
        });

        updateDate();



        mPriceEditText = v.findViewById(R.id.id_infolet_fragment_edittext_price);
        mPriceEditText.setText(String.valueOf(mTransaction.getPrice()));
        mPriceEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    mTransaction.setPrice(0);
                } else {
                    mTransaction.setPrice(Integer.parseInt(s.toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mNoteEditText = (EditText) v.findViewById(R.id.id_infolet_wallet_edittext_note);
        mNoteEditText.setText(mTransaction.getNote());
        mNoteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTransaction.setNote(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mOKButton = v.findViewById(R.id.id_infolet_fragment_button_ok);
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTransaction.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mTransaction.setDate(date);
            updateDate();
        }
    }

    private void updateDate() {

        String dateFormat = "EEEE, MMM dd, yyyy";
        String dateString = DateFormat.format(dateFormat, mTransaction.getDate()).toString();

        mDateButton.setText(dateString);

        String timeFormat = "HH : mm";
        String timeString = DateFormat.format(timeFormat, mTransaction.getDate()).toString();

        mTimeButton.setText(timeString);
    }

    private void setCategoryText(){
        String[] stringType;
        if (mTransaction.isIncome()){
            stringType = getResources().getStringArray(R.array.transaction_category_income);
            mTransaction.setCategory(stringType[mTransaction.getCategoryIndex()]);
        } else {
            stringType = getResources().getStringArray(R.array.transaction_category_expense);
            mTransaction.setCategory(stringType[mTransaction.getCategoryIndex()]);
        }

    }

}
