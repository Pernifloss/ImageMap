package com.zechassault.cimi.imagemap.operation;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.zechassault.zonemap.Adapter.MapAdapter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Very simple implementation of MapAdapter
 */
public class BoneAdapter extends MapAdapter<BoneItem> {
    // the list of ous item to display
    private final List<BoneItem> items;

    // list of picked item
    private Set<BoneItem> pickedItem = new HashSet<>();


    public BoneAdapter(List<BoneItem> items) {
        this.items = items;
    }

    /*
     Tell adapter how to get item coordinate
     */
    @Override
    public PointF getItemCoordinates(BoneItem item) {
        return new PointF(item.x, item.y);
    }

    /*
    Tell adapter how to retrieve an item based on its position
    */
    @Override
    public BoneItem getItemAtPosition(int position) {
        return items.get(position);
    }

    /*
    Tell adapter how many item in total we have
    */
    @Override
    public int getCount() {
        return items.size();
    }


    @Override
    public Bitmap getItemBitmap(BoneItem item) {
        if (pickedItem.contains(item)) {
            // if the item is already picked it will item appear as hole without bone
            return item.bitmapEmpty;
        }
        // The bitmap of the item with the bone
        return item.bitmap;
    }

    /**
     * Pick an item
     * @param item BoneItem to pick
     */
    public void pickItem(BoneItem item) {
        //update the list of picked items
        pickedItem.add(item);
        //Refresh the view
        notifyDataSetHasChanged();
    }
}
