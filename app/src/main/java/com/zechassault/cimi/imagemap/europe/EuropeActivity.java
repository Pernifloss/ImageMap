package com.zechassault.cimi.imagemap.europe;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zechassault.cimi.imagemap.AnatomyAdapter;
import com.zechassault.cimi.imagemap.Item;
import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Listener.ItemClickListener;
import com.zechassault.zonemap.Util.BitmapUtils;
import com.zechassault.zonemap.View.NoteImageView;

import java.util.HashSet;
import java.util.Set;

public class EuropeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_europe);


        Set<Item> points = new HashSet<>();


        NoteImageView imageMapView = (NoteImageView) findViewById(R.id.imageMapView);
        Set<Country> countries = new HashSet<>();
        countries.add(new Country("France",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.france_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.france),
                0.313f,
                0.528f
        ));
        Country italia = new Country("Italia",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.italia_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.italia),
                0.73f,
                0.695f
        );
        italia.anchor = new PointF(0.7f,0.5f);
        countries.add(italia);

        countries.add(new Country("United Kingdom",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.uk_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.uk),
                0.085f,
                0.281f
        ));

        countries.add(new Country("Switzerland",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.switzerland_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.switzerland),
                0.549f,
                0.506f
        ));


        countries.add(new Country("Germany",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.germany_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.germany),
                0.684f ,
                0.343f
        ));
      countries.add(new Country("Luxembourg",
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.luxembourg_s),
                BitmapUtils.resAsBitmap(getApplicationContext(), R.drawable.luxembourg),
              0.42987353f,
              0.39199176f
        ));


        imageMapView.setAdapter(new CountryAdapter(countries, getApplicationContext()));
        imageMapView.setAllowTransparent(false);


    }
}
