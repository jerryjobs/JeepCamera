package cn.jerry.android.jeepcamera.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import cn.jerry.android.jeepcamera.R;
import cn.jerry.android.jeepcamera.config.Config;

/**
 * Created by JieGuo on 16/1/5.
 */
public class GalleryPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GalleryPhotoActivity";
    private RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private HandlerThread handlerThread;
    private Handler handler;

    private HashMap<String, List<String>> groupMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.jeep_acitivity_gallery_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initRecyclerView();

        loadPhotos();
    }


    @Override
    public void onClick(View v) {
        String path = (String) v.getTag();
        Intent intent = new Intent();
        intent.setData(Uri.parse("file:///" + path));
        setResult(Config.REQUEST_CODE_SELECT, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    private void loadPhotos() {
        handlerThread = new HandlerThread("LoadImage");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1) {

                    Set<String> keys = groupMap.keySet();
                    List<String> paths = new ArrayList<>();
                    for (String k : keys) {
                        paths.addAll(groupMap.get(k));
                    }
                    adapter.addData(paths);
                    adapter.notifyDataSetChanged();

                    handlerThread.quit();
                }
                return true;
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                getGalleryPhoto();
                handler.sendEmptyMessage(1);
            }
        });
    }

    private void initRecyclerView() {

        adapter = new GalleryAdapter();
        adapter.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.listView);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        return layoutManager;
    }

    private void getGalleryPhoto() {

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return;
        }

        explainCursor(cursor);

        cursor.close();
    }

    private void explainCursor(Cursor cursor) {
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            Log.i(TAG, "path : " + path);

            String parentName = new File(path).getParentFile().getName();

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
