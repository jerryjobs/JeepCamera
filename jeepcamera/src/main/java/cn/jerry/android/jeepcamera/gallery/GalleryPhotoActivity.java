package cn.jerry.android.jeepcamera.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import cn.jerry.android.jeepcamera.R;

/**
 * Created by JieGuo on 16/1/5.
 */
public class GalleryPhotoActivity extends Activity {

  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.jeep_acitivity_gallery_photo);

    initRecyclerView();
  }

  private void initRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.listView);

    recyclerView.setLayoutManager(getLayoutManager());
    recyclerView.setAdapter(new GalleryAdapter());
  }

  private RecyclerView.LayoutManager getLayoutManager() {
    //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    GridLayoutManager layoutManager =
    new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
    return layoutManager;
  }
}
