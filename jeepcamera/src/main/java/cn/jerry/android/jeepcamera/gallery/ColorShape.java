package cn.jerry.android.jeepcamera.gallery;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

/**
 * Created by JieGuo on 1/5/16.
 */
public class ColorShape extends Shape {

  private Paint paint = new Paint();

  public ColorShape() {
    paint.setStyle(Paint.Style.FILL_AND_STROKE);
    paint.setAntiAlias(true);

    resize(300, 300);
  }

  @Override public void draw(Canvas canvas, Paint paint) {
    float left, top, right, bottom;
    left = 0f; top = 0f; right = getWidth(); bottom = getHeight();

    RectF rectF = new RectF(left, top, right, bottom);
    paint.setColor(getColor());
    canvas.drawRect(rectF, paint);
  }

  private int getColor() {
    int color = Color.argb(getRandom(), getRandom(), getRandom(), getRandom());
    return color;
  }

  private int getRandom() {
    return (int) (Math.random() * 255);
  }
}
