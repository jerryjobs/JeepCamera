package cn.jerry.android.jeepcamera.gallery;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import cn.jerry.android.jeepcamera.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by JieGuo on 16/1/5.
 */
public class GalleryPhotoActivity extends AppCompatActivity {

  private static final String TAG = "GalleryPhotoActivity";
  private RecyclerView recyclerView;
  private HashMap<String, List<String>> groupMap = new HashMap<String, List<String>>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.jeep_acitivity_gallery_photo);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initRecyclerView();

    if (initCamera()) {

    } else {
      // TODO no camera
    }

    new Thread(new Runnable() {
      @Override public void run() {
        getGalleryPhoto();
      }
    });
  }

  private void initRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.listView);

    recyclerView.setLayoutManager(getLayoutManager());
    recyclerView.setAdapter(new GalleryAdapter());

    recyclerView.addItemDecoration(new SpacesItemDecoration(2));
  }

  private RecyclerView.LayoutManager getLayoutManager() {
    //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    GridLayoutManager layoutManager =
        new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
    return layoutManager;
  }

  private void getGalleryPhoto() {

    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    Cursor cursor = getContentResolver().query(uri, null,
        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

    if (cursor == null) {
      return;
    }

    explainCursor(cursor);

    cursor.close();
  }

  private void explainCursor(Cursor cursor) {
    while (cursor.moveToNext()) {
      //获取图片的路径
      String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

      //获取该图片的父路径名
      String parentName = new File(path).getParentFile().getName();

      Log.d(TAG, "explainCursor: " + parentName);

      //根据父路径名将图片放入到mGruopMap中
      if (!groupMap.containsKey(parentName)) {
        List<String> chileList = new ArrayList<>();
        chileList.add(path);
        groupMap.put(parentName, chileList);
      } else {
        groupMap.get(parentName).add(path);
      }
    }
  }

  private boolean initCamera() {
    PackageManager pm = getPackageManager();
    boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
        pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
        Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD ||
        Camera.getNumberOfCameras() > 0;

    return hasCamera;
  }

  @Override protected void onResume() {
    super.onResume();

    // TODO: 1/8/16 open camera devices read image load to surface view
  }

  @Override protected void onPause() {
    super.onPause();

    // TODO: 1/8/16 release camera device when activity is paused
  }

  @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
      int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private CameraDevice device = null;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private void loadCamera() throws CameraAccessException {
    CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
    String[] camera = manager.getCameraIdList();
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    manager.openCamera(camera[0], new CameraDevice.StateCallback() {
      @Override public void onOpened(CameraDevice camera) {
        device = camera;
      }

      @Override public void onDisconnected(CameraDevice camera) {
        camera.close();
      }

      @Override public void onError(CameraDevice camera, int error) {
        camera.close();
        Log.e(TAG, "error code is : [" + error + "], when we open the camera.");
      }
    }, new CameraHandler());
  }


  private class CameraHandler extends Handler {

    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
    }
  }
}
