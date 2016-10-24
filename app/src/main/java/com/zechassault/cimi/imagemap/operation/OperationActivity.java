package com.zechassault.cimi.imagemap.operation;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.zechassault.cimi.imagemap.R;
import com.zechassault.zonemap.Listener.ItemClickListener;
import com.zechassault.zonemap.Util.BitmapUtils;
import com.zechassault.zonemap.View.ImageMapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity That display game of Operation, well, a personal adaptation of the game where the user
 * is asked to tap a specific bone item, if he pick the good one, it is removed (visually) from the
 * image map view and a win sound is played, he can the pick the next item, util he picked them all,
 * if he does not pick the good item a bad sound is played.
 * <p>
 * This activity as no purpose but to be a demo to ImageMapView
 */
public class OperationActivity extends Activity implements ItemClickListener<BoneItem> {

    /* The index of the item we want the user to tap */
    int askedItemIndex = 0;

    /* Data to populate the adapter */
    public static final int NB_ELEMENTS = 11;

    String[] names = new String[]{
            "Ankle",
            "Apple",
            "Arm",
            "Bread",
            "Butterfly",
            "Chicken",
            "Funny",
            "Heart",
            "Horse",
            "Rib",
            "Head"

    };
    int[] resID = new int[]{
            R.drawable.ankle,
            R.drawable.apple,
            R.drawable.arm,
            R.drawable.bread,
            R.drawable.butterfly,
            R.drawable.chicken,
            R.drawable.funny,
            R.drawable.heart,
            R.drawable.horse,
            R.drawable.rib,
            R.drawable.head

    };
    int[] resIDEmpty = new int[]{
            R.drawable.ankle_empty,
            R.drawable.apple_empty,
            R.drawable.arm_empty,
            R.drawable.bread_empty,
            R.drawable.butterfly_empty,
            R.drawable.chicken_empty,
            R.drawable.funny_empty,
            R.drawable.heart_empty,
            R.drawable.horse_empty,
            R.drawable.rib_empty,
            R.drawable.head_empty

    };
    private PointF[] pointFs = new PointF[]{
            new PointF(0.55413014f, 0.8852321f),
            new PointF(0.47312737f, 0.2281924f),
            new PointF(0.7258657f, 0.46690002f),
            new PointF(0.4931545f, 0.573822f),
            new PointF(0.51643884f, 0.45194212f),
            new PointF(0.4382141f, 0.29778966f),
            new PointF(0.29861376f, 0.36735255f),
            new PointF(0.5429275f, 0.30569845f),
            new PointF(0.3812865f, 0.6557617f),
            new PointF(0.4198726f, 0.3800899f),
            new PointF(0.55254054f, 0.0431957f)
    };
    /* Item Map view*/
    private ImageMapView imageMapView;


    /*List of item to show on map view*/
    private List<BoneItem> items;

    private TextView textViewSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_operation);

        // constructing adapter item list to display on image map view
        items = new ArrayList<>();
        for (int i = 0; i < NB_ELEMENTS; i++) {
            items.add(new BoneItem(names[i], pointFs[i]
                    , BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), resID[i]))
                    , BitmapUtils.resAsBitmap(ContextCompat.getDrawable(getApplicationContext(), resIDEmpty[i]))));
        }
        // retrieving view from xml
        imageMapView = (ImageMapView) findViewById(R.id.imageMapViewFront);
        // main image is set in xml android:src="@drawable/background"
        // here we tell the view to no scale image with background
        imageMapView.setScaleToBackground(false);
        // here we tell the view not to considerate transparent pixel as part of the item
        imageMapView.setAllowTransparent(false);

        // setting image map adapter
        imageMapView.setAdapter(new BoneAdapter(items));

        // setting adapter on item tap listener
        imageMapView.getAdapter().setItemClickListener(this);

        soundPoolPlayer = new SoundPoolPlayer(this);

        textViewSelection = (TextView) findViewById(R.id.textViewSelection);
        updateText();
    }

    /*
    setup next item to pick
     */
    private void updateText() {
        textViewSelection.setText(String.format(getResources().getText(R.string.pick_the_bone).toString(), items.get(askedItemIndex).getText()));
    }

    /* Define what happen when an item is clicked */

    @Override
    public void onMapItemClick(BoneItem item) {
        // on this activity we check if the clicked item correspond to the asked one.

        if (askedItemIndex < NB_ELEMENTS && items.get(askedItemIndex).equals(item)) {
            //add item to the list of picked bone
            ((BoneAdapter) imageMapView.getAdapter()).pickItem(item);
            //set next bone to retrieve
            askedItemIndex++;
            if (askedItemIndex < NB_ELEMENTS) {
                updateText();

                soundPoolPlayer.playShortResource(R.raw.win);//don't mind this, for win sound ...

            }
        } else {
            soundPoolPlayer.playShortResource(R.raw.lose);//don't mind this, for win sound ...
        }
    }

    //don't mind this, for win sound ...
    private SoundPoolPlayer soundPoolPlayer;

}
