package com.zechassault.cimi.imagemap.europe;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Created by Lar on 09/10/2016.
 */
public class Country {
    PointF location;
    String label;
    Bitmap selectedBitmap;
    Bitmap unselectedBitmap;
    public PointF anchor = new PointF(0.5f,0.5f);

    public Country(String label, Bitmap selectedBitmap, Bitmap unselectedBitmap, float x, float y) {
        this.label = label;
        this.selectedBitmap = selectedBitmap;
        this.unselectedBitmap = unselectedBitmap;
        this.location = new PointF(x, y);
    }
}
