package com.zechassault.zonemap;

import android.graphics.PointF;
import android.graphics.Rect;

import com.zechassault.zonemap.View.ImageMapView;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImageMapViewTest {
    @Test
    public void intersect() throws Exception {
        Rect rect = new Rect();
        rect.bottom = 100;
        rect.top = 50;
        rect.left = 50;
        rect.right = 100;

        assertFalse(ImageMapView.doesIntersect(10, 20, rect));
        assertFalse(ImageMapView.doesIntersect(55, 20, rect));
        assertFalse(ImageMapView.doesIntersect(110, 20, rect));

        assertTrue(ImageMapView.doesIntersect(60, 60, rect));

        assertFalse(ImageMapView.doesIntersect(20, 55, rect));
        assertFalse(ImageMapView.doesIntersect(20, 110, rect));

        assertFalse(ImageMapView.doesIntersect(110, 110, rect));

    }

    @Test
    public void getDestinationRect() {
        PointF f = new PointF(12f, 15f);
        f.y = 12f;
        f.x = 15f;
        System.out.println(f.y);
    }


}