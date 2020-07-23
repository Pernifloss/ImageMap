package com.example.myapplication.anatomy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.myapplication.R;
import com.zechassault.zonemap.adapter.NoteImageAdapter;
import com.zechassault.zonemap.listener.ItemClickListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.core.content.ContextCompat;

public class NoteImageAdapterImpl extends NoteImageAdapter<AnatomyItem> implements ItemClickListener<AnatomyItem> {
    private final List<AnatomyItem> items;
    private final Context context;
    private Paint labelPaintUnselected;
    private Paint labelPaintSelected;
    private Set<AnatomyItem> selected = new HashSet<>();

    public NoteImageAdapterImpl(List<AnatomyItem> items, Context context) {
        this.context = context;
        this.items = items;

        /*  Define unselected label paint */
        labelPaintUnselected = new Paint();
        labelPaintUnselected.setAntiAlias(true);
        labelPaintUnselected.setStrokeWidth(5);
        labelPaintUnselected.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        labelPaintUnselected.setTextSize(50);

        /*Define selected item paint for labels */
        labelPaintSelected = new Paint();
        labelPaintSelected.setAntiAlias(true);
        labelPaintSelected.setFakeBoldText(true);
        labelPaintSelected.setTextSize(50);
        labelPaintSelected.setStrokeWidth(5);
        labelPaintSelected.setColor(ContextCompat.getColor(context, R.color.red));

        setItemClickListener(this);

    }

    /*
        Tell the adapter how to get an item coordinate
     */
    @Override
    public PointF getItemCoordinates(AnatomyItem item) {
        return item.getCoordinate();
    }

    /*
        Tell adapter how to retrieve an item based on its position
     */
    @Override
    public AnatomyItem getItemAtPosition(int position) {
        return items.get(position);
    }

    /*
       Tell the adapter the number of total item
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /*
    here we give the adapter the text to show in the sides label lists
     */
    @Override
    public String getLabel(AnatomyItem item) {
        return item.getText();
    }

    /**
     * here we give the function to define the way the adapter retrieve an item bitmap
     */
    @Override
    public Bitmap getItemBitmap(AnatomyItem item) {

        if (selected.contains(item)) {
            return item.getBitmapSelected();
        }
        return item.getBitmapUnselected();
    }

    /**
     * here we define how the paint to use depending on weather an item is "selected" or not
     */
    @Override
    public Paint getLabelPaint(AnatomyItem item) {
        if (selected.contains(item)) {
            return labelPaintSelected;
        }
        return labelPaintUnselected;
    }

    /**
     * If some item label should not link to ce center of the item location you can use getAnchor
     * to define a custom location for the line to link to.
     * PointF(0,0) will link the label to the top left of an item image on the map
     * PointF(0.5,0.5) will link the label to the center. By default the anchor is  PointF(0.5,0.5)
     * so here we define only those which not link to center
     */
    @Override
    public PointF getAnchor(AnatomyItem item) {
        switch (item.getText()) {
            case "Arms":
                return new PointF(0.95f, 0.6f);
            case "Nose":
                return new PointF(0.25f, 0.7f);
            case "Shoulders":
                return new PointF(0.33f, 0.5f);
            case "Quadriceps":
                return new PointF(0.1f, 0.5f);
        }
        return super.getAnchor(item);
    }

    /**
     * Define what happen when an item is clicked here we store taped item,
     * so we can display differently selected items
     */
    @Override
    public void onMapItemClick(AnatomyItem item) {
        if (selected.contains(item)) {
            selected.remove(item);
        } else {
            selected.add(item);
        }
        notifyDataSetHasChanged();

    }
}
