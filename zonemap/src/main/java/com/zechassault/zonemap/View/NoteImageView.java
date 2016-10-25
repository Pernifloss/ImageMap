package com.zechassault.zonemap.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zechassault.zonemap.Adapter.NoteImageAdapter;
import com.zechassault.zonemap.Util.BitmapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NoteImageView extends ImageMapView {
    /**
     * Margin in pixel between text and line
     */
    private final int TEXT_MARGIN = 20;

    /**
     * Collection of Rectangle containing labels for tap interaction
     */
    private Map<Rect, Object> labelClickable = new HashMap<>();

    /**
     * Left list of items
     */
    private List<Object> left = new ArrayList<>();

    /**
     * Right list of items
     */
    private List<Object> right = new ArrayList<>();

    /**
     * Adapter population the map
     */
    private NoteImageAdapter adapter;


    public NoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        refreshElements();

    }

    /**
     * Reset left and right items
     */
    private void refreshElements() {
        left = new ArrayList<>();
        right = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Object itemAtPosition = adapter.getItemAtPosition(i);
            if (adapter.isItemOnLeftSide(itemAtPosition)) {
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
        // Erase canvas
        canvas.drawColor(Color.TRANSPARENT);
        if (background != null) {
            canvas.drawBitmap(background, null, destination, paint);
        }
        bitmapClickable.clear();
        addAllPins(canvas);
        labelClickable.clear();
        drawnPoints(canvas);
    }

    /**
     * Draw label and line joining item and label
     *
     * @param canvas the canvas to draw onto
     */
    private void drawnPoints(Canvas canvas) {
        int height;
        String itemText;
        Paint temporaryPaint;
        Rect textBound;
        // TODO refactor left and right to avoid duplicated code
        if (left.size() > 0) {

            height = HEIGHT / left.size();
            for (int i = 0; i < left.size(); i++) {
                Object item = left.get(i);


                Bitmap itemBitmap = adapter.getItemBitmap(item);
                if (itemBitmap == null) {
                    itemBitmap = BitmapUtils.getEmptyBitmap();
                }
                itemText = adapter.getLabel(item);
                temporaryPaint = adapter.getLabelPaint(item);
                textBound = new Rect();
                float textWidth = temporaryPaint.measureText(itemText);
                temporaryPaint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
                float textHeight = textBound.height();


                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);
                    Rect rect = new Rect(
                            0,
                            Math.round(positionY - textHeight),
                            Math.round(textWidth),
                            positionY + Math.round(textHeight));

                    labelClickable.put(rect, item);

                    float itemHeight = itemBitmap.getHeight() / (scaleToBackground ? ratio : 1);
                    float itemWidth = itemBitmap.getWidth() / (scaleToBackground ? ratio : 1);

                    canvas.drawText(itemText, 0, positionY, temporaryPaint);
                    PointF anchor = adapter.getAnchor(item);
                    canvas.drawLine(
                            textWidth + TEXT_MARGIN,
                            positionY - (textHeight / 2),
                            location.x + (itemWidth * anchor.x) - (itemWidth / 2),
                            location.y + (itemHeight * anchor.y) - (itemHeight / 2),
                            adapter.getLinePaint(item));
                }
            }
        }
        if (right.size() > 0) {

            height = HEIGHT / right.size();
            for (int i = 0; i < right.size(); i++) {
                Object item = right.get(i);

                itemText = adapter.getLabel(item);
                textBound = new Rect();

                temporaryPaint = adapter.getLabelPaint(item);

                float textSize = temporaryPaint.measureText(itemText);
                temporaryPaint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);
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
                            (height / 2) + (height * i) + Math.round(textSizeH), temporaryPaint);


                    Bitmap itemBitmap = adapter.getItemBitmap(item);
                    if (itemBitmap == null) {
                        itemBitmap = BitmapUtils.getEmptyBitmap();
                    }
                    float itemHeight = itemBitmap.getHeight() / (scaleToBackground ? ratio : 1);
                    float itemWidth = itemBitmap.getWidth() / (scaleToBackground ? ratio : 1);

                    PointF anchor = adapter.getAnchor(item);
                    canvas.drawLine(WIDTH - textSize - TEXT_MARGIN,
                            5 + positionY + textSizeH / 2,
                            location.x + (itemWidth * anchor.x) - (itemWidth / 2),
                            location.y + (itemHeight * anchor.y) - (itemHeight / 2),
                            adapter.getLinePaint(item));
                }
            }
        }
    }

    /**
     * Add label touch event
     *
     * @param motionEvent motion to analyse
     * @return super
     */
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            for (Rect rect : labelClickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    if (adapter.itemClickListener != null) {
                        adapter.itemClickListener.onMapItemClick(labelClickable.get(rect));
                    }
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    /**
     * Comparator to order label vertically
     */
    public class ItemYComparator implements java.util.Comparator<Object> {
        @Override
        public int compare(Object first, Object second) {
            PointF itemLocation = adapter.getItemCoordinates(first);
            return Float.compare(itemLocation.y, adapter.getItemCoordinates(second).y);
        }
    }
}
