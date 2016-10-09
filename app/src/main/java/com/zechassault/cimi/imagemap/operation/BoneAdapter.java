package com.zechassault.cimi.imagemap.operation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.zechassault.zonemap.Adapter.MapAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoneAdapter extends MapAdapter<BoneItem> {
    private final List<BoneItem> items;
    private final Context context;
    private Set<BoneItem> selectedItems = new HashSet<>();

    public void selectedItem(BoneItem item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        notifyDataSetHasChanged();
    }



    public BoneAdapter(List<BoneItem> items, Context context) {
        this.context = context;
        this.items = items;

    }

    @Override
    public PointF getItemLocation(BoneItem item) {
        return new PointF(item.x,item.y);
    }

    @Override
    public BoneItem getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Bitmap getItemBitmap(BoneItem item) {
        if (selectedItems.contains(item)){
            return item.bitmapEmpty;
        }
        return item.bitmap;
    }

}
