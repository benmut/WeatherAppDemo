package com.mtdengineering.weatherappdemo.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertUserDialog extends DialogFragment implements DialogInterface.OnClickListener
{
    private String message;
    private String settingsActivityAction;

    public AlertUserDialog(String message, String action)
    {
        this.message = message != null? message : "Message not set!";
        this.settingsActivityAction = action;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("Ok", this)
                .setNegativeButton("Cancel", this);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int i)
    {
        switch(i)
        {
            case Dialog.BUTTON_POSITIVE:
                break;
            case Dialog.BUTTON_NEGATIVE:
                // Do nothing
                break;
        }
    }
}
