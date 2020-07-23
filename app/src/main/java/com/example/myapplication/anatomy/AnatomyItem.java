package com.example.myapplication.anatomy;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Anatomy item holder
 */
public class AnatomyItem {

    /**
     * Item coordinate on image
     */
    private PointF coordinate;

    /**
     * Item label
     */
    private String text;

    /**
     * bitmap to show when item is "selected" (clicked previously)
     */
    Bitmap bitmapSelected;

    /**
     * bitmap to show when item not "selected"
     */
    Bitmap bitmapUnselected;


    public AnatomyItem(String text, PointF point, Bitmap bitmapSelected, Bitmap bitmapUnselected) {
        this.coordinate = point;
        this.text = text;
        this.bitmapSelected = bitmapSelected;
        this.bitmapUnselected = bitmapUnselected;
    }

    public PointF getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(PointF coordinate) {
        this.coordinate = coordinate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Bitmap getBitmapSelected() {
        return bitmapSelected;
    }

    public void setBitmapSelected(Bitmap bitmapSelected) {
        this.bitmapSelected = bitmapSelected;
    }

    public Bitmap getBitmapUnselected() {
        return bitmapUnselected;
    }

    public void setBitmapUnselected(Bitmap bitmapUnselected) {
        this.bitmapUnselected = bitmapUnselected;
    }
}
