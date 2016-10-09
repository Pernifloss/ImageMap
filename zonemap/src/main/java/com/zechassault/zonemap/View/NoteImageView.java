package com.zechassault.zonemap.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.zechassault.zonemap.Adapter.NoteImageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoteImageView extends ImageMapView {

    private static final String TAG = "NoteImageView";
    public static final int TEXT_MARGIN_RIGHT = 20;

    private final Typeface typeface;
    Paint usingPaint;
    private List<Object> left = new ArrayList<>();
    private List<Object> right= new ArrayList<>();
    private NoteImageAdapter adapter;
    private Paint backgroundPaint;

    Map<Rect, Object> labelClickable = new HashMap<>();

    public NoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);


        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/ARSMaquettePro-Regular.otf");
        backgroundPaint = new Paint();
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
        float startX = 0;
        float startY = 0;

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
        return new Rect(Math.round(startX), Math.round(startY), Math.round(bitmap.getWidth() / min) + Math.round(startX), Math.round(bitmap.getHeight() / min) + Math.round(startY));
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

    public void setAdapter(final NoteImageAdapter adapter) {
        super.setAdapter(adapter);
        NoteImageView.this.adapter = adapter;
        //  NoteImageView.this.items = adapter.getItems();
        left = new ArrayList<>();
        right = new ArrayList<>();
        Map<Float, String> leftMap = new HashMap<>();
        Map<Float, String> rightmapMap = new HashMap<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Object itemAtPosition = adapter.getItemAtPosition(i);
            PointF point = adapter.getItemLocation(itemAtPosition);
            if (point.x < 0.5f) {
                left.add(itemAtPosition);
            } else {
                right.add(itemAtPosition);
            }
        }

        Collections.sort(left, new ItemYComparator());
        Collections.sort(right, new ItemYComparator());

        handler.post(new Runnable() {
            @Override
            public void run() {
                NoteImageView.this.invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        if (background != null) {
            canvas.drawBitmap(background, null, destination, backgroundPaint);
        }
        bitmapClickable.clear();
        addAllPins(canvas);
        drawnPoints(canvas);
    }

    private void drawnPoints(Canvas canvas) {
        int height;
        if (left.size() > 0) {

            height = HEIGHT / left.size();
            for (int i = 0; i < left.size(); i++) {
                Object item = left.get(i);

                Bitmap itemBitmap = adapter.getItemBitmap(item);
                String itemText = adapter.getLabel(item);
                Paint paint = adapter.getLabelPaint(item);
                Rect textBound = new Rect();
                float textWidth = paint.measureText(itemText);
                this.paint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
                float textHeight = textBound.height();


                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);
                    Rect rect = new Rect(
                            0,
                            Math.round(positionY - textHeight),
                            Math.round(textWidth),
                            (height / 2) + (height * i) + Math.round(textHeight));

                    labelClickable.put(rect, item);
                    usingPaint = adapter.getLabelPaint(item);

                    float itemHeight = itemBitmap.getHeight() / ratio;
                    float itemWidth = itemBitmap.getWidth() / ratio;

                    canvas.drawText(itemText, 0, positionY, usingPaint);
                    canvas.drawLine(
                            textWidth + TEXT_MARGIN_RIGHT,
                            positionY - (textHeight / 2),
                            location.x + (itemWidth * adapter.getAnchor(item).x)-(itemWidth/2),
                            location.y + (itemHeight * adapter.getAnchor(item).y)-(itemHeight/2) ,
                            usingPaint);
                }
            }
        }
        if (right.size() > 0) {

            height = HEIGHT / right.size();
            for (int i = 0; i < right.size(); i++) {
                Object item =right.get(i);

                String itemText = adapter.getLabel(item);
                Rect textBound = new Rect();

                Paint labelPaint = adapter.getLabelPaint(item);

                float textSize = labelPaint.measureText(itemText);
                labelPaint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
                float textSizeH = textBound.height();

                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);
                    Rect rect = new Rect(
                            Math.round(WIDTH - textSize),
                            Math.round(positionY - (textSizeH)),
                            Math.round(WIDTH),
                            Math.round(positionY + textSizeH));

                    labelClickable.put(rect, item);
                    canvas.drawText(itemText, WIDTH - textSize,
                            (height / 2) + (height * i) + Math.round(textSizeH), labelPaint);


                    Bitmap itemBitmap = adapter.getItemBitmap(item);

                    float itemHeight = itemBitmap.getHeight() / ratio;
                    float itemWidth = itemBitmap.getWidth() / ratio;

                    canvas.drawLine(WIDTH - textSize - TEXT_MARGIN_RIGHT,
                            5 + positionY + textSizeH / 2,
                            location.x + (itemWidth * adapter.getAnchor(item).x)-(itemWidth/2),
                            location.y + (itemHeight * adapter.getAnchor(item).y)-(itemHeight/2) ,
                            adapter.getLinePaint(item));
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            for (Rect rect : labelClickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    if (adapter.itemClickListener!=null){
                            adapter.itemClickListener.onMapItemClick(labelClickable.get(rect));
                    }
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    /**
     * Display all pins at their respective position onto a canvas
     *
     *
     */
    /*
    private void addAllPins(Canvas canvas) {

        for (int i = 0; i < adapter.getCount(); i++) {
            Object item = adapter.getItemAtPosition(i);

            PointF location = getLocation(item);

            Bitmap bitmap = adapter.getItemBitmap(item);
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
                bitmapClickable.put(rect, item);
            }

        }
    }*/

   /* @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            Log.e(TAG, "onTouchEvent: x:" + (motionEvent.getX() / WIDTH) + " y:" + (motionEvent.getY() / HEIGHT));
            for (Rect rect : bitmapClickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    adapter.itemClickListener.onMapItemClick(bitmapClickable.get(rect));
                    Toast.makeText(getContext(), adapter.getLabel(bitmapClickable.get(rect)), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }*/

    public class ItemXComparator implements java.util.Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            return Float.compare(adapter.getItemLocation(first).x, adapter.getItemLocation(second).x);
        }
    }

    public class ItemYComparator implements java.util.Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            return Float.compare(adapter.getItemLocation(first).y, adapter.getItemLocation(second).y);
        }
    }

}
