# ImageMap 

Simple Android library to display bitmap on top of an Image

You can get it on this repo
```groovy
repositories {
        maven {
            url  "http://zechassault.bintray.com/maven"
        }
   }
```
and add it as a dependency with 
```groovy

    compile 'com.zechassault.zonemap:zonemap:0.1.0'
    
```
This libray deliver two views, first the simple ImageMapView, it allow you to place items on an image and define where they are positioned and wich bitmap is drawn at this position. (a little bit like a map with marker but way simpler to use)

Here is a little demo of the simple ImageMapView : 

![alt text](https://github.com/lary-pipot/ImageMap/blob/master/operation.gif)

How it work? really simply, based on ListView adapter, you just have to define your adapter that extends MapAdapter !

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

You can then interact with them by defining your itemClickListener


