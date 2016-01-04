package cn.jerry.android.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import cn.jerry.android.jeepcamera.BasePhotoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show();
      }
    });

    Button photo = (Button) findViewById(R.id.button);
    Button choose = (Button) findViewById(R.id.button2);

    photo.setOnClickListener(this);
    choose.setOnClickListener(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override public void onClick(View v) {
    BasePhotoActivity.tackPhoto(this, v.getId() == R.id.button2);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_CANCELED) {
      return;
    }

    Uri uri = data.getData();
    switch (requestCode) {
      case BasePhotoActivity.REQUEST_CODE_SELECT:
        //upload(uri);
        Log.e("URL", uri.toString());
        break;

      case BasePhotoActivity.REQUEST_CODE_PHOTO:
        //upload(uri);
        Log.e("URL", uri.toString());
        break;

      default:
        break;
    }

    ImageView imageView = (ImageView) findViewById(R.id.imageView);
    imageView.setImageURI(uri);
  }
}
