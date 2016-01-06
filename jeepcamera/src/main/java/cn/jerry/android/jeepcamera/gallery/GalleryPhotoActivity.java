package cn.jerry.android.jeepcamera.gallery;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
      String path = cursor.getString(cursor
          .getColumnIndex(MediaStore.Images.Media.DATA));

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
}
