package com.zechassault.cimi.imagemap.anatomy;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Util.BitmapUtils;
import com.zechassault.zonemap.View.NoteImageView;

import java.util.ArrayList;
import java.util.List;

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

        setContentView(R.layout.activity_main);

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

        /* Don't mind this part, for rotation animation purpose ... */

        card_flip_left_out = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_left_out);
        card_flip_left_out.setTarget(imageMapViewFront);
        card_flip_right_in = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_right_in);
        card_flip_right_in.setTarget(imageMapViewFront);
        card_flip_right_in.addListener(hideBack);
        card_flip_left_in = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_left_in);
        card_flip_left_in.setTarget(imageMapViewBack);
        card_flip_left_in.addListener(hideFront);
        card_flip_right_out = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_right_out);
        card_flip_right_out.setTarget(imageMapViewBack);

        ImageView imageViewRotate = (ImageView) findViewById(R.id.imageViewRotate);
        imageViewRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMapViewFront.setVisibility(View.VISIBLE);
                imageMapViewBack.setVisibility(View.VISIBLE);
                if (isFrontShown) {
                    card_flip_left_out.start();
                    card_flip_left_in.start();
                } else {
                    card_flip_right_out.start();
                    card_flip_right_in.start();
                }
                isFrontShown = !isFrontShown;
            }
        });


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

    /* Don't mind this part, for rotation animation purpose ... */
    private Animator card_flip_left_out;
    private Animator card_flip_left_in;
    private Animator card_flip_right_in;
    private Animator card_flip_right_out;
    private boolean isFrontShown = true;
    private Animator.AnimatorListener hideBack = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            imageMapViewBack.setVisibility(View.INVISIBLE);
            imageMapViewFront.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };
    private Animator.AnimatorListener hideFront = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            imageMapViewFront.setVisibility(View.INVISIBLE);
            imageMapViewBack.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    };

}
