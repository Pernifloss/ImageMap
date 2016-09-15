package com.larypipot.cimi.imagemap;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class BoneItem {
    public float x;
    public float y;
    public String text;
    Bitmap bitmap;
    Bitmap bitmapEmpty;

    public BoneItem(String text, PointF point, Bitmap bitmap, Bitmap bitmapEmpty) {
        this.x = point.x;
        this.y = point.y;
        this.text = text;
        this.bitmapEmpty = bitmapEmpty;
        this.bitmap = bitmap;
    }

    public String getText() {
        return text;
    }
}
