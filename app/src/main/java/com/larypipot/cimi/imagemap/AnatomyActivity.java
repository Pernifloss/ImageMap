package com.larypipot.cimi.imagemap;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.larypipot.cimi.zonemap.Listener.ItemClickListener;
import com.larypipot.cimi.zonemap.View.NoteImageView;

import java.util.HashSet;
import java.util.Set;

public class AnatomyActivity extends Activity implements ItemClickListener<Item> {
    private Set<Item> pointsFront;
    private Set<Item> pointsBack;
    private NoteImageView imageMapView ;
    private ImageView imageViewRotate ;
    private  NoteImageView imageMapViewFront ;
    private Set<Item> selected = new HashSet<>();


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
            imageMapView.setVisibility(View.INVISIBLE);
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
            imageMapView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("MainActivity", "onCreate: " );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsFront = new HashSet<>();


        pointsFront.add(new Item("Abdominaux", new PointF(0.5f, 0.33f)));
        pointsFront.add(new Item("Psoas iliaque", new PointF(0.44f, 0.46f)));
        pointsFront.add(new Item("Adducteur", new PointF(0.48f, 0.55f)));
        pointsFront.add(new Item("Quadriceps", new PointF(0.600f, 0.580f)));
        pointsFront.add(new Item("Visage", new PointF(0.5f, 0.064f)));
        pointsFront.add(new Item("Bras", new PointF(0.252f, 0.37f)));
        pointsFront.add(new Item("Epaule", new PointF(0.688f, 0.180f)));
        pointsFront.add(new Item("Hanche", new PointF(0.65f, 0.48f)));
        pointsFront.add(new Item("Genou", new PointF(0.4f, 0.7f)));
        pointsFront.add(new Item("Orteil", new PointF(0.4f, 0.950f)));
        pointsFront.add(new Item("Nez", new PointF(0.5f, 0.120f)));

        pointsBack = new HashSet<>();

        pointsBack.add(new Item("Ischio-jambier", new PointF(0.440f, 0.620f)));
        pointsBack.add(new Item("Mollet", new PointF(0.420f, 0.780f)));
        pointsBack.add(new Item("Tête", new PointF(0.5f, 0.074f)));
        pointsBack.add(new Item("Doigt", new PointF(0.82f, 0.530f)));
        pointsBack.add(new Item("Poignet", new PointF(0.19f, 0.47f)));
        pointsBack.add(new Item("Main", new PointF(0.19f, 0.53f)));
        pointsBack.add(new Item("Dos", new PointF(0.5f, 0.35f)));
        pointsBack.add(new Item("Cheville", new PointF(0.43f, 0.92f)));
        pointsBack.add(new Item("Pied", new PointF(0.60f, 0.96f)));
        pointsBack.add(new Item("Fessier", new PointF(0.54f, 0.48f)));

        imageMapView = (NoteImageView) findViewById(R.id.imageMapView);
        imageMapViewFront = (NoteImageView) findViewById(R.id.imageMapViewFront);
        imageViewRotate = (ImageView) findViewById(R.id.imageViewRotate);

        imageMapView.setAdapter(new AnatomyAdapter(pointsBack,this));
        imageMapViewFront.setAdapter( new AnatomyAdapter(pointsFront,this));
       // imageMapViewFront.addItems(pointsFront);
        imageMapView.getAdapter().setItemClickListener(this);
        imageMapViewFront.getAdapter().setItemClickListener(this);
//655



        card_flip_left_out = AnimatorInflater.loadAnimator(AnatomyActivity.this,R.animator.card_flip_left_out);


        card_flip_left_out.setTarget(imageMapViewFront);
        card_flip_right_in = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_right_in);
        card_flip_right_in.setTarget(imageMapViewFront);
        card_flip_right_in.addListener(hideBack);

        card_flip_left_in = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_left_in);
        card_flip_left_in.setTarget(imageMapView);

        card_flip_left_in.addListener(hideFront);

        card_flip_right_out = AnimatorInflater.loadAnimator(AnatomyActivity.this,
                R.animator.card_flip_right_out);

        card_flip_right_out.setTarget(imageMapView);

        imageViewRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageMapViewFront.setVisibility(View.VISIBLE);
                imageMapView.setVisibility(View.VISIBLE);
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

    @Override
    public void onMapItemClick(Item answer) {
        if (selected.contains(answer)) {
            selected.remove(answer);
            ((AnatomyAdapter)  imageMapView.getAdapter()).selectedItem(answer, false);
            ((AnatomyAdapter)  imageMapViewFront.getAdapter()).selectedItem(answer, false);
        } else {
            selected.add(answer);
            ((AnatomyAdapter)  imageMapView.getAdapter()).selectedItem(answer, true);
            ((AnatomyAdapter) imageMapViewFront.getAdapter()).selectedItem(answer, true);
        }
    }


}
