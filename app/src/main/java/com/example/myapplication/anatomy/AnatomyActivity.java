package com.example.myapplication.anatomy;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.zechassault.zonemap.util.BitmapUtils;
import com.zechassault.zonemap.view.NoteImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class AnatomyActivity extends Activity {

    /**
     * Front view items
     */
    private List<AnatomyItem> pointsFront;

    /**
     * Back view items
     */
    private List<AnatomyItem> pointsBack;

    /**
     * Back image map view
     */
    private NoteImageView imageMapViewBack;

    /**
     * Front image map view
     */
    private NoteImageView imageMapViewFront;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.anatomy_activity);

        /*Create data to populate adapter */
        setUpData();

        imageMapViewBack = (NoteImageView) findViewById(R.id.imageMapView);
        imageMapViewBack.setAdapter(new NoteImageAdapterImpl(pointsBack, this));
        // Set ImageMapView check tap location and only trigger select if visible pixel is hit
        imageMapViewBack.setAllowTransparent(false);

        imageMapViewFront = (NoteImageView) findViewById(R.id.imageMapViewFront);
        imageMapViewFront.setAdapter(new NoteImageAdapterImpl(pointsFront, this));

        // Set ImageMapView check tap location and only trigger select if visible pixel is hit
        imageMapViewFront.setAllowTransparent(false);

    }

    private void setUpData() {
        pointsFront = new ArrayList<>();

        pointsFront.add(new AnatomyItem("Abs", new PointF(0.493f, 0.385f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.abs_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.abs))));
        pointsFront.add(new AnatomyItem("Abductor", new PointF(0.505f, 0.54f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.adductor_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.adductor))));
        pointsFront.add(new AnatomyItem("Quadriceps", new PointF(0.658f, 0.575f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quad_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quad))));
        pointsFront.add(new AnatomyItem("Face", new PointF(0.5f, 0.075f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.face_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.face))));
        pointsFront.add(new AnatomyItem("Arms", new PointF(0.513f, 0.273f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arms_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.arms))));
        pointsFront.add(new AnatomyItem("Shoulders", new PointF(0.405f, 0.209f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shoulders_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shoulders))));
        pointsFront.add(new AnatomyItem("Nose", new PointF(0.515f, 0.093f),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nose_col)),
                BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nose))));

        pointsBack = new ArrayList<>();

        pointsBack.add(new AnatomyItem("Calves", new PointF(0.420f, 0.780f), null, null));
        pointsBack.add(new AnatomyItem("Head", new PointF(0.5f, 0.074f), null, null));
        pointsBack.add(new AnatomyItem("Finger", new PointF(0.82f, 0.530f), null, null));
        pointsBack.add(new AnatomyItem("Wrist", new PointF(0.19f, 0.47f), null, null));
        pointsBack.add(new AnatomyItem("Hand", new PointF(0.19f, 0.53f), null, null));
        pointsBack.add(new AnatomyItem("Back", new PointF(0.5f, 0.35f), null, null));
        pointsBack.add(new AnatomyItem("Ankle", new PointF(0.43f, 0.92f), null, null));
        pointsBack.add(new AnatomyItem("Foot", new PointF(0.60f, 0.96f), null, null));
        pointsBack.add(new AnatomyItem("Gluts", new PointF(0.54f, 0.48f), null, null));
    }

}
