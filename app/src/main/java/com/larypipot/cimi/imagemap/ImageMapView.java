package com.larypipot.cimi.imagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ImageMapView extends View {

    private static final String TAG = "ImageMapView";
    private final Bitmap background;
    private final Bitmap selected;

    public ItemClickListener listener;

    /*private OnTouchListener cancelMoveListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                //propagate touch for layer click only on action down
                motionEvent.setAction(1 & 255);
                mapView.onTouchEvent(motionEvent);
            }
            return true;
        }
    };*/
    Bitmap unselected;
    Map<Rect, Item> clickable = new HashMap<>();
    private Set<Item> items = new HashSet<>();
    private int WIDTH;
    private int HEIGHT;

    private Paint paint;
    private Rect destination;
    private Set<Item> selectedItems = new HashSet<>();
    private int bitmapWidth = 50;
    private int bitmapHeight = 50;
    private Handler handler = new Handler();




    public ImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        unselected = resAsBitmap(R.drawable.anatomy_dot_unselected);
        selected = resAsBitmap(R.drawable.anatomy_dot_selected);
        paint = new Paint();
        background = resAsBitmap(R.drawable.background);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.WIDTH = w;
        this.HEIGHT = h;
        float min = 0;
        float startX = 0;
        float startY = 0;
        if(background.getWidth() > WIDTH || background.getHeight() > HEIGHT){
            if(background.getWidth() / WIDTH > background.getHeight() / HEIGHT){
                min = (float) background.getWidth() / WIDTH;
                startY = HEIGHT/2 - background.getHeight()/min/2;
            }else{
                min = (float) background.getHeight() / HEIGHT;
                startX = WIDTH/2 - background.getWidth()/min/2;
            }
        }else{
            if(WIDTH - background.getWidth() < HEIGHT - background.getHeight()){
                min = (float)  background.getWidth() / WIDTH ;
                startY = HEIGHT/2 - background.getHeight()/min/2;
            }else{
                min = (float) background.getWidth() / HEIGHT;
                startX = WIDTH/2 - background.getHeight()/min/2;
            }
        }

        destination = new Rect(Math.round(startX), Math.round(startY), Math.round(background.getWidth()/min) + Math.round(startX), Math.round(background.getHeight()/min) + Math.round(startY));

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        canvas.drawBitmap(background, null, destination, paint);

        addallItems(canvas);
    }

    private void addallItems(Canvas canvas) {
        for (Item item : items) {
            Log.e(TAG, "addallItems: "+item.x+" y"+item.y );
            PointF location = getLocation(item);
            Bitmap bitmap = selectedItems.contains(item) ? selected : unselected;

            int roundx = Math.round(location.x - (bitmapWidth / 2));
            int roundy = Math.round(location.y - (bitmapHeight / 2));

            Rect rect = new Rect(
                    roundx,
                    roundy,
                    bitmapWidth,
                    bitmapHeight);

            canvas.drawBitmap(
                    bitmap,
                    location.x - (bitmapWidth / 2),
                    location.y - (bitmapHeight / 2),
                    paint);
            clickable.put(rect, item);

        }
    }

    public void selectedItem(Item Item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(Item);
        } else {
            selectedItems.remove(Item);
        }
        this.invalidate();
    }


    private Bitmap resAsBitmap(int resID) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resID);
        return convertToBitmap(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    //FAKE DATA

    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return mutableBitmap;
    }

    public void addItems(final Set<Item> Items) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                ImageMapView.this.items = Items;
                ImageMapView.this.invalidate();
            }
        });
        //    mapView.refresh();
    }

    private PointF getLocation(Item item) {
        //TODO use real Position
            int x = item.x;
            int y =item.y;
            return new PointF((WIDTH / 500f) * x, (HEIGHT / 1000f) * y);
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());

            for (Rect rect : clickable.keySet()) {
                int right = rect.right + rect.left;
                int top = rect.top;
                int bottom = rect.bottom + top;
                int left = rect.left;

                if (!(x < left || x > right || y < top || y > bottom)) {
                    listener.onMapAnswerClick(clickable.get(rect));
                }
            }
        }

        return false;
    }
}
