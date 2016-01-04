package cn.jerry.android.jeepcamera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by JieGuo on 15/1/23.
 */
public class LoadingProgressDialog {

  private Context context;
  private ProgressDialog progressDialog;

  public LoadingProgressDialog(Context context) {
    this.context = context;
    progressDialog = new ProgressDialog(context);
    progressDialog.setProgressStyle(R.style.AlertDialog_AppCompat_Light);
    progressDialog.setProgressDrawable(
        context.getResources().getDrawable(R.drawable.progress_circle_shape));
    progressDialog.setMessage(context.getResources().getString(R.string.wait_loading));
    progressDialog.setCancelable(false);
  }

  public void setMessage(CharSequence message) {

    progressDialog.setMessage(message);
  }

  public void setMessage(int resId) {
    progressDialog.setMessage(context.getString(resId));
  }

  public void show() {
    progressDialog.show();
  }

  public void dismiss() {
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
    progressDialog.setOnDismissListener(dismissListener);
  }

  public void setCancelable(boolean cancelable) {
    progressDialog.setCancelable(cancelable);
  }
}
