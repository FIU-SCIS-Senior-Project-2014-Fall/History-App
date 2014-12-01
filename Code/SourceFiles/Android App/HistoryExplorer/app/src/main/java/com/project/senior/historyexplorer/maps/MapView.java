/*
 * Copyright (C) 2009 James Ancona
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.project.senior.historyexplorer.maps;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ZoomControls;
/*
import com.commonsware.cwac.bus.SimpleBus;
import com.commonsware.cwac.cache.AsyncCache;
import com.commonsware.cwac.cache.WebImageCache;*/

/**
 * {@hide}
 * @author Jim Ancona
 */
public class MapView 
    extends ViewGroup 
    implements ViewManager, ViewParent, Drawable.Callback, KeyEvent.Callback {
    private static final int MAX_CACHE_SIZE = 101;
    private static final int ONE_WEEK_IN_MILLIS = 1000 * 60 * 60 * 24 * 7;
    private static final String TAG = "MapView";
    private MapSourceInfo mapSourceInfo = new CloudmadeSourceInfo("b06a3135a4eb5848a225483969f56967");
    private int tileSize = mapSourceInfo.getTileSize();
    private GeoPoint center = new GeoPoint(0, 0);
    private int zoomLevel = 1;
    private int width, height;
    private List<Overlay> overlays = new ArrayList<Overlay>();
    /*private WebImageCache cache;*/
    private Drawable placeholder;
  /*  private SimpleBus bus = new SimpleBus();
    private AsyncCache.DiskCachePolicy policy = new AsyncCache.DiskCachePolicy() {
        public boolean eject(File file) {
            return (System.currentTimeMillis() - file.lastModified() > ONE_WEEK_IN_MILLIS);
        }
    };*/
    private ZoomControls zoomControls = new ZoomControls(getContext());
    private int touchDownX;
    private int touchDownY;
    private Stack<ImageView> extraImages = new Stack<ImageView>(); 

    public MapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    public MapView(Context context, String apiKey) {
        super(context);
        init(context, null);
    }
    private void init(Context context, AttributeSet attrs) {
        try {
          placeholder = getResources().getDrawable(Class.forName("com.android.internal.R").getField("drawable").getInt("blank_tile"));
        } catch (Exception e) {
          Log.e(TAG, "Unable to locate resource", e);
          placeholder = new BitmapDrawable(getClass().getClassLoader().getResourceAsStream("blank_tile.png"));
        }
        zoomControls.setLayoutParams(new LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {
            public void onClick(View v) {
                setZoomLevel(zoomLevel - 1);
                checkZoomControl();
                invalidate();
            }
        });
        zoomControls.setOnZoomInClickListener(new OnClickListener() {
            public void onClick(View v) {
                setZoomLevel(zoomLevel + 1);
                checkZoomControl();
                invalidate();
            }
        });
        zoomControls.setVisibility(View.VISIBLE);
        zoomControls.measure(android.view.View.MeasureSpec.UNSPECIFIED,
                android.view.View.MeasureSpec.UNSPECIFIED);
    }
    void checkZoomControl() {
        zoomControls.setIsZoomOutEnabled(zoomLevel > 0);
        zoomControls.setIsZoomInEnabled(zoomLevel < getMaxZoomLevel());
        
    }
/*    private synchronized WebImageCache getCache() {
        if (cache == null) {
            cache = new WebImageCache(getContext().getCacheDir(), bus, policy, MAX_CACHE_SIZE,
                    placeholder);
        }
        return cache;
    }*/

    public boolean canCoverCenter() {
        throw new NotImplementedException();
    }

    @Override
    public void computeScroll() {
    }

    public void displayZoomControls(boolean takeFocus) {
        addView(zoomControls);
        if (takeFocus)
            requestChildFocus(zoomControls, zoomControls);
    }

    @Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        throw new NotImplementedException();
    }

    public MapController getController() {
        return new MapController(this);
    }

    public int getLatitudeSpan() {
        InternalProjection p = (InternalProjection)getProjection();
        GeoPoint viewTopLeft = p.fromPixels(0, 0);
        GeoPoint viewBottomRight = p.fromPixels(width - 1, height - 1);
        return viewTopLeft.getLatitudeE6() - viewBottomRight.getLatitudeE6();
    }

    public int getLongitudeSpan() {
        InternalProjection p = (InternalProjection)getProjection();
        GeoPoint viewTopLeft = p.fromPixels(0, 0);
        GeoPoint viewBottomRight = p.fromPixels(width - 1, height - 1);
        return viewBottomRight.getLongitudeE6() - viewTopLeft.getLongitudeE6();
    }

    void setMapCenter(GeoPoint center) {
        this.center = center;
        invalidate();
    }

    public GeoPoint getMapCenter() {
        return center;
    }

    public int getMaxZoomLevel() {
        return mapSourceInfo.getMaxZoom();
    }

    public final List<Overlay> getOverlays() {
        return overlays;
    }

    public Projection getProjection() {
        return new InternalProjection();
    }

    @Deprecated
    public View getZoomControls() {
        return zoomControls;
    }

    public void setBuiltInZoomControls(boolean on) {
        if (on)
            addView(zoomControls);
        else
            removeView(zoomControls);
    }
    boolean zoomIn() {
        return setZoomLevel(zoomLevel + 1);
    }
    boolean zoomOut() {
        return setZoomLevel(zoomLevel - 1);
    }
    boolean setZoomLevel(int zoomLevel) {
        if (zoomLevel < 0)
            this.zoomLevel = 0;
        else if (zoomLevel > getMaxZoomLevel())
            zoomLevel = getMaxZoomLevel();
        else
            this.zoomLevel = zoomLevel;
        return this.zoomLevel == zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public boolean isSatellite() {
        return false;
    }

    public boolean isStreetView() {
        return false;
    }

    public boolean isTraffic() {
        return false;
    }

    @Override
    public void onFocusChanged(boolean hasFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onRestoreInstanceState(Bundle state) {
    }

    public void onSaveInstanceState(Bundle state) {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Overlay ov : this.overlays)
            if (ov.onTouchEvent(event, this))
                return true;
        GeoPoint newCenter;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = (int)event.getX();
                touchDownY = (int)event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                newCenter = getProjection().fromPixels(
                        width / 2 - (int)(event.getX() - touchDownX),
                        height / 2 - (int)(event.getY() - touchDownY));
                touchDownX = (int)event.getX();
                touchDownY = (int)event.getY();
                setMapCenter(newCenter); // Calls invalidate
                return true;
            case MotionEvent.ACTION_UP:
                newCenter = getProjection().fromPixels(
                        width / 2 - (int)(event.getX() - touchDownX),
                        height / 2 - (int)(event.getY() - touchDownY));
                setMapCenter(newCenter); // Calls invalidate
        }

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        return super.onTrackballEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void preLoad() {
    }

    public void setReticleDrawMode(ReticleDrawMode mode) {
    }

    public void setSatellite(boolean on) {
        throw new NotImplementedException();
    }

    public void setStreetView(boolean on) {
        throw new NotImplementedException();
    }

    public void setTraffic(boolean on) {
        throw new NotImplementedException();
    }

    @Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof MapView.LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.FILL_PARENT);
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p.width, p.height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height,
                heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        InternalProjection p = (InternalProjection)getProjection();
        GeoPoint viewTopLeft = p.fromPixels(0, 0);
        TileCoords topLeftTile = p.getTileCoords(viewTopLeft);
        GeoPoint viewBottomRight = p.fromPixels(width - 1, height - 1);
        TileCoords bottomRightTile = p.getTileCoords(viewBottomRight);
        TileCoords centerTile = p.getTileCoords(center);
        Point centerTileOffset = p.geoPointToPoint(center);
        centerTileOffset.x -= (centerTile.getX() * tileSize); 
        centerTileOffset.y -= (centerTile.getY() * tileSize); 
        int index = 0;
        boolean zoomDisplayed = false;
        final int maxTileCoord = (1 << zoomLevel) - 1;
        for (int tileX = Math.max(topLeftTile.getX(), 0); 
                tileX <= Math.min(bottomRightTile.getX(), maxTileCoord); 
                tileX++) {
            for (int tileY = Math.max(topLeftTile.getY(), 0); 
                    tileY <= Math.min(bottomRightTile.getY(), maxTileCoord); 
                    tileY++) {
                ImageView image = null;
                while (index < getChildCount() && image == null) {
                    View v = getChildAt(index++);
                    if (v instanceof ImageView)
                        image = (ImageView)v;
                    else if (v.equals(zoomControls))
                        zoomDisplayed = true;
                }
                if (index >= getChildCount() && image == null) {
                    image = getImageView();
                    addView(image);
                    index++;
                }
                try {
                    /*getCache().handleImageView(
                            image,
                            mapSourceInfo.getTileUri(tileX, tileY, zoomLevel), 
                            "Maps");*/
                } catch (Exception e) {
                    throw new RuntimeException("Caught exception retrieving image: ", e);
                }
                int leftPadding = (tileX - centerTile.getX()) * tileSize + (getWidth() / 2) - centerTileOffset.x;
                int topPadding = (tileY - centerTile.getY()) * tileSize + (getHeight() / 2) - centerTileOffset.y;
                image.layout(
                        leftPadding, 
                        topPadding, 
                        leftPadding + tileSize, 
                        topPadding + tileSize);
            }
        }
        for (int i = index; i < getChildCount(); i++) {
            View v = getChildAt(i);
            if (v instanceof ImageView)
                removeImageViewAt(i);
            else if (v.equals(zoomControls))
                zoomDisplayed = true;
        }
        if (zoomDisplayed) {
            zoomControls.setVisibility(View.VISIBLE);
            bringChildToFront(zoomControls);
            zoomControls.layout(
                    (width - zoomControls.getMeasuredWidth()) / 2, 
                    height - zoomControls.getMeasuredHeight(),
                    (width + zoomControls.getMeasuredWidth()) / 2, 
                    height);
        }
        super.dispatchDraw(canvas);
        for (Overlay o : overlays) {
            o.draw(canvas, this, false);
        }
    }

    private void removeImageViewAt(int i) {
        extraImages.push((ImageView)getChildAt(i));
        removeViewAt(i);
    }
    private ImageView getImageView() {
        if (extraImages.isEmpty())
            return new ImageView(getContext());
        else
            return extraImages.pop();
    }

    public static enum ReticleDrawMode {
        DRAW_RETICLE_NEVER, DRAW_RETICLE_OVER, DRAW_RETICLE_UNDER
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public static final int MODE_MAP = 0;
        public static final int MODE_VIEW = 1;
        public static final int LEFT = 3;
        public static final int RIGHT = 5;
        public static final int TOP = 48;
        public static final int BOTTOM = 80;
        public static final int CENTER_HORIZONTAL = 1;
        public static final int CENTER_VERTICAL = 16;
        public static final int CENTER = 17;
        public static final int TOP_LEFT = 51;
        public static final int BOTTOM_CENTER = 81;

        public int mode;
        public GeoPoint point;
        public int x;
        public int y;
        public int alignment;

        public LayoutParams(int width, int height, GeoPoint point, int alignment) {
            super(width, height);
        }
        public LayoutParams(int width, int height, GeoPoint point, int x, int y, int alignment) {
            super(width, height);
        }
        public LayoutParams(int width, int height, int x, int y, int alignment) {
            super(width, height);
        }
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    private class InternalProjection implements Projection {
        private static final double CIRCUMFERENCE_IN_METERS = 40075160.0;
        private int tiles = 1 << zoomLevel;
        private double circumference = tileSize * tiles;
        private double radius = circumference / (2.0 * Math.PI);
        
        Point geoPointToPoint(GeoPoint gp) {
          Point ret = new Point();
          double longitude = ((double)gp.getLongitudeE6()) / 1000000.0 * Math.PI / 180.0;
          ret.x = (int)(radius * longitude + (circumference / 2.0));
          double latitude = ((double)gp.getLatitudeE6()) / 1000000.0 * Math.PI / 180.0;
          ret.y = (int)((circumference / 2.0) - 
                  (radius / 2.0 * Math.log((1.0 + Math.sin(latitude)) / (1.0 - Math.sin(latitude)))));
          return ret;
        }
        GeoPoint pointToGeoPoint(Point p) {
          double longRadians = (p.x - (circumference / 2.0)) / radius;
          double longDegrees = longRadians * 180.0 / Math.PI;
          int long1E6 = (int)(longDegrees * 1000000.0);

          double latitude =  (Math.PI / 2.0) - (2.0 * Math.atan(Math.exp(/* -1.0 * */(p.y - (circumference / 2.0)) / radius)));
          int lat1E6 = (int)(latitude * 180.0 / Math.PI * 1000000.0);

          return new GeoPoint(lat1E6, long1E6);
        }

        public GeoPoint fromPixels(int x, int y) {
            Point c = geoPointToPoint(center);
            c.x = c.x - (getWidth() / 2) + x;
            c.y = c.y - (getHeight() / 2) + y;
            return pointToGeoPoint(c);
        }

        public float metersToEquatorPixels(float meters) {
            return (float)(meters * circumference / CIRCUMFERENCE_IN_METERS);
        }
        public Point toPixels(GeoPoint in, Point out) {
            if (out == null)
                out = new Point();
            Point p = geoPointToPoint(in);
            Point c = geoPointToPoint(center);
            out.set(p.x - c.x + (getWidth() / 2), p.y - c.y + (getHeight() / 2));
            return out;
        }
//        /**
//         * Get the coordinates of the upper-left corner of the tile containing a
//         * Geopoint
//         */
//        GeoPoint getTileGeoPoint(GeoPoint gp) {
//            return tileCoordsToGeoPoint(getTileCoords(gp));
//        }
//
//        TileCoords getTileCoords(final double lat, final double lon) {
//            int xtile = (int)Math.floor((lon + 180) / 360 * (1 << zoomLevel));
//            int ytile = (int)Math.floor((1 - Math.log(Math.tan(lat * Math.PI / 180) + 1
//                    / Math.cos(lat * Math.PI / 180))
//                    / Math.PI)
//                    / 2 * (1 << zoomLevel));
//            return new TileCoords(xtile, ytile, zoomLevel);
//        }

        TileCoords getTileCoords(final int latE6, final int lonE6) {
            final double lat = ((double) latE6) / 1000000.0;
            final double lon = ((double) lonE6) / 1000000.0;
            final long z = (1L << zoomLevel);
            int xtile = (int)Math.floor( (lon + 180.0) / 360.0 * z) ;
            int ytile = (int)Math.floor( (1.0 - Math.log(Math.tan(lat * Math.PI / 180.0) + 1.0 / Math.cos(lat * Math.PI / 180.0)) / Math.PI) / 2 * z) ;
            return new TileCoords(xtile, ytile, zoomLevel);
        }

        TileCoords getTileCoords(final GeoPoint gp) {
            return getTileCoords(gp.getLatitudeE6(), gp.getLongitudeE6());
        }

        @SuppressWarnings("unused")
        GeoPoint tileCoordsToGeoPoint(TileCoords osmTile) {
            double n = Math.PI - 2.0 * Math.PI * osmTile.getY() / tiles;
            return new GeoPoint(
                    (int)Math.round(180000000.0 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)))), 
                    (int)(osmTile.getX() * 360000000L / tiles - 180000000L));
        }
    }
}
