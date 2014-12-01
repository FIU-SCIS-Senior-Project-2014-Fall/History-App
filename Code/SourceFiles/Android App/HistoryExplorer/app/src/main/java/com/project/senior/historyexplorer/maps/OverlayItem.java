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

import android.graphics.drawable.Drawable;

/**
 * @author Jim Ancona
 */
public class OverlayItem {
    protected final GeoPoint mPoint;
    protected final String mTitle;
    protected Drawable mMarker;

    public OverlayItem(GeoPoint point, String title) {
        mPoint = point;
        mTitle = title;
        mMarker = null;
    }
    static void setState(android.graphics.drawable.Drawable drawable, int stateBitset) {
        throw new NotImplementedException();
    }
    public Drawable getMarker(int stateBitset) {
        return mMarker;
    }

    public GeoPoint getPoint() {
        return mPoint;
    }

    public String getTitle() {
        return mTitle;
    }

    public String routableAddress() {
        throw new NotImplementedException();
    }

    public void setMarker(Drawable marker) {
        this.mMarker = marker;
    }

}
