package com.zechassault.zonemap.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zechassault.zonemap.Adapter.MapAdapter;
import com.zechassault.zonemap.Listener.AdapterListener;
import com.zechassault.zonemap.R;
import com.zechassault.zonemap.Util.BitmapUtils;

import java.util.HashMap;
import java.util.Map;


public class ImageMapView extends View {


    protected static float startX;
    protected static float startY;
    protected static float ratio;

    private static int backgroundWidth;
    private static int backgroundHeight;

    protected Bitmap background;
    protected int WIDTH;
    protected int HEIGHT;
    protected MapAdapter adapter;
    protected Paint paint;
    protected Rect destination;
    protected Handler handler = new Handler();

    Map<Rect, Object> bitmapClickable = new HashMap<>();

    /**
     * define weather or not a click on a transparent pixel trigger item click
     */
    private boolean allowTransparent = true;

    /**
     * define weather or not zone bitmap scale to background image
     */
    private boolean scaleToBackground = true;

    public ImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.com_zechassault_zonemap_ImageMapView, 0, 0);
        try {
            int src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

            background = BitmapUtils.resAsBitmap(getContext(), src_resource);
        } finally {
            typedArray.recycle();
        }

        paint = new Paint();
    }

    /**
     * Define destination rectangle to fit center an image
     *
     * @param bitmap       the image to fit
     * @param canvasWidth  the width of the rectangle in which the image will be fitted
     * @param canvasHeight the height of the rectangle in which the image will be fitted
     * @return the rectangle that will contain the fitted bitmap
     */
    static Rect getDestinationRect(Bitmap bitmap, int canvasWidth, int canvasHeight) {
        ratio = (float) bitmap.getHeight() / canvasHeight;
        startX = canvasWidth / 2 - bitmap.getWidth() / ratio / 2;
        startY = canvasHeight / 2 - bitmap.getHeight() / ratio / 2;

        backgroundWidth = Math.round(bitmap.getWidth() / ratio);
        backgroundHeight = Math.round(bitmap.getHeight() / ratio);

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
    public static boolean doesIntersect(int x, int y, Rect rect) {
        if (rect != null) {
            return !(x < rect.left || x > rect.right || y < rect.top || y > rect.bottom);
        }
        return false;
    }

    public void setScaleToBackground(boolean scaleToBackground) {
        this.scaleToBackground = scaleToBackground;
    }

    public void setAllowTransparent(boolean allowTransparent) {
        this.allowTransparent = allowTransparent;
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
        if (background != null) {
            canvas.drawBitmap(background, null, destination, paint);
        }
        bitmapClickable.clear();
        addAllPins(canvas);
    }

    /**
     * Display all pins at their respective position onto a canvas
     *
     * @param canvas Canvas to draw on to
     */
    protected void addAllPins(Canvas canvas) {
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                Object item = adapter.getItemAtPosition(i);
                PointF location = getLocation(item);

                Bitmap bitmap = adapter.getItemBitmap(item);
                if (bitmap != null) {

                    int bitmapWidth = Math.round(bitmap.getWidth() / (scaleToBackground ? ratio : 1));
                    int bitmapHeight = Math.round(bitmap.getHeight() / (scaleToBackground ? ratio : 1));
                    int roundX = Math.round(location.x - (bitmapWidth / 2));
                    int roundY = Math.round(location.y - (bitmapHeight / 2));

                    Rect destinationRect = new Rect(
                            roundX,
                            roundY,
                            roundX + bitmapWidth,
                            roundY + bitmapHeight);


                    Rect sourceRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

                    canvas.drawBitmap(
                            bitmap,
                            sourceRect,
                            destinationRect,
                            paint);
                    bitmapClickable.put(destinationRect, item);
                }
            }
        }
    }

    /**
     * Return the location of an item, scalled with the background image
     *
     * @param item
     * @return
     */
    protected PointF getLocation(Object item) {
        float x = adapter.getItemLocation(item).x;
        float y = adapter.getItemLocation(item).y;
        return new PointF(startX + (backgroundWidth * x), startY + (backgroundHeight * y));
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            for (Rect rect : bitmapClickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    if (adapter.itemClickListener != null) {
                        if (allowTransparent) {
                            adapter.itemClickListener.onMapItemClick(bitmapClickable.get(rect));
                        } else {
                            int pixel = getPixelClickedAt(x, y, rect, adapter.getItemBitmap(bitmapClickable.get(rect)));
                            if (pixel != 0) {
                                adapter.itemClickListener.onMapItemClick(bitmapClickable.get(rect));
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

    /**
     * @param x      horizontal coordinate of taped location
     * @param y      vertical coordinate of taped location
     * @param rect   rectangle encapsulating clicked bitmap
     * @param bitmap blicked bitmap
     * @return the pixel
     */
    private int getPixelClickedAt(int x, int y, Rect rect, Bitmap bitmap) {
        try {

            if (scaleToBackground) {
                return bitmap.getPixel(Math.round((x - rect.left) * ratio), Math.round((y - rect.top) * ratio));
            }
            return bitmap.getPixel(x - rect.left, y - rect.top);
        } catch (IllegalArgumentException i) {
            return 0;
        }

    }

    public MapAdapter getAdapter() {
        return adapter;
    }

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
    }
}
