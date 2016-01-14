package cn.jerry.android.jeepcamera.gallery;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.jerry.android.jeepcamera.R;
import cn.jerry.android.jeepcamera.Util.Util;

/**
 * Created by JieGuo on 1/5/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    private int contentWidth = 0;
    private int itemWidth = 0;
    private List<String> data = new ArrayList<>();

    public void addData(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jeep_activity_gallery_item, parent, false);

        if (contentWidth == 0) {
            contentWidth = parent.getMeasuredWidth();
            itemWidth = contentWidth / 3;
        }

        return new GalleryViewHolder(itemView, itemWidth);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {

        Util.displayImage(holder.cover, "file:///" + data.get(position), itemWidth, itemWidth);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
