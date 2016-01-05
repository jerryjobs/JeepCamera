package cn.jerry.android.jeepcamera.gallery;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.jerry.android.jeepcamera.R;

/**
 * Created by JieGuo on 1/5/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

  private int contentWidth = 0;

  @Override public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.jeep_activity_gallery_item, parent, false);

    if (contentWidth == 0) {
      contentWidth = parent.getMeasuredWidth();
    }

    return new GalleryViewHolder(itemView, contentWidth / 3);
  }

  @Override public void onBindViewHolder(GalleryViewHolder holder, int position) {

    ColorDrawable colorDrawable = new ColorDrawable();
    colorDrawable.setColor(getColor());
    holder.cover.setImageDrawable(colorDrawable);
  }

  @Override public int getItemCount() {
    return 300;
  }

  private int getColor() {
    int color = Color.argb(getRandom(), getRandom(), getRandom(), getRandom());
    return color;
  }

  private int getRandom() {
    return (int) (Math.random() * 199);
  }
}
