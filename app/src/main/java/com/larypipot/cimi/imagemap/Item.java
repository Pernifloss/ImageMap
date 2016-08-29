package com.larypipot.cimi.imagemap;

import android.graphics.Point;

public class Item {
    public int x;
    public int y;
    public String text;

    public Item(String text, Point point) {
        this.x = point.x;
        this.y = point.y;
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
