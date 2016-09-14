package com.larypipot.cimi.imagemap.View;

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
import android.widget.Toast;

import com.larypipot.cimi.imagemap.Adapter.NoteImageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoteImageView extends ImageMapView {

    private static final String TAG = "NoteImageView";

    private final Typeface typeface;
    private List<String> left;
    private List<String> right;
    private NoteImageAdapter adapter;
    private Paint backgroundPaint;

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

    @Override
    public void setAdapter(final NoteImageAdapter adapter) {
        super.setAdapter(adapter);
        NoteImageView.this.adapter = adapter;
        //  NoteImageView.this.items = adapter.getItems();
        left = new ArrayList<>();
        right = new ArrayList<>();
        Map<Float, String> leftMap = new HashMap<>();
        Map<Float, String> rightmapMap = new HashMap<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            PointF point = adapter.getItemLocation(adapter.getItemAtPosition(i));
            if (point.x < 0.5f) {
                leftMap.put(point.y, adapter.getLabel(adapter.getItemAtPosition(i)));
            } else {
                rightmapMap.put(point.y, adapter.getLabel(adapter.getItemAtPosition(i)));
            }
        }
        List<Float> sortedLeft = new ArrayList<>();
        sortedLeft.addAll(leftMap.keySet());
        Collections.sort(sortedLeft);
        for (Float f : sortedLeft) {
            left.add(leftMap.get(f));
        }
        List<Float> sortedRight = new ArrayList<>();
        sortedRight.addAll(rightmapMap.keySet());
        Collections.sort(sortedRight);
        for (Float f : sortedRight) {
            right.add(rightmapMap.get(f));
        }

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
        //canvas.drawText("");
        if (background != null) {
            canvas.drawBitmap(background, null, destination, backgroundPaint);
        }
        drawnPoints(canvas);
        addAllPins(canvas);
    }

    Paint usingPaint;

    private void drawnPoints(Canvas canvas) {
        int height;
        if (left.size() > 0) {

            height = HEIGHT / left.size();
            for (int i = 0; i < left.size(); i++) {
                String itemText = left.get(i);
                Paint paint = adapter.getPaint(getItem(itemText));
                Rect textBound = new Rect();
                float textSize =  paint.measureText(itemText);
                this.paint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
                float textSizeH = textBound.height();

                Object item = getItem(itemText);

                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);
                    Rect rect = new Rect(
                            0,
                            Math.round(positionY - textSizeH),
                            Math.round(textSize),
                            (height / 2) + (height * i) + Math.round(textSizeH));

                    clickable.put(rect, item);
                    usingPaint = adapter.getPaint(item);

                    canvas.drawText(itemText, 0, positionY, usingPaint);
                    canvas.drawLine(textSize + 20, positionY - (textSizeH / 2), 3 + location.x - (selected.getWidth() / 2), location.y, usingPaint);
                }
            }
        }
        if (right.size() > 0) {

            height = HEIGHT / right.size();
            for (int i = 0; i < right.size(); i++) {
                String itemText = right.get(i);
                Rect textBound = new Rect();

                Paint paint = adapter.getPaint(getItem(itemText));
                float textSize = paint.measureText(itemText);
                paint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
                float textSizeH = textBound.height();
                Object item = getItem(itemText);

                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);
                    Rect rect = new Rect(
                            Math.round(WIDTH - textSize),
                            Math.round(positionY - (textSizeH)),
                            Math.round(WIDTH),
                            Math.round(positionY + textSizeH));

                    clickable.put(rect, item);
                    // usingPaint = selectedItems.contains(item) ? paintSelected : paint;

                    usingPaint = adapter.getPaint(item);
                    canvas.drawText(right.get(i), WIDTH - textSize,
                            (height / 2) + (height * i) + Math.round(textSizeH), usingPaint);


                    canvas.drawLine(WIDTH - textSize - 20, 5 + positionY + textSizeH / 2, location.x + (selected.getWidth() / 2), location.y, usingPaint);
                }
            }
        }
    }
//TODO REMOVE THIS SHIT
    private Object getItem(String s) {
        for (int i = 0; i < adapter.getCount(); i++) {
            Object itemAtPosition = adapter.getItemAtPosition(i);
            if (adapter.getLabel(itemAtPosition).equals(s)) {
                return itemAtPosition;
            }
        }

        return null;
    }




    /**
     * Display all pins at their respective position onto a canvas
     * @param canvas Canvas to draw on to
     */
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
                clickable.put(rect, item);
            }

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            Log.e(TAG, "onTouchEvent: x:" + (motionEvent.getX() / WIDTH) + " y:" + (motionEvent.getY() / HEIGHT));
            for (Rect rect : clickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    adapter.itemClickListener.onMapItemClick(clickable.get(rect));
                    Toast.makeText(getContext(), adapter.getLabel(clickable.get(rect)), Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

}
