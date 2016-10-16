package com.zechassault.cimi.imagemap.anatomy;

import android.graphics.PointF;

public class AnatomyItem {
    public float x;
    public float y;
    public String text;


    public AnatomyItem(String text, PointF point) {
        this.x = point.x;
        this.y = point.y;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
