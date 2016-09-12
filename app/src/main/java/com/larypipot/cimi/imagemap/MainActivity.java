package com.larypipot.cimi.imagemap;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity implements ItemClickListener {
    private Set<Item> points;
    private ImageMapView imageMapView;
    private Set<Item> selected = new HashSet<>();

    {
        points = new HashSet<>();
        points.add(new Item("Pedro", new PointF(0.5f, 0.33f)));
        points.add(new Item("Caracasse", new PointF(0.6f, 0.493f)));
        points.add(new Item("Gigot", new PointF(0.5f, 0.550f)));
        points.add(new Item("Flétant", new PointF(0.638f, 0.640f)));
        points.add(new Item("Connrad", new PointF(0.442f, 0.614f)));
        points.add(new Item("Pendouse", new PointF(0.426f, 0.431f)));
        points.add(new Item("Douglat", new PointF(0.442f, 0.789f)));
        points.add(new Item("Merlouse", new PointF(0.5f, 0.104f)));
        points.add(new Item("Fereur", new PointF(0.5f, 0.07f)));
        points.add(new Item("Jorias", new PointF(0.5f, 0.107f)));
        points.add(new Item("Tango", new PointF(0.688f, 0.183f)));
        points.add(new Item("Charli", new PointF(0.58f, 0.551f)));
        points.add(new Item("Begnet", new PointF(0.35f, 0.471f)));
        points.add(new Item("Torla", new PointF(0.4f, 0.522f)));
        points.add(new Item("Fletose", new PointF(0.32f, 0.377f)));
        points.add(new Item("Jisade", new PointF(0.5f, 0.344f)));
        points.add(new Item("Perlut", new PointF(0.66f, 0.480f)));
        points.add(new Item("Doglof", new PointF(0.39f, 0.705f)));
        points.add(new Item("Fleveur", new PointF(0.456f, 0.923f)));
        points.add(new Item("Jorianne", new PointF(0.612f, 0.965f)));
        points.add(new Item("Tagos", new PointF(0.4f, 0.950f)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageMapView = (ImageMapView) findViewById(R.id.imageMapView);
        imageMapView.addItems(points);
        imageMapView.itemClickListener = this;
    }

    @Override
    public void onMapItemClick(Item answer) {
        if (selected.contains(answer)) {
            selected.remove(answer);
            imageMapView.selectedItem(answer, false);
        } else {
            selected.add(answer);
            imageMapView.selectedItem(answer, true);
        }
    }
}
