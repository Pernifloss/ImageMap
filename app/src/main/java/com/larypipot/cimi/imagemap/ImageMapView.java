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
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.HashSet;
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
            int right = rect.right + rect.left;
            int top = rect.top;
            int bottom = rect.bottom + top;
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

        if (background != null) {
            canvas.drawBitmap(background, null, destination, paint);
        }

        addAllPins(canvas);
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
        int x = item.x;
        int y = item.y;
        return new PointF((WIDTH / 500f) * x, (HEIGHT / 1000f) * y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int x = Math.round(motionEvent.getX());
            int y = Math.round(motionEvent.getY());
            for (Rect rect : clickable.keySet()) {
                if (doesIntersect(x, y, rect)) {
                    itemClickListener.onMapItemClick(clickable.get(rect));
                }
            }
        }
        return false;
    }

}
