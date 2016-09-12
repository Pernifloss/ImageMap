package com.larypipot.cimi.imagemap;

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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ImageMapView extends View {

    private static final String TAG = "ImageMapView";
    public ItemClickListener itemClickListener;
    Map<Rect, Item> clickable = new HashMap<>();
    private LayoutInflater layoutInflater;
    private Bitmap background;
    private Bitmap selected;
    private Bitmap unselected;
    private Set<Item> items = new HashSet<>();
    private int WIDTH;
    private int HEIGHT;


    private Paint paint;
    private Paint paintSelected;
    private Rect destination;

    private Set<Item> selectedItems = new HashSet<>();

    private Handler handler = new Handler();

    public ImageMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.com_larypipot_cimi_imagemap_ImageMapView, 0, 0);
        try {
            background = resAsBitmap(typedArray.getResourceId(R.styleable.com_larypipot_cimi_imagemap_ImageMapView_backgroundDrawable, 0));
            selected = resAsBitmap(typedArray.getResourceId(R.styleable.com_larypipot_cimi_imagemap_ImageMapView_selectedpDrawable, 0));
            unselected = resAsBitmap(typedArray.getResourceId(R.styleable.com_larypipot_cimi_imagemap_ImageMapView_unselectedDrawable, 0));
        } finally {
            typedArray.recycle();
        }
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(getResources().getDimension(R.dimen.textSize));
        paint.setAntiAlias(true);

        paintSelected = new Paint();
        paintSelected.setColor(Color.BLACK);
        paintSelected.setTextSize(getResources().getDimension(R.dimen.textSize));
        paintSelected.setStrokeWidth(3);
        paintSelected.setFakeBoldText(true);
        paintSelected.setAntiAlias(true);
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
        canvas.drawColor(Color.WHITE);
        //canvas.drawText("");
        if (background != null) {
            canvas.drawBitmap(background, null, destination, paint);
        }
        drawnPoints(canvas);
        addAllPins(canvas);
    }
    Paint usingPaint;

    private void drawnPoints(Canvas canvas) {
        List<String> left = new ArrayList<>();
        List<String> right = new ArrayList<>();
        Map<Float,String> leftMap = new HashMap<>();
        Map<Float,String> rightmapMap = new HashMap<>();
        for (String point : points.keySet()) {
            if (points.get(point).x < 0.5f) {
                leftMap.put(points.get(point).y,point);
            } else {
                rightmapMap.put(points.get(point).y,point);
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

        int height = HEIGHT / left.size();
        for (int i = 0; i < left.size(); i++) {
            String answerText = left.get(i);
            Rect textBound = new Rect();
            float textSize = paint.measureText(answerText);
            paint.getTextBounds(answerText, 0, answerText.length() - 1, textBound);
            float textSizeH = textBound.height();

            Item item = getItem(answerText);

            if (item != null) {
                PointF location = getLocation(item);

                int positionY = (height / 2) + (height * i);
                Rect rect = new Rect(
                        0,
                        Math.round(positionY - textSizeH),
                        Math.round(textSize),
                        (height / 2) + (height * i) + Math.round(textSizeH));

                clickable.put(rect, item);
                usingPaint = selectedItems.contains(item) ? paintSelected : paint;

                canvas.drawText(answerText, 0, positionY, usingPaint);
                canvas.drawLine(textSize + 20, positionY - (textSizeH / 2), location.x, location.y, usingPaint);
            }
        }

        height = HEIGHT / right.size();
        for (int i = 0; i < right.size(); i++) {
            String answerText = right.get(i);
            Rect textBound = new Rect();
            float textSize = paint.measureText(answerText);
            paint.getTextBounds(answerText,0,answerText.length()-1,textBound);
            float textSizeH = textBound.height();
            Item item = getItem(answerText);

            if (item != null) {
                PointF location = getLocation(item);

                int positionY = (height / 2) + (height * i);
                Rect rect = new Rect(
                        Math.round(WIDTH - textSize),
                        Math.round(positionY - (textSizeH)),
                        Math.round(WIDTH),
                        Math.round(positionY+textSizeH));

                clickable.put(rect, item);
                usingPaint = selectedItems.contains(item) ? paintSelected : paint;

                canvas.drawText(right.get(i), WIDTH - textSize ,
                        (height / 2) + (height * i) + Math.round(textSizeH), usingPaint);
                canvas.drawLine(WIDTH - textSize  - 20, positionY + textSizeH / 2, location.x, location.y, usingPaint);
            }
        }


    }

    private Item getItem(String s) {
        for (Item item : items) {
            if (item.getText().equals(s)) {
                return item;
            }
        }
        return null;
    }


    /**
     * Display all pins at their respective position onto a canvas
     *
     * @param canvas Canvas to draw on to
     */
    private void addAllPins(Canvas canvas) {
        for (Item item : items) {
            PointF location = getLocation(item);

            Bitmap bitmap = selectedItems.contains(item) ? selected : unselected;
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

    /**
     * @param item
     * @param isSelected
     */
    public void selectedItem(Item item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        invalidate();
    }


    private Bitmap resAsBitmap(int resID) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), resID);
        return convertToBitmap(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

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
    }

    private PointF getLocation(Item item) {
        //TODO use real Position
        float x = item.x;
        float y = item.y;
        return new PointF((WIDTH * x), (HEIGHT * y));

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            for (Rect rect : clickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    itemClickListener.onMapItemClick(clickable.get(rect));
                    Toast.makeText(getContext(),clickable.get(rect).getText(),Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }

    private Map<String, PointF> points;

    {
        points = new HashMap<>();
        points.put("Pedro", new PointF(0.5f, 0.33f));
        points.put("Gigot", new PointF(0.6f, 0.493f));
        points.put("Flétant", new PointF(0.5f, 0.550f));
        points.put("Connrad", new PointF(0.638f, 0.640f));
        points.put("Pendouse", new PointF(0.442f, 0.614f));
        points.put("Caracasse", new PointF(0.426f, 0.431f));
        points.put("Douglat", new PointF(0.442f, 0.789f));
        points.put("Merlouse", new PointF(0.5f, 0.104f));
        points.put("Fereur", new PointF(0.5f, 0.07f));
        points.put("Jorias", new PointF(0.5f, 0.107f));
        points.put("Tango", new PointF(0.688f, 0.183f));
        points.put("Charli", new PointF(0.58f, 0.551f));
        points.put("Begnet", new PointF(0.35f, 0.471f));
        points.put("Torla", new PointF(0.4f, 0.522f));
        points.put("Fletose", new PointF(0.32f, 0.377f));
        points.put("Jisade", new PointF(0.5f, 0.344f));
        points.put("Perlut", new PointF(0.66f, 0.480f));
        points.put("Doglof", new PointF(0.39f, 0.705f));
        points.put("Fleveur", new PointF(0.456f, 0.923f));
        points.put("Jorianne", new PointF(0.612f, 0.965f));
        points.put("Tagos", new PointF(0.4f, 0.950f));
    }
}
