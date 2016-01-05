/**
 * Copyright 2012 Easy Read Tech. All rights reserved.
 */
package cn.jerry.android.jeepcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import cn.jerry.android.jeepcamera.config.Config;
import java.io.File;

/**
 * @author guojie
 */
public class ConfirmImageActivity extends Activity implements View.OnClickListener {

  public static final int REQUEST_CROP_IMAGE = 110;

  private Bitmap bitmap;
  private String filePath;
  private String cropToPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    cropToPath = Config.getInstance().picDir() + "/" + System.currentTimeMillis() + ".jpg";

    filePath = getIntent().getStringExtra("file");
    setContentView(R.layout.jeep_activity_base_photo);

    ImageView image = (ImageView) findViewById(R.id.showPhoto);

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 5;
    bitmap = BitmapFactory.decodeFile(filePath, options);
    image.setImageBitmap(bitmap);

    findViewById(R.id.ok).setOnClickListener(this);
    findViewById(R.id.discard).setOnClickListener(this);
    findViewById(R.id.cut).setOnClickListener(this);

    // 直接去裁剪照片，不要再确认
    goSystemCrop();
  }

  @Override
  public void onClick(View v) {

    if (v.getId() == R.id.ok) {
      ok(filePath);
    } else if (v.getId() == R.id.cut) {
      goSystemCrop();
    } else if (v.getId() == R.id.discard) {
      cancel();
    }

    if (bitmap != null) {
      bitmap.recycle();
    }
  }

  private void goSystemCrop() {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.putExtra("return-data", false)
        .putExtra("noFaceDetection", true)
        .putExtra("crop", "true")
        .putExtra("scale", true)
        .putExtra("scaleUpIfNeeded", true)
        .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

    intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");

    Bundle b = getIntent().getBundleExtra(BasePhotoActivity.MEASURABLE_BUNDLE_NAME);
    if (null != b) {
      intent.putExtra("aspectX", b.getInt("aspectX"));
      intent.putExtra("aspectY", b.getInt("aspectY"));
    }
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cropToPath)));
    startActivityForResult(intent, REQUEST_CROP_IMAGE);
  }

  @Override
  public void onBackPressed() {
    cancel();
  }

  private void cancel() {
    setResult(RESULT_CANCELED);
    finish();
  }

  // 直接返回数据
  private void ok(String path) {
    Intent intent = new Intent();
    intent.setData(Uri.fromFile(new File(path)));
    setResult(RESULT_OK, intent);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case REQUEST_CROP_IMAGE:
        if (resultCode == RESULT_CANCELED) {
          cancel();
        } else {
          ok(cropToPath);
        }
        break;

      default:
        break;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }
}
