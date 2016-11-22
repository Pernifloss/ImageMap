# ImageMap 

Simple Android library to display bitmaps on top of an Image. 

For now, you can get it on this repo
```groovy
repositories {
        maven {
             url "https://jitpack.io" 
        }
   }
```
And add it as a dependency with 
```groovy

    compile 'com.github.Pernifloss:ImageMap:0.4.0'
    
```
This library deliver two views, first the simple ImageMapView, it allow you to place items on an image and define where they are positioned and wich bitmap is drawn at this position. (a little bit like a map with marker but way simpler to use)

Here is a little demo of the simple ImageMapView, in this demo the user have to tap on the item which is asked on top : 

![alt text](https://github.com/lary-pipot/ImageMap/blob/master/operation.gif)

How it work? really simply

First define view on xml use src attribute to define the main image: 

```xml
<com.zechassault.zonemap.View.ImageMapView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:src="@drawable/my_image" />

```
You just have to define your adapter that extends MapAdapter !

An then you define specidied method :
```Java
     /**
     * Define this function to target where the item is positioned on the image
     *
     * @param item Item to get position
     * @return PointF(x, y) x and y are float 0 to 1
     * x is ratio of image width (e. x = 0.5f  item is centered horizontally)
     * y is ratio of image height (e. y =  1f  item will be at bottom)
     */
     public abstract PointF getItemCoordinates(T item);

    /**
     * Define this function to indicate which item the given position correspond to
     *
     * @param position int, index of item
     * @return the item corresponding to the position index
     */
    public abstract T getItemAtPosition(int position);

    /**
     * define this function to specifie the number of item you want to display
     *
     * @return int, the number of item to display
     */

    public abstract int getCount();

    /**
     * Override this method to a custom bitmap to draw for each element
     *
     * @param item the item of which the bitmap will correspond
     * @return the Bitmap to draw for given item
     */
    public abstract Bitmap getItemBitmap(T item);

```

You can then interact with items by setting the itemClickListener of you addapter.

If you want an exemple feel free to clone the project, it has a demo app that display an ImageViewMap.


The other view is called NoteImageView, it's like ImgeView map, but you also have labels diplayed on both side on the image, they are liken to the items. Like the following :

![alt text](https://github.com/lary-pipot/ImageMap/blob/master/anatomival.gif)

Label click are also considered as click on specified item !

You can customize labels, you can tell where the label is linked to the item image. By default it's linked to the center.

```Java
 
    private PointF centerAnchor = new PointF(0.5f, 0.5f);
     /**
     * Override this method to change the anchor calculation based on the bitmap
     * PointF(0.0f,0.5f) will anchor the line to the center left of the bitmap
     * @param item the item to define the anchor
     * @return anchor as PointF
     */
    public PointF getAnchor(T item) {
        return centerAnchor;
    }
```
By overiting the right method can also define the Paint used for writing text, so you can change color, style and font of labels. the same apply for line.

```Java

    /**
     * Override this function to define your own Paint for label drawing
     *
     * @param item the item to which the legend is linked
     * @return the labelPaint of to draw the label
     */
    public Paint getLabelPaint(T item) {
        return labelPaint;
    }

    /**
     * Override this function to define your own Paint for lines
     *
     * @param item the item to which the legend is linked
     * @return the labelPaint of to draw the label
     */
    public Paint getLinePaint(T item) {
        return linePaint;
    }
    
```
There is a way of telling on wich side you want a label to appear. 

Please note that this library is still in development, and i will be glad to correct bugs and add new features!

We made the choice to manipulate bitmap and paint for maximum flexibulity and customisation, but don't worry the library come with a helper that convert drawable to bitmap.
