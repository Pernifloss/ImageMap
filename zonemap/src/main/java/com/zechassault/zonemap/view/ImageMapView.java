package com.zechassault.zonemap.view;

import android.content.Context;
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

import com.zechassault.zonemap.adapter.MapAdapter;
import com.zechassault.zonemap.listener.AdapterListener;
import com.zechassault.zonemap.util.BitmapUtils;

import java.util.HashMap;
import java.util.Map;


public class ImageMapView extends View {

    /**
     * Main image bitmap
     */
    protected Bitmap background;

    /**
     * Padding between start of canvas and start of main image
     */
    protected static float startX;

    /**
     * Padding between top of canvas and top of main image
     */
    protected static float startY;

    /**
     * Image to canvas ratio
     */
    protected static float ratio;

    /**
     * Main image width scaled inside the canvas (fit center)
     */
    private static int backgroundWidth;

    /**
     * Main image height scaled inside the canvas (fit center)
     */
    private static int backgroundHeight;

    /**
     * View width
     */
    protected int WIDTH;
    /**
     * View height
     */
    protected int HEIGHT;

    /**
     * Adapter to populate the map with items
     */
    protected MapAdapter adapter;

    /**
     * Paint used to draw bitmaps
     */
    protected Paint paint;

    /**
     * Zone of canvas designated for the main image
     */
    protected Rect destination;

    /**
     * Android Ui thread andler
     */
    protected Handler handler = new Handler();

    /**
     * Collection of clickable rectangle that contain item bitmap
     */
    protected Map<Rect, Object> bitmapClickable = new HashMap<>();

    /**
     * Define weather or not a click on a transparent pixel trigger item click
     */
    private boolean allowTransparent = true;


    /**
     * Define weather or not zone bitmap scale to background image
     */
    protected boolean scaleToBackground = true;

    private String TAG = "ImageMapView";

    protected boolean uIDebug = false;
    protected boolean debug = false;
    protected final Paint debugPaint;

    /**
     * ImageMapView extending android.view.View
     *
     * @param context android context
     * @param attrs   xml AttributeSet
     */
    public ImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            int src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);

            background = BitmapUtils.resAsBitmap(getResources().getDrawable(src_resource));
        } catch (Exception e) {
            Log.i("ImageMapView", "src not defined in xml ! " + e.getMessage());
        }

        paint = new Paint();

        debugPaint = new Paint();
        debugPaint.setStrokeWidth(3);
        debugPaint.setColor(Color.RED);
        debugPaint.setStyle(Paint.Style.STROKE);
        debugPaint.setTextSize(50);
    }

    /**
     * Define destination rectangle to fit center an image
     *
     * @param bitmap       the image to fit
     * @param canvasWidth  the width of the rectangle in which the image will be fitted
     * @param canvasHeight the height of the rectangle in which the image will be fitted
     * @return the rectangle that will contain the fitted bitmap
     */
    protected Rect getDestinationRect(Bitmap bitmap, int canvasWidth, int canvasHeight) {
        float bitmapRatio = (float) bitmap.getHeight() / (float) bitmap.getWidth();
        float canvasRatio = canvasHeight / canvasWidth;
        // if the bitmap h/w ratio is bigger than the canvas ratio we use width to fit the image
        if (bitmapRatio < canvasRatio) {
            ratio = (float) bitmap.getWidth() / canvasWidth;
        } else {
            ratio = (float) bitmap.getHeight() / canvasHeight;
        }
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
            if (uIDebug) {
                canvas.drawRect(destination, debugPaint);
            }

        }
        bitmapClickable.clear();
        addAllPins(canvas);
    }

    public void setDebug(boolean debug) {
        this.uIDebug = debug;
        this.debug = debug;
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
            if (uIDebug) {
                for (Rect r : bitmapClickable.keySet()) {

                    canvas.drawRect(r
                            , debugPaint);
                    for (int i = 0; i < adapter.getCount(); i++) {
                        if (adapter.getItemAtPosition(i).equals(bitmapClickable.get(r))) {
                            canvas.drawText(i + "", r.left, r.top, debugPaint);
                        }
                    }
                }
            }
        }
    }

    /**
     * Return the location of an item, scaled with the background image
     *
     * @param item the item to place on the image map
     * @return PointF coordinate of item
     */
    protected PointF getLocation(Object item) {
        PointF itemLocation = adapter.getItemCoordinates(item);
        float x = itemLocation.x;
        float y = itemLocation.y;
        return new PointF(startX + (backgroundWidth * x), startY + (backgroundHeight * y));
    }

    /**
     * Intercept touch event and determine whether or not it on an item bitmap
     *
     * @param motionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            debugLog("onTouchEvent tapped pixel : x:"+x+" y: "+y+"");
            debugLog("onTouchEvent tapped relative ratio to background image, ratioX: "+((x-startX)/backgroundWidth)+" ratioY: "+((y-startY)/backgroundHeight));
            for (Rect rect : bitmapClickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    if (adapter.itemClickListener != null) {
                        Object item = bitmapClickable.get(rect);
                        debugLog("onTouchEvent item tapped : "+ item.toString());
                        if (allowTransparent) {
                            adapter.itemClickListener.onMapItemClick(item);
                        } else {
                            int pixel = getPixelClickedAt(x, y, rect, adapter.getItemBitmap(item));
                            if (pixel != 0) {

                                adapter.itemClickListener.onMapItemClick(item);

                            }else {

                                debugLog("onTouchEvent item tapped but on transparent pixel (see setAllowTransparent method) ");
                            }
                        }

                    }
                }
            }
        }
        return false;
    }

    /**
     * Log a string if view is in debug mode
     * @param s the string to log
     */
    protected void debugLog(String s) {
        if (debug){
            Log.d(TAG,s);
        }
    }

    /**
     * Retrieve a specific pixel
     *
     * @param x      horizontal coordinate of taped location
     * @param y      vertical coordinate of taped location
     * @param rect   rectangle encapsulating clicked bitmap
     * @param bitmap clicked bitmap
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

    /**
     * Set view adapter that populate map with items
     *
     * @param adapter item adapter
     */
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

    /**
     * @return current Map adapter
     */
    public MapAdapter getAdapter() {
        return adapter;
    }

    /**
     * @param allowTransparent the value to set to allowTransparent
     */
    public void setAllowTransparent(boolean allowTransparent) {
        this.allowTransparent = allowTransparent;
    }

    /**
     * @param scaleToBackground the new value scaleToBackground
     */
    public void setScaleToBackground(boolean scaleToBackground) {
        this.scaleToBackground = scaleToBackground;
    }
}
