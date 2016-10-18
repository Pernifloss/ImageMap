package com.zechassault.cimi.imagemap.anatomie.anatomy;

import android.graphics.PointF;

public class AnatomyTwoItem {
    private final PointF point;
    private final TYPE type;
    public String text;


    public AnatomyTwoItem(TYPE type, String text, PointF point) {
        this.type =type;
        this.point = point;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public PointF getPoint() {
        return point;
    }

    public TYPE getType() {
        return type;
    }
}
