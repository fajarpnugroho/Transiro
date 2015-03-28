package com.seventh.transiro.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;

import com.seventh.transiro.R;
import com.seventh.transiro.helper.LoadingDialogInterface;

public abstract class ActivityWithLoadingDialog extends ActionBarActivity
        implements LoadingDialogInterface {
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = setupProgressDialog();

        setActionBar();
    }

    private void setActionBar() {
        if (getSupportActionBar() != null) { getSupportActionBar().hide(); }
    }

    private Dialog setupProgressDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onLoadingDialogCancel();
            }
        });
        return dialog;
    }

    @Override
    public void showLoadingDialog() { if (dialog != null) { dialog.show(); } }

    @Override
    public void hideLoadingDialog() { if (dialog != null) { dialog.dismiss(); }}

    @Override
    protected void onDestroy() {
        if (dialog != null) { dialog.dismiss(); }
        super.onDestroy();
    }
}
