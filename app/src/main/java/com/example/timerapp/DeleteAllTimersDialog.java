package com.example.timerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteAllTimersDialog extends DialogFragment {
    private IDialogInteraction datable;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        datable = (IDialogInteraction) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String dialog = getArguments().getString("dialog");
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle(getResources().getString(R.string.Confirmation))
                .setMessage(dialog)
                .setPositiveButton(getResources().getString(R.string.Confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datable.remove("");
                    }
                })
                .setNegativeButton(getResources().getString(R.string.Cancel), null)
                .create();
    }
}
