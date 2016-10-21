package com.zechassault.zonemap.Adapter;

import android.graphics.Bitmap;
import android.graphics.PointF;

import com.zechassault.zonemap.Listener.AdapterListener;
import com.zechassault.zonemap.Listener.ItemClickListener;

public abstract class MapAdapter<T> {
    /**
     * listener used to communicate from adapter to view
     */
    public AdapterListener listener;

    public void setItemClickListener(ItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * The listener of item click on map
     */
    public ItemClickListener<T> itemClickListener;

    /**
     * Define this function to target where the item is positioned on the image
     *
     * @param item Item to get position
     * @return PointF(x, y) x and y are float 0 to 1
     * x is ratio of image width (e. x = 0.5f  item is centered horizontally)
     * y is ratio of image height (e. y =  1f  item will be at bottom)
     */
    public abstract PointF getItemLocation(T item);

    /**
     * Define this function to indicate which item the given position correspond to
     *
     * @param position int, index of item
     * @return the item corresponding to the position index
     */
    public abstract T getItemAtPosition(int position);

    /**
     * define this function to specifie the number of item you want to display
     *
     * @return int, the number of item to display
     */

    public abstract int getCount();

    /**
     * Override this method to a custom bitmap to draw for each element
     *
     * @param item the item of which the bitmap will correspond
     * @return the Bitmap to draw for given item
     */
    public abstract Bitmap getItemBitmap(T item);

    /**
     * Call this method to notify a change occurred on data
     */
    public void notifyDataSetHasChanged() {
        if (listener != null) {
            listener.notifyDataSetHasChanged();
        }
    }
}