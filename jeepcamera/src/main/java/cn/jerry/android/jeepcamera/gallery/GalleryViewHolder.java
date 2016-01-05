package cn.jerry.android.jeepcamera.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import cn.jerry.android.jeepcamera.R;

/**
 * Created by JieGuo on 1/5/16.
 */
public class GalleryViewHolder extends RecyclerView.ViewHolder {

  public ImageView cover;

  public GalleryViewHolder(View itemView, int itemHeight) {
    super(itemView);

    cover = (ImageView) itemView.findViewById(R.id.cover);
    cover.setMinimumHeight(itemHeight);
  }
}
