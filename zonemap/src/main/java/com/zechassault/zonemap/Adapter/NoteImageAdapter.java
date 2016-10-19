package com.zechassault.zonemap.Adapter;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.PointF;

public abstract class NoteImageAdapter<T> extends MapAdapter<T> {
    private Paint labelPaint = new Paint();
    private Paint linePaint = new Paint();
    private PointF centerAnchor = new PointF(0.5f, 0.5f);

    public NoteImageAdapter() {
        //18 sp
        float textSize = (Resources.getSystem().getDisplayMetrics().scaledDensity) * 18;
        labelPaint.setAntiAlias(true);
        labelPaint.setTextSize(textSize);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(4);
    }

    /**
     * Define this function to specify the legend of an item
     *
     * @param item the item to which the legend is linked
     * @return String, legend displayed
     */
    public abstract String getLabel(T item);

    /**
     * Override this function to define your own Paint for label drawing
     *
     * @param item the item to which the legend is linked
     * @return the labelPaint of to draw the label
     */
    public Paint getLabelPaint(T item) {
        return labelPaint;
    }

    /**
     * Override this function to define your own Paint for lines
     *
     * @param item the item to which the legend is linked
     * @return the labelPaint of to draw the label
     */
    public Paint getLinePaint(T item) {
        return linePaint;
    }

    /**
     * Override this method to change the anchor calculation based on the bitmap
     * PointF(0.0f,0.5f) will anchor the line to the center left of the bitmap
     * @param item the item to define the anchor
     * @return anchor as PointF
     */
    public PointF getAnchor(T item) {
        return centerAnchor;
    }

    /**
     * Overide this methode to define on wich side the label will be
     * @param item the item to define the side
     * @return boolean true, item will be on the left
     */
    public boolean isItemOnLeftSide(T item) {
        return this.getItemLocation(item).x < 0.5f;
    }

}
