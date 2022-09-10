package com.laacompany.infolet.picker;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.laacompany.infolet.R;

public class BalancePickerFragment extends AppCompatDialogFragment {

    public static final String EXTRA_SET_BALANCE = "extra_set_balance";

    private static final String ARG_BALANCE = "arg_balance";

    private TextView mOldBalance;
    private EditText mNewBalance;

    public static BalancePickerFragment newInstance(int oldBalance){
        Bundle args = new Bundle();
        args.putInt(ARG_BALANCE, oldBalance);

        BalancePickerFragment fragment = new BalancePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_balance,null);

        int oldBalance = getArguments().getInt(ARG_BALANCE);

        mOldBalance = v.findViewById(R.id.id_infolet_dialog_old_balance_textview_field);
        mOldBalance.setText(String.valueOf(oldBalance));

        mNewBalance = v.findViewById(R.id.id_infolet_dialog_new_balance_edittext);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.balance_picker_title)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int newBalance = Integer.parseInt(mNewBalance.getText().toString());
                        sendResult(Activity.RESULT_OK,newBalance);
                    }
                })
                .show();
    }

    private void sendResult(int resultCode, int newBalance){
        if (getTargetFragment()==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SET_BALANCE, newBalance);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
