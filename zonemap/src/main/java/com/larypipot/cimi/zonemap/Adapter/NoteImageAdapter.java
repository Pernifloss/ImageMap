package com.larypipot.cimi.zonemap.Adapter;

import android.graphics.Paint;

public abstract class NoteImageAdapter<T>  extends MapAdapter<T> {

    private Paint paint;

    public abstract Paint getPaint(T item);


    public  Paint getLabelPaint(T item) {

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(22);
        return paint;
    }

    /**
     * Define this function to specify the legend of an item
     * @param item the item to which the legend is linked
     * @return String, legend displayed
     */
    public abstract String getLabel(T item);
}
