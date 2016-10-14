package com.zechassault.cimi.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;

import com.zechassault.zonemap.Adapter.NoteImageAdapter;
import com.zechassault.zonemap.Util.BitmapUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteImageAdapterImpl extends NoteImageAdapter<Item> {
    private final List<Item> items;
    private final Context context;
    private final Bitmap quad;
    private final Bitmap colQuad;
    private final Bitmap abs;
    private final Bitmap abs_col;
    private final Bitmap adductor;
    private final Bitmap arm_col;
    private final Bitmap arm;
    private final Bitmap nose_col;
    private final Bitmap nose;
    private final Bitmap face_col;
    private final Bitmap face;
    private final Bitmap shoulders;
    private final Bitmap shoulders_col;
    private Set<Item> selectedItems = new HashSet<>();
    private Paint labelPaintUnselected;
    private Paint labelPaintSelected;
    private Paint paint;
    private Bitmap selectedBitmap;
    private Bitmap unselectedBitmap;
    private Bitmap adductor_col;

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
        labelPaintUnselected.setStrokeWidth(5);
        labelPaintUnselected.setTextSize(30);

        labelPaintSelected = new Paint();
        labelPaintSelected.setAntiAlias(true);
        labelPaintSelected.setFakeBoldText(true);
        labelPaintSelected.setTextSize(30);
        labelPaintSelected.setStrokeWidth(5);
        labelPaintSelected.setColor(ContextCompat.getColor(context, R.color.colorAccent));

        colQuad = BitmapUtils.resAsBitmap(context, R.drawable.quad_col);
        quad = BitmapUtils.resAsBitmap(context, R.drawable.quad);
        abs = BitmapUtils.resAsBitmap(context, R.drawable.abs);
        abs_col = BitmapUtils.resAsBitmap(context, R.drawable.abs_col);
        adductor_col = BitmapUtils.resAsBitmap(context, R.drawable.adductor_col);
        adductor = BitmapUtils.resAsBitmap(context, R.drawable.adductor);

        arm_col = BitmapUtils.resAsBitmap(context, R.drawable.arms_col);
        arm = BitmapUtils.resAsBitmap(context, R.drawable.arms);
        nose_col = BitmapUtils.resAsBitmap(context, R.drawable.nose_col);
        nose = BitmapUtils.resAsBitmap(context, R.drawable.nose);
        face_col = BitmapUtils.resAsBitmap(context, R.drawable.face_col);
        face = BitmapUtils.resAsBitmap(context, R.drawable.face);

        shoulders_col = BitmapUtils.resAsBitmap(context, R.drawable.shoulders_col);
        shoulders = BitmapUtils.resAsBitmap(context, R.drawable.shoulders);

        selectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);

        unselectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_unselected);
    }

    @Override
    public PointF getItemLocation(Item item) {
        return new PointF(item.x, item.y);
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
        if (item.getText().equals("Quadriceps")) {
            if (selectedItems.contains(item)) {
                return colQuad;
            } else {
                return quad;
            }
        } else if (item.getText().equals("Abdominaux")) {
            if (selectedItems.contains(item)) {
                return abs_col;
            } else {
                return abs;
            }
        } else if (item.getText().equals("Adducteur")) {
            if (selectedItems.contains(item)) {
                return adductor_col;
            } else {
                return adductor;
            }
        } else if (item.getText().equals("Bras")) {
            if (selectedItems.contains(item)) {
                return arm_col;
            } else {
                return arm;
            }
        } else if (item.getText().equals("Nez")) {
            if (selectedItems.contains(item)) {
                return nose_col;
            } else {
                return nose;
            }
        } else if (item.getText().equals("Visage")) {
            if (selectedItems.contains(item)) {
                return face_col;
            } else {
                return face;
            }
        } else if (item.getText().equals("Epaule")) {
            if (selectedItems.contains(item)) {
                return shoulders_col;
            } else {
                return shoulders;
            }
        } else if (selectedItems.contains(item)) {
            return selectedBitmap;
        }
        return unselectedBitmap;
    }


    @Override
    public Paint getLabelPaint(Item item) {
        if (selectedItems.contains(item)) {
            return labelPaintSelected;
        }
        return labelPaintUnselected;
    }

    @Override
    public PointF getAnchor(Item item) {
        if (item.getText().equals("Bras")) {
            return new PointF(0.95f, 0.6f);
        } else if (item.getText().equals("Nez")) {
            return new PointF(0.25f, 0.7f);
        } else if (item.getText().equals("Epaule")) {
            return new PointF(0.33f, 0.5f);
        }else if (item.getText().equals("Quadriceps")) {
            return new PointF(0.1f, 0.5f);
        }
        return super.getAnchor(item);
    }

}
