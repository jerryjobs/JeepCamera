/**
 * Copyright 2012 Easy Read Tech. All rights reserved.
 */
package cn.jerry.android.jeepcamera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import cn.jerry.android.jeepcamera.config.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author guojie
 */
public class BasePhotoActivity extends Activity {

  public static final String MEASURABLE_BUNDLE_NAME = "measurable";

  public static final int REQUEST_CODE_SELECT = 13;
  public static final int REQUEST_CODE_PHOTO = 11;

  /**
   * 请求确认照片去向( 去向包括直接返回此照片 和裁剪一下再返回)
   */
  public static final int REQUEST_PHOTO_CONFIRM = 13;
  /**
   * 选择照片
   */
  public static final int RESQUEST_SELECT_PHOTO = 14;
  private static final String TAG = "BasePhotoActivity";
  /**
   * 临时存储位置
   */
  private static String tempPicPath;

  @SuppressWarnings("unused")
  public static void tackPhoto(Activity context, boolean selectSystemPic) {
    Bundle measurable = new Bundle();
    measurable.putInt("aspectX", 1);
    measurable.putInt("aspectY", 1);
    Intent intent = new Intent(context, BasePhotoActivity.class);
    intent.putExtra("chosePhoto", selectSystemPic);
    intent.putExtra(BasePhotoActivity.MEASURABLE_BUNDLE_NAME, measurable);

    if (selectSystemPic) {
      context.startActivityForResult(intent, REQUEST_CODE_SELECT);
    } else {
      context.startActivityForResult(intent, REQUEST_CODE_PHOTO);
    }
  }

  public static void saveBitmap(String filename, Bitmap bitmap) {
    File f = new File(filename);
    FileOutputStream fOut = null;
    try {
      f.createNewFile();
      fOut = new FileOutputStream(f);
      bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
      fOut.flush();
    } catch (Exception e) {
      Log.e(TAG, "failed to save image", e);
    } finally {
      try {
        fOut.close();
      } catch (IOException e) {
      }
    }
  }

  ;

  @SuppressLint("HandlerLeak") protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    boolean chosePhoto = getIntent().getBooleanExtra("chosePhoto", false);
    if (chosePhoto) {
      Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
      intentFromGallery.setType("image/*");
      startActivityForResult(intentFromGallery, RESQUEST_SELECT_PHOTO);
    } else {
      if (tempPicPath == null) {
        takePhoto();
      }
    }
  }

  /**
   * 去照相
   * <p/>
   * 返回结果时接口
   */
  public void takePhoto() {
    tempPicPath = Config.getInstance().picDir() + "/" + System.currentTimeMillis() + ".jpg";
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempPicPath)));
    startActivityForResult(intent, REQUEST_CODE_PHOTO);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    switch (requestCode) {
      case REQUEST_CODE_PHOTO:
        // 请求拍照，返回处理
        if (RESULT_CANCELED == resultCode) {
          setResult(RESULT_CANCELED);
          finish();
          return;
        }

        File file = new File(tempPicPath);
        if (file.exists()) {
          Intent intent = new Intent(this, ConfirmImageActivity.class);
          intent.putExtra("file", tempPicPath);
          intent.putExtra(MEASURABLE_BUNDLE_NAME,
              getIntent().getBundleExtra(MEASURABLE_BUNDLE_NAME));
          startActivityForResult(intent, REQUEST_PHOTO_CONFIRM);
        } else {
          Log.e(TAG, "file not exists! : [" + file.getAbsolutePath() + "]");
        }
        break;

      case RESQUEST_SELECT_PHOTO:
        if (data == null) {
          setResult(RESULT_CANCELED);
          finish();
          return;
        }
        new ImageLoadTask().execute(data.getData());
        break;

      case REQUEST_PHOTO_CONFIRM:
        // 确认照片是否裁剪
        if (resultCode == RESULT_CANCELED) {
          setResult(RESULT_CANCELED);
        } else {
          setResult(RESULT_OK, data);
        }
        finish();
        break;

      default:
        break;
    }
  }

  @Override public void finish() {
    tempPicPath = null;
    super.finish();
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onPause() {
    super.onPause();
  }

  private class ImageLoadTask extends AsyncTask<Uri, Void, String> {

    private LoadingProgressDialog dlg;

    @Override protected String doInBackground(Uri... params) {
      Cursor cursor = null;
      try {
        Uri contentUri = params[0];
        if (contentUri.toString().startsWith("content://")) {
          String[] proj = {
              MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME
          };
          cursor = getContentResolver().query(contentUri, proj, null, null, null);
          int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          cursor.moveToFirst();
          String path = cursor.getString(columnIndex);
          if (path == null) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
            path = cursor.getString(columnIndex);
          }
          if (new File(path).exists()) {
            return path;
          }
          return downloadImage(contentUri);
        } else {
          return contentUri.toString();
        }
      } catch (Exception e) {
        Log.e(TAG, "failed to load image", e);
        return null;
      } finally {
        if (null != cursor) {
          cursor.close();
        }
      }
    }

    @Override protected void onPreExecute() {
      super.onPreExecute();
      dlg = new LoadingProgressDialog(BasePhotoActivity.this);
      dlg.setMessage(R.string.wait_loading);
      dlg.show();
    }

    @Override protected void onPostExecute(String imagePath) {
      super.onPostExecute(imagePath);
      dlg.dismiss();
      if (imagePath != null) {
        Intent intent = new Intent(BasePhotoActivity.this, ConfirmImageActivity.class);
        intent.putExtra("file", imagePath);
        intent.putExtra(MEASURABLE_BUNDLE_NAME, getIntent().getBundleExtra(MEASURABLE_BUNDLE_NAME));
        startActivityForResult(intent, REQUEST_PHOTO_CONFIRM);
      } else {
        // Util.startToast(R.string.take_photo);
        finish();
      }
    }

    private String downloadImage(Uri contentUri) throws FileNotFoundException {
      Bitmap bitmap = null;
      bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(contentUri));
      String path = Config.getInstance().picDir() + "/" + System.currentTimeMillis() + ".jpg";
      saveBitmap(path, bitmap);
      return path;
    }
  }
}
