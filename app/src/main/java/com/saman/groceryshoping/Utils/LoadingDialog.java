package com.saman.groceryshopping.Utils;

import android.app.Dialog;
import android.content.Context;

import com.saman.groceryshopping.R;

public class LoadingDialog {
    Context context;
    Dialog dialogView;

    public LoadingDialog(Context activity) {
        this.context = activity;
    }

    public void show() {
        dialogView = new Dialog(context,R.style.CustomAlertDialog);
//        dialogView = new Dialog(context,R.style.MyTheme);
        dialogView.setCanceledOnTouchOutside(false);
//        dialogView.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        dialogView.setContentView(R.layout.progress_dialouge_layout);
        dialogView.show();
    }

    public void dismiss() {
        dialogView.dismiss();
    }
}
