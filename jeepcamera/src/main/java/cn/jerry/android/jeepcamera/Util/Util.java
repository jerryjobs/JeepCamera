package cn.jerry.android.jeepcamera.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by JieGuo on 1/14/16.
 */
public final class Util {

    private static Picasso picasso = null;

    private Util() {
    }

    public static void displayImage(ImageView view, String path, int width, int height) {
        Picasso.with(view.getContext())
                .load(path)
                .centerCrop()
                .resize(width, height)
                .into(view);
    }
}
