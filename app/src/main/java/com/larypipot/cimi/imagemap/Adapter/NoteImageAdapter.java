package com.larypipot.cimi.imagemap.Adapter;

import android.graphics.Paint;

public abstract class NoteImageAdapter<T>  extends MapAdapter<T> {
    public abstract Paint getPaint(T item);


    public  Paint getLabelPaint(T item) {
        return new Paint();
    }

    /**
     * Define this function to specify the legend of an item
     * @param item the item to which the legend is linked
     * @return String, legend displayed
     */
    public abstract String getLabel(T item);
}
