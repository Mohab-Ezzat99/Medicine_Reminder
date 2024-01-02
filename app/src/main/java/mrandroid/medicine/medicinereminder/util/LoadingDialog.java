package mrandroid.medicine.medicinereminder.util;

import android.app.ProgressDialog;
import android.content.Context;
import mrandroid.medicine.medicinereminder.R;

public class LoadingDialog {

    private ProgressDialog progressDialog;

    public LoadingDialog(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public void display() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_loading);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismiss() {
        progressDialog.dismiss();
    }
}
