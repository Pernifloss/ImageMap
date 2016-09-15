package com.larypipot.cimi.imagemap;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.larypipot.cimi.imagemap.Listener.ItemClickListener;
import com.larypipot.cimi.imagemap.Util.BitmapUtils;
import com.larypipot.cimi.imagemap.View.ImageMapView;

import java.util.ArrayList;
import java.util.List;

public class OperationActivity extends Activity implements ItemClickListener<BoneItem> {

    private ImageMapView imageMapView ;

    private TextView textViewSelection ;

    public static final int NB_ELEMENTS = 11;
    private List<BoneItem> items;
    private PointF[] pointFs = new PointF[]{
            new PointF (0.55413014f, 0.8852321f),
            new PointF(0.47312737f,0.2281924f),
            new PointF(0.7258657f,0.46690002f),
            new PointF(0.4931545f,0.573822f),
            new PointF(0.51643884f,0.45194212f),
            new PointF(0.4382141f,0.29778966f),
            new PointF(0.29861376f,0.36735255f),
            new PointF(0.5429275f,0.30569845f),
            new PointF(0.3812865f,0.6557617f),
            new PointF(0.4198726f,0.3800899f),
            new PointF(0.55254054f,0.0431957f)
    };
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
    private BoneItem selectedAnswer;
    private SoundPoolPlayer soundPoolPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        items = new ArrayList<>();
        for (int i = 0; i < NB_ELEMENTS; i++) {
            items.add(new BoneItem(names[i], pointFs[i],BitmapUtils.resAsBitmap(getApplicationContext(),resID[i]),BitmapUtils.resAsBitmap(getApplicationContext(),resIDEmpty[i])));
        }

        soundPoolPlayer = new SoundPoolPlayer(this);
        imageMapView = (ImageMapView) findViewById(R.id.imageMapViewFront);
        textViewSelection = (TextView) findViewById(R.id.textViewSelection);
        updateText();
        imageMapView.setAdapter(new BoneAdapter(items,this));

        imageMapView.getAdapter().setItemClickListener(this);
    }
    private void updateText() {
        textViewSelection.setText(String.format(getResources().getText(R.string.pick_the_bone).toString(),items.get(i).getText()));
    }

    int i = 0;
    @Override
    public void onMapItemClick(BoneItem item) {
        if (i< NB_ELEMENTS&&items.get(i).equals(item)) {
            ((BoneAdapter)  imageMapView.getAdapter()).selectedItem(item, true);
            i++;
            if (i< NB_ELEMENTS ){
                soundPoolPlayer.playShortResource(R.raw.win);
                updateText();
            }
        }else {
            soundPoolPlayer.playShortResource(R.raw.lose);
        }
    }


}
