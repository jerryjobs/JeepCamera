package cn.jerry.android.jeepcamera.gallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import cn.jerry.android.jeepcamera.R;

/**
 * Created by JieGuo on 16/1/5.
 */
public class GalleryPhotoActivity extends AppCompatActivity {

  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.jeep_acitivity_gallery_photo);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initRecyclerView();
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
}
