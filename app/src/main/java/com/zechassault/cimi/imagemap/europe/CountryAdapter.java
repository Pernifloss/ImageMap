package com.zechassault.cimi.imagemap.europe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;

import com.zechassault.zonemap.Adapter.NoteImageAdapter;
import com.zechassault.zonemap.Listener.ItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountryAdapter extends NoteImageAdapter<Country> {
    private final List<Country> countries = new ArrayList<>();
    private Set<Country> selected = new HashSet<>();


    public CountryAdapter(Set<Country> countries, Context applicationContext) {
        this.countries.addAll(countries);
        setItemClickListener(new ItemClickListener<Country>() {
            @Override
            public void onMapItemClick(Country item) {
                if (selected.contains(item)) {
                    selected.remove(item);
                } else {
                    selected.add(item);
                }
                notifyDataSetHasChanged();
            }
        });
    }


    @Override
    public String getLabel(Country item) {
        return item.label;
    }

    @Override
    public PointF getItemCoordinates(Country item) {
        return item.location;
    }

    @Override
    public Country getItemAtPosition(int position) {
        return countries.get(position);
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public Bitmap getItemBitmap(Country item) {
        if (selected.contains(item)) {
            return item.selectedBitmap;
        }
        return item.unselectedBitmap;
    }

    @Override
    public PointF getAnchor(Country item) {
        return item.anchor;
    }
}
