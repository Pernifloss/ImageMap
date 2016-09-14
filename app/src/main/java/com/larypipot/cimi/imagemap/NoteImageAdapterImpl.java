package com.larypipot.cimi.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;

import com.larypipot.cimi.imagemap.Adapter.NoteImageAdapter;
import com.larypipot.cimi.imagemap.Util.BitmapUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteImageAdapterImpl extends NoteImageAdapter<Item> {
    private final List<Item> items;
    private final Context context;
    private Set<Item> selectedItems = new HashSet<>();

    public void selectedItem(Item item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        notifyDataSetHasChanged();
    }



    public NoteImageAdapterImpl(List<Item> items, Context context) {
        this.context = context;
        this.items = items;

    }

    @Override
    public PointF getItemLocation(Item item) {
        return new PointF(item.x,item.y);
    }

    @Override
    public Item getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public String getLabel(Item item) {
        return item.getText();
    }

    @Override
    public Bitmap getItemBitmap(Item item) {
        if (selectedItems.contains(item)){
            return BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);
        }
        return BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_unselected);
    }
    @Override
    public Paint getPaint(Item item) {
        Paint paint = new Paint();
        return paint;
    }

    @Override
    public Paint getLabelPaint(Item item) {
        return new Paint();
    }
}
