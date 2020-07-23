package com.example.myapplication.europe;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.anatomy.AnatomyItem;
import com.zechassault.zonemap.util.BitmapUtils;
import com.zechassault.zonemap.view.NoteImageView;

import java.util.HashSet;
import java.util.Set;

import androidx.core.content.ContextCompat;

public class EuropeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_europe);

        Set<AnatomyItem> points = new HashSet<>();

        NoteImageView imageMapView = (NoteImageView) findViewById(R.id.imageMapView);
        Set<Country> countries = new HashSet<>();
        Context context = getApplicationContext();
        countries.add(new Country("France",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.france_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.france)),
                0.313f,
                0.528f
        ));
        Country italia = new Country("Italia",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.italia_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.italia)),
                0.73f,
                0.695f
        );
        italia.anchor = new PointF(0.7f,0.5f);
        countries.add(italia);

        countries.add(new Country("United Kingdom",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.uk_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.uk)),
                0.085f,
                0.281f
        ));

        countries.add(new Country("Switzerland",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.switzerland_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.switzerland)),
                0.549f,
                0.506f
        ));


        countries.add(new Country("Germany",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.germany_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.germany)),
                0.684f ,
                0.343f
        ));
      countries.add(new Country("Luxembourg",
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.luxembourg_s)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(context, R.drawable.luxembourg)),
              0.42987353f,
              0.39199176f
        ));


        imageMapView.setAdapter(new CountryAdapter(countries, getApplicationContext()));
        imageMapView.setAllowTransparent(false);


    }
}
