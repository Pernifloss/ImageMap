package com.larypipot.cimi.imagemap;

import android.graphics.Rect;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ImageMapViewTest {
    @Test
    public void intersect() throws Exception {
        Rect rect = new Rect();
        rect.bottom = 50;
        rect.top = 50;
        rect.left = 50;
        rect.right = 50;

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


    }


}