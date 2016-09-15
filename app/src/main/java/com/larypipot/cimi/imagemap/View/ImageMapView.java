package com.larypipot.cimi.imagemap.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.larypipot.cimi.imagemap.Util.BitmapUtils;
import com.larypipot.cimi.imagemap.Listener.AdapterListener;
import com.larypipot.cimi.imagemap.Adapter.MapAdapter;
import com.larypipot.cimi.imagemap.Adapter.NoteImageAdapter;
import com.larypipot.cimi.imagemap.R;

import java.util.HashMap;
import java.util.Map;


public class ImageMapView extends View {

    private static final String TAG = "ImageMapView";
    private static int backgroundWidth;
    private static int backgroundHeight;
    Map<Rect, Object> clickable = new HashMap<>();
    private LayoutInflater layoutInflater;
    protected Bitmap background;
    protected Bitmap unselected;
//    protected Set<Object> items = new HashSet<>();
    protected int WIDTH;
    protected int HEIGHT;
    protected MapAdapter adapter;
    protected Paint paint;
    protected Paint paintSelected;
    protected Rect destination;

    // protected Set<Object> selectedItems = new HashSet<>();

    protected Handler handler = new Handler();
    private static float startX;
    private static float startY;

    public void setAdapter(MapAdapter adapter) {
        this.adapter = adapter;
        adapter.listener = new AdapterListener() {
            @Override
            public void notifyDataSetHasChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageMapView.this.invalidate();
                    }
                });
            }
        };
    //    items = new HashSet<>();
    //    for (int i = 0; i < adapter.getCount(); i++) {
      //      items.add(adapter.getItemAtPosition(i));
       // }
    }

    public ImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.com_larypipot_cimi_imagemap_ImageMapView, 0, 0);
        try {
            background = BitmapUtils.resAsBitmap(getContext(), typedArray.getResourceId(R.styleable.com_larypipot_cimi_imagemap_ImageMapView_backgroundDrawable, 0));
        } finally {
            typedArray.recycle();
        }

        paint = new Paint();
    }

    /**
     * define destination rectangle to fit center an image
     *
     * @param bitmap       the image to fit
     * @param canvasWidth  the width of the rectangle in which the image will be fittted
     * @param canvasHeight the height of the rectangle in which the image will be fittted
     * @return the rectangle that will contain the fitted bitmap
     */
    static Rect getDestinationRect(Bitmap bitmap, int canvasWidth, int canvasHeight) {
        float min = 0;
        startX = 0;
        startY = 0;

        if (bitmap.getWidth() > canvasWidth || bitmap.getHeight() > canvasHeight) {
            if (bitmap.getWidth() / canvasWidth > bitmap.getHeight() / canvasHeight) {
                min = (float) bitmap.getWidth() / canvasWidth;
                startY = canvasHeight / 2 - bitmap.getHeight() / min / 2;
            } else {
                min = (float) bitmap.getHeight() / canvasHeight;
                startX = canvasWidth / 2 - bitmap.getWidth() / min / 2;
            }
        } else {
            if (canvasWidth - bitmap.getWidth() < canvasHeight - bitmap.getHeight()) {
                min = (float) bitmap.getWidth() / canvasWidth;
                startY = canvasHeight / 2 - bitmap.getHeight() / min / 2;
            } else {
                min = (float) bitmap.getWidth() / canvasHeight;
                startX = canvasWidth / 2 - bitmap.getHeight() / min / 2;
            }
        }
        backgroundWidth = Math.round(bitmap.getWidth() / min);
        backgroundHeight = Math.round(bitmap.getHeight() / min);
        return new Rect(Math.round(startX), Math.round(startY), backgroundWidth + Math.round(startX), backgroundHeight + Math.round(startY));
    }

    /**
     * Check if coordinates are inside a rectangle
     *
     * @param x    x coordinate
     * @param y    y coordinate
     * @param rect Rect the rect in which we check if x,y are inside
     * @return if the coordinate are inside the rectangle
     */
    static boolean doesIntersect(int x, int y, Rect rect) {
        if (rect != null) {
            int right = rect.right;
            int top = rect.top;
            int bottom = rect.bottom;
            int left = rect.left;
            return !(x < left || x > right || y < top || y > bottom);
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.WIDTH = w;
        this.HEIGHT = h;
        if (background != null) {
            destination = getDestinationRect(background, w, h);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        //canvas.drawText("");
        if (background != null) {
            canvas.drawBitmap(background, null, destination, paint);
        }
        // drawnPoints(canvas);
        addAllPins(canvas);
    }

    Paint usingPaint;


    /**
     * Display all pins at their respective position onto a canvas
     *
     * @param canvas Canvas to draw on to
     */
    private void addAllPins(Canvas canvas) {
        for (int i = 0; i < adapter.getCount(); i++) {
            Object item = adapter.getItemAtPosition(i);
            PointF location = getLocation(item);

            Bitmap bitmap = adapter.getItemBitmap(item);// ? selected : unselected;
            if (bitmap != null) {
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                int roundx = Math.round(location.x - (bitmapWidth / 2));
                int roundy = Math.round(location.y - (bitmapHeight / 2));

                Rect rect = new Rect(
                        roundx,
                        roundy,
                        roundx + bitmapWidth,
                        roundy + bitmapHeight);

                canvas.drawBitmap(
                        bitmap,
                        location.x - (bitmapWidth / 2),
                        location.y - (bitmapHeight / 2),
                        paint);
                clickable.put(rect, item);
            }

        }
    }


    private Bitmap resAsBitmap(int resID) {
        return BitmapUtils.resAsBitmap(getContext(), resID);
    }

    private Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        return BitmapUtils.convertToBitmap(drawable, widthPixels, heightPixels);
    }

    protected PointF getLocation(Object item) {
        //TODO use real Position
        float x = adapter.getItemLocation(item).x;
        float y = adapter.getItemLocation(item).y;
        return new PointF(startX + (backgroundWidth * x), startY + (backgroundHeight * y));

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            Log.e(TAG, "onTouchEvent: x:"+((motionEvent.getX()-startX) /backgroundWidth)+" y:"+((motionEvent.getY()-startY)/backgroundHeight) );
            for (Rect rect : clickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    if (adapter.itemClickListener!=null){
                        adapter.itemClickListener.onMapItemClick(clickable.get(rect));
                    }
                }
            }
        }
        return false;
    }

    public MapAdapter getAdapter() {
        return adapter;
    }
}
