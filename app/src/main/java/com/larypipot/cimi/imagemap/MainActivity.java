package com.larypipot.cimi.imagemap;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity implements ItemClickListener {
    private Set<Item> points;
    private ImageMapView imageMapView;
    private Set<Item> selected = new HashSet<>();

    {
        points = new HashSet<>();
        points.add(new Item("Pedro", new Point(250, 330)));
        points.add(new Item("Caracasse", new Point(213, 431)));
        points.add(new Item("Gigot", new Point(294, 493)));
        points.add(new Item("Flétant", new Point(250, 550)));
        points.add(new Item("Connrad", new Point(319, 640)));
        points.add(new Item("Pendouse", new Point(221, 614)));
        points.add(new Item("Douglat", new Point(221, 789)));
        points.add(new Item("Merlouse", new Point(250, 104)));
        points.add(new Item("Fereur", new Point(250, 70)));
        points.add(new Item("Jorias", new Point(250, 107)));
        points.add(new Item("Tango", new Point(344, 183)));
        points.add(new Item("Charli", new Point(400, 550)));
        points.add(new Item("Begnet", new Point(99, 471)));
        points.add(new Item("Torla", new Point(100, 522)));
        points.add(new Item("Fletose", new Point(126, 377)));
        points.add(new Item("Jisade", new Point(254, 344)));
        points.add(new Item("Perlut", new Point(330, 480)));
        points.add(new Item("Doglof", new Point(195, 705)));
        points.add(new Item("Fleveur", new Point(228, 923)));
        points.add(new Item("Jorianne", new Point(306, 965)));
        points.add(new Item("Tagos", new Point(200, 950)));
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
