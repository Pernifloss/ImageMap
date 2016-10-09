package com.zechassault.cimi.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.zechassault.zonemap.Adapter.MapAdapter;
import com.zechassault.zonemap.Util.BitmapUtils;

import java.util.List;
import java.util.Set;

public class AnatomyAdapter extends MapAdapter<Item> {
    private final Set<Item> items;
    private final Context context;
    private Set<Item> selectedItems;
    private Bitmap selectedBitmap;
    private Bitmap unselectedBitmap;

    private List<Bitmap> bitmapList;
    private List<Bitmap> bitmapEmptyList;

    public void selectedItem(Item item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }


        notifyDataSetHasChanged();
    }


    public AnatomyAdapter(Set<Item> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PointF getItemLocation(Item item) {
        return new PointF(item.x, item.y);
    }

    @Override
    public Item getItemAtPosition(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }



    @Override
    public Bitmap getItemBitmap(Item item) {
        if (selectedItems.contains(item)) {
            if (selectedBitmap == null) {
                selectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);
            }
            return selectedBitmap;
        }
        if (unselectedBitmap == null) {
            unselectedBitmap = BitmapUtils.resAsBitmap(context, R.drawable.anatomy_dot_selected);
        }
        return unselectedBitmap;
    }
}
