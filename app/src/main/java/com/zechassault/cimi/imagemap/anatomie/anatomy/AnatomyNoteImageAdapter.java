package com.zechassault.cimi.imagemap.anatomie.anatomy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;

import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Adapter.NoteImageAdapter;
import com.zechassault.zonemap.Listener.ItemClickListener;
import com.zechassault.zonemap.Util.BitmapUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnatomyNoteImageAdapter extends NoteImageAdapter<AnatomyTwoItem> {
    private final List<AnatomyTwoItem> items;
    private final Context context;
    private final Bitmap nullBit;
    private Set<AnatomyTwoItem> selectedItems = new HashSet<>();
    private Paint labelPaintUnselected;
    private Paint labelPaintSelected;
    private Paint paint;
    private Bitmap selectedBitmap;
    private Bitmap unselectedBitmap;
    private final Paint trainspaint;

    TYPE mode = TYPE.MUSCLE;

    public void selectedItem(AnatomyTwoItem item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        notifyDataSetHasChanged();
    }


    public AnatomyNoteImageAdapter(List<AnatomyTwoItem> items, Context context) {
        this.context = context;
        this.items = items;

        paint = new Paint();
        paint.setAntiAlias(true);
        this.setItemClickListener(new ItemClickListener<AnatomyTwoItem>() {
            @Override
            public void onMapItemClick(AnatomyTwoItem item) {
                selectedItem(item);
            }
        });
        labelPaintUnselected = new Paint();
        labelPaintUnselected.setAntiAlias(true);
        labelPaintUnselected.setStrokeWidth(5);
        labelPaintUnselected.setTextSize(30);
        labelPaintUnselected.setColor(Color.RED);

        labelPaintSelected = new Paint();
        labelPaintSelected.setAntiAlias(true);
        labelPaintSelected.setFakeBoldText(true);
        labelPaintSelected.setTextSize(30);
        labelPaintSelected.setStrokeWidth(5);
        labelPaintSelected.setColor(Color.RED);
        trainspaint = new Paint();
        trainspaint.setColor(Color.TRANSPARENT);


        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        nullBit =Bitmap.createBitmap(1, 1, conf);

        selectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);

        unselectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_unselected);
    }

    @Override
    public PointF getItemLocation(AnatomyTwoItem item) {
        return item.getPoint();
    }

    @Override
    public AnatomyTwoItem getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public String getLabel(AnatomyTwoItem item) {
        return item.getText();
    }

    @Override
    public Bitmap getItemBitmap(AnatomyTwoItem item) {
        if (item.getType().equals(mode)){

            if (selectedItems.contains(item)) {
                return selectedBitmap;
            }
            return unselectedBitmap;
        }

        return nullBit;
    }


    @Override
    public Paint getLabelPaint(AnatomyTwoItem item) {
        if (item.getType().equals(mode)){
            if (selectedItems.contains(item)) {
                return labelPaintSelected;
            }
            return labelPaintUnselected;
        }
            return trainspaint;
    }
    @Override
    public Paint getLinePaint(AnatomyTwoItem item) {
        if (item.getType().equals(mode)){
            if (selectedItems.contains(item)) {
                return labelPaintSelected;
            }
            return labelPaintUnselected;
        }
        return trainspaint;
    }

    public void setMode(TYPE joint) {

        this.mode = joint;
        switch (mode){
            case MUSCLE:
                labelPaintUnselected.setColor(Color.RED);
                labelPaintSelected.setColor(Color.RED);

                break;
            case JOINT:
                labelPaintUnselected.setColor(context.getResources().getColor(R.color.yellowCoach));
                labelPaintSelected.setColor(context.getResources().getColor(R.color.yellowCoach));
                break;
        }
        notifyDataSetHasChanged();
    }

    @Override
    public boolean isItemOnLeftSide(AnatomyTwoItem item) {
        return true;
    }
/*
    @Override
    public PointF getAnchor(AnatomyTwoItem item) {
        return super.getAnchor(item);
    }*/

}
