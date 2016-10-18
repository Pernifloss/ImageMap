package com.zechassault.cimi.imagemap.anatomy;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Listener.ItemClickListener;
import com.zechassault.zonemap.View.NoteImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnatomyActivity extends Activity implements ItemClickListener<AnatomyItem> {
    private List<AnatomyItem> pointsFront;
    private List<AnatomyItem> pointsBack;
    private NoteImageView imageMapView ;
    private ImageView imageViewRotate ;
    private  NoteImageView imageMapViewFront ;
    private Set<AnatomyItem> selected = new HashSet<>();


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
        Log.e("AnatomyTwoActivity", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointsFront = new ArrayList<>();


        pointsFront.add(new AnatomyItem("Abdominaux", new PointF(0.493f, 0.385f)));
        pointsFront.add(new AnatomyItem("Adducteur", new PointF(0.505f, 0.54f)));
        pointsFront.add(new AnatomyItem("Quadriceps", new PointF(0.658f, 0.575f)));
        pointsFront.add(new AnatomyItem("Visage", new PointF(0.5f, 0.075f)));
        pointsFront.add(new AnatomyItem("Bras", new PointF(0.513f, 0.273f)));
        pointsFront.add(new AnatomyItem("Epaule", new PointF(0.405f, 0.209f)));
        pointsFront.add(new AnatomyItem("Hanche", new PointF(0.65f, 0.48f)));
        pointsFront.add(new AnatomyItem("Genou", new PointF(0.4f, 0.7f)));
        pointsFront.add(new AnatomyItem("Orteil", new PointF(0.4f, 0.950f)));
        pointsFront.add(new AnatomyItem("Nez", new PointF(0.515f, 0.093f)));
        pointsFront.add(new AnatomyItem("Psoas iliaque", new PointF(0.44f, 0.46f)));

        pointsBack = new ArrayList<>();

        pointsBack.add(new AnatomyItem("Ischio-jambier", new PointF(0.440f, 0.620f)));
        pointsBack.add(new AnatomyItem("Mollet", new PointF(0.420f, 0.780f)));
        pointsBack.add(new AnatomyItem("Tête", new PointF(0.5f, 0.074f)));
        pointsBack.add(new AnatomyItem("Doigt", new PointF(0.82f, 0.530f)));
        pointsBack.add(new AnatomyItem("Poignet", new PointF(0.19f, 0.47f)));
        pointsBack.add(new AnatomyItem("Main", new PointF(0.19f, 0.53f)));
        pointsBack.add(new AnatomyItem("Dos", new PointF(0.5f, 0.35f)));
        pointsBack.add(new AnatomyItem("Cheville", new PointF(0.43f, 0.92f)));
        pointsBack.add(new AnatomyItem("Pied", new PointF(0.60f, 0.96f)));
        pointsBack.add(new AnatomyItem("Fessier", new PointF(0.54f, 0.48f)));

        imageMapView = (NoteImageView) findViewById(R.id.imageMapView);
        imageMapViewFront = (NoteImageView) findViewById(R.id.imageMapViewFront);
        imageViewRotate = (ImageView) findViewById(R.id.imageViewRotate);

        imageMapView.setAdapter(new NoteImageAdapterImpl(pointsBack,this));
        imageMapViewFront.setAdapter( new NoteImageAdapterImpl(pointsFront,this));
        imageMapViewFront.setAllowTransparent(false);
       // imageMapViewFront.addItems(pointsFront);
        imageMapView.getAdapter().setItemClickListener(this);
        imageMapViewFront.getAdapter().setItemClickListener(this);
//655

        card_flip_left_out = AnimatorInflater.loadAnimator(AnatomyActivity.this, R.animator.card_flip_left_out);


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
    public void onMapItemClick(AnatomyItem answer) {
        if (selected.contains(answer)) {
            selected.remove(answer);
            ((NoteImageAdapterImpl)  imageMapView.getAdapter()).selectedItem(answer, false);
            ((NoteImageAdapterImpl)  imageMapViewFront.getAdapter()).selectedItem(answer, false);
        } else {
            selected.add(answer);
            ((NoteImageAdapterImpl)  imageMapView.getAdapter()).selectedItem(answer, true);
            ((NoteImageAdapterImpl) imageMapViewFront.getAdapter()).selectedItem(answer, true);
        }
    }


}
