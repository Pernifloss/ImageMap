package com.zechassault.zonemap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zechassault.zonemap.adapter.NoteImageAdapter;
import com.zechassault.zonemap.listener.AdapterListener;

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
        if (backImage != null) {
            destination = getDestinationRect(backImage, w, h);
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setAdapter(final NoteImageAdapter adapter) {
        super.setAdapter(adapter);
        adapter.listener = new AdapterListener() {
            @Override
            public void notifyDataSetHasChanged() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        NoteImageView.this.invalidate();
                    }
                });
                refreshElements();
            }
        };
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
        super.onDraw(canvas);
        labelClickable.clear();
        drawnPoints(canvas);
        if (uIDebug) {

            for (Rect r : labelClickable.keySet()) {

                canvas.drawRect(r
                        , debugPaint);
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (adapter.getItemAtPosition(i).equals(labelClickable.get(r))) {
                        canvas.drawText(i + "", r.left, r.top, debugPaint);
                    }
                }
            }
        }
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

                Bitmap itemBitmap = adapter.getNotNullBitmap(item);
                itemText = adapter.getLabel(item);
                temporaryPaint = adapter.getLabelPaint(item);
                textBound = new Rect();
                float textMaxWidth = getMaximumWidth(itemText, temporaryPaint);
                temporaryPaint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);

                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);


                    float itemHeight = itemBitmap.getHeight() / (scaleToBackground ? ratio : 1);
                    float itemWidth = itemBitmap.getWidth() / (scaleToBackground ? ratio : 1);

                    labelClickable.put(drawText(canvas, temporaryPaint, 0, positionY, itemText, true), item);

                    PointF anchor = adapter.getAnchor(item);
                    canvas.drawLine(
                            textMaxWidth + TEXT_MARGIN,
                            positionY - (textBound.height() / 2),
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

                temporaryPaint.getTextBounds(itemText, 0, itemText.length() - 1, textBound);


                if (item != null) {
                    PointF location = getLocation(item);

                    int positionY = (height / 2) + (height * i);


                    Rect rect = drawText(canvas, temporaryPaint, WIDTH, positionY, itemText, false);
                    labelClickable.put(rect, item);
                    drawLine(canvas, textBound, item, location, positionY, rect.width());
                }
            }
        }
    }

    private void drawLine(Canvas canvas, Rect textBound, Object item, PointF location, int positionY, float textMaxWidth) {
        Bitmap itemBitmap = adapter.getNotNullBitmap(item);
        float itemHeight = itemBitmap.getHeight() / (scaleToBackground ? ratio : 1);
        float itemWidth = itemBitmap.getWidth() / (scaleToBackground ? ratio : 1);

        PointF anchor = adapter.getAnchor(item);

        canvas.drawLine(WIDTH - textMaxWidth - TEXT_MARGIN,
                positionY - (textBound.height() / 2),
                location.x + (itemWidth * anchor.x) - (itemWidth / 2),
                location.y + (itemHeight * anchor.y) - (itemHeight / 2),
                adapter.getLinePaint(item));
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
                        if (debug) {
                            Object item = labelClickable.get(rect);
                            debugLog("onTouchEvent label tapped ! label :" + adapter.getLabel(item) + " for item : " + item.toString());
                        }
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

    /**
     * Get the maximum width of a text respecting line break (i.e. \n in text).
     *
     * @param text  the text to draw
     * @param paint the paint to draw
     * @return the maximum width
     */
    private static float getMaximumWidth(final String text, final Paint paint) {
        float width = 0;
        for (String token : text.split("\n")) {
            final float textWidth = paint.measureText(token);
            if (textWidth > width) {
                width = textWidth;
            }
        }

        return width;
    }

    /**
     * Draw a text on a canvas respecting line break (i.e. \n in text).
     *
     * @param canvas          the canvas
     * @param paint           the paint to draw
     * @param x               the x position of text
     * @param y               the y position of text to draw
     * @param text            the text to draw
     * @param isLeftAlignment the text is left or right alignment
     */
    private static Rect drawText(final Canvas canvas, final Paint paint, final float x,
                                 final float y, final String text, final boolean isLeftAlignment) {

        float textVerticalMargin = 0;
        float maxWidth = 0;
        float textHeight = 0;
        float textX = 0;
        float minTextX = x;
        for (String token : text.split("\n")) {
            final Rect textBound = new Rect();
            final int textLength = token.length() > 1 ? token.length() - 1 : 0;
            paint.getTextBounds(token, 0, textLength, textBound);
            float textWidth = paint.measureText(token);
            textHeight = textBound.height();
            textX = isLeftAlignment ? x : x - textWidth;
            float textY = y + textVerticalMargin;
            canvas.drawText(token, textX, textY, paint);
            textVerticalMargin += textHeight + 5;
            if (textWidth > maxWidth) {
                maxWidth = textWidth;
            }
            if (minTextX > textX) {
                minTextX = textX;
            }
        }
        return new Rect(
                Math.round(minTextX),
                Math.round(y - textHeight),
                Math.round(minTextX + maxWidth),
                Math.round(y + textVerticalMargin - textHeight));
    }
}
