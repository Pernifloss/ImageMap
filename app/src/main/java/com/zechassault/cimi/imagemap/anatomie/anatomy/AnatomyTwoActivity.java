package com.zechassault.cimi.imagemap.anatomie.anatomy;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Listener.ItemClickListener;
import com.zechassault.zonemap.View.NoteImageView;

import java.util.ArrayList;
import java.util.List;

import static com.zechassault.cimi.imagemap.anatomie.anatomy.TYPE.JOINT;
import static com.zechassault.cimi.imagemap.anatomie.anatomy.TYPE.MUSCLE;

public class AnatomyTwoActivity extends Activity implements ItemClickListener<AnatomyTwoItem> {
    private List<AnatomyTwoItem> pointsFront;
    private List<AnatomyTwoItem> pointsBack;
    private List<AnatomyTwoItem> pointProfile;
    private NoteImageView frontChart;
    private NoteImageView backChart;
    private NoteImageView profileChart;
    private ImageView imageNext;
    private ToggleButton toggleButton;
    private ViewFlipper viewFlipper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_anatomie);

        pointsFront = new ArrayList<>();


        pointsFront.add(new AnatomyTwoItem(TYPE.MUSCLE, "Abdominaux", new PointF(0.493f, 0.385f)));
        pointsFront.add(new AnatomyTwoItem(TYPE.MUSCLE, "Adducteur", new PointF(0.505f, 0.54f)));
        pointsFront.add(new AnatomyTwoItem(TYPE.MUSCLE, "Quadriceps", new PointF(0.658f, 0.575f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Visage", new PointF(0.5f, 0.075f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Bras", new PointF(0.513f, 0.273f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Epaule", new PointF(0.405f, 0.209f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Hanche", new PointF(0.65f, 0.48f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Genou", new PointF(0.4f, 0.7f)));
        pointsFront.add(new AnatomyTwoItem(JOINT, "Orteil", new PointF(0.4f, 0.950f)));

        pointsBack = new ArrayList<>();

        pointsBack.add(new AnatomyTwoItem(TYPE.MUSCLE, "Ischio-jambier", new PointF(0.440f, 0.620f)));
        pointsBack.add(new AnatomyTwoItem(TYPE.MUSCLE, "Mollet", new PointF(0.420f, 0.780f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Tête", new PointF(0.5f, 0.074f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Doigt", new PointF(0.82f, 0.530f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Poignet", new PointF(0.19f, 0.47f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Main", new PointF(0.19f, 0.53f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Dos", new PointF(0.5f, 0.35f)));
        pointsBack.add(new AnatomyTwoItem(JOINT, "Cheville", new PointF(0.43f, 0.92f)));
        pointsBack.add(new AnatomyTwoItem(TYPE.MUSCLE, "Fessier", new PointF(0.54f, 0.48f)));


        pointProfile = new ArrayList<>();

        pointProfile.add(new AnatomyTwoItem(JOINT, "Nez", new PointF(0.555f, 0.093f)));
        pointProfile.add(new AnatomyTwoItem(JOINT, "Pied", new PointF(0.515f, 0.96f)));
        pointProfile.add(new AnatomyTwoItem(TYPE.MUSCLE, "Psoas iliaque", new PointF(0.44f, 0.46f)));


        frontChart = (NoteImageView) findViewById(R.id.front);
        backChart = (NoteImageView) findViewById(R.id.back);
        profileChart = (NoteImageView) findViewById(R.id.profile);

        frontChart.setScaleToBackground(false);
        backChart.setScaleToBackground(false);
        profileChart.setScaleToBackground(false);

        final AnatomyNoteImageAdapter adapterFront = new AnatomyNoteImageAdapter(pointsFront, getBaseContext());

        frontChart.setAdapter(adapterFront);
        final AnatomyNoteImageAdapter adapterback = new AnatomyNoteImageAdapter(pointsBack, getBaseContext());
        backChart.setAdapter(adapterback);
        final AnatomyNoteImageAdapter adapterProfile = new AnatomyNoteImageAdapter(pointProfile, getBaseContext());
        profileChart.setAdapter(adapterProfile);

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    adapterFront.setMode(JOINT);
                            adapterback.setMode(JOINT);
                    adapterProfile.setMode(JOINT);
                }else {

                    adapterFront.setMode(MUSCLE);
                    adapterback.setMode(MUSCLE);
                    adapterProfile.setMode(MUSCLE);

                }

            }
        });
        imageNext = (ImageView) findViewById(R.id.imageNext);
        imageNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
    }

    @Override
    public void onMapItemClick(AnatomyTwoItem answer) {

    }


}
