package com.larypipot.cimi.imagemap;

import android.graphics.Point;
import android.graphics.PointF;

public class Item {
    public float x;
    public float y;
    public String text;

    public Item(String text, PointF point) {
        this.x = point.x;
        this.y = point.y;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
