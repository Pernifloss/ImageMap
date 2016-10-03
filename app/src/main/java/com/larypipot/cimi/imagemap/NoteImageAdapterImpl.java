package com.larypipot.cimi.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;

import com.larypipot.cimi.zonemap.Adapter.NoteImageAdapter;
import com.larypipot.cimi.zonemap.Util.BitmapUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteImageAdapterImpl extends NoteImageAdapter<Item> {
    private final List<Item> items;
    private final Context context;
    private final Bitmap quad;
    private final Bitmap colQuad;
    private Set<Item> selectedItems = new HashSet<>();
    private Paint labelPaintUnselected;
    private Paint labelPaintSelected;
    private Paint paint;

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

        paint = new Paint();
        paint.setAntiAlias(true);

        labelPaintUnselected = new Paint();
        labelPaintUnselected.setAntiAlias(true);
        labelPaintUnselected.setTextSize(30);

        labelPaintSelected = new Paint();
        labelPaintSelected.setAntiAlias(true);
        labelPaintSelected.setFakeBoldText(true);
        labelPaintSelected.setTextSize(30);
        colQuad = BitmapUtils.resAsBitmap(context,R.drawable.quad_col);
        quad = BitmapUtils.resAsBitmap(context,R.drawable.quad);
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
        if (item.getText().equals("Quadriceps")){
            if (selectedItems.contains(item)){
                return colQuad;
            }else {
                return quad;
            }
        }
        if (selectedItems.contains(item)){
            return BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);
        }
        return BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_unselected);
    }

    @Override
    public Paint getPaint(Item item) {
        return paint;
    }

    @Override
    public  Paint getLabelPaint(Item item) {
        if (selectedItems.contains(item)){
            return labelPaintSelected;
        }
        return labelPaintUnselected;
    }
}
