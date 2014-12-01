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

import android.graphics.Point;

/**
 * @author Jim Ancona
 */
public interface Projection {
    /**
     * Create a new GeoPoint from pixel coordinates relative to the top-left of
     * the MapView that provided this PixelConverter.
     */
    GeoPoint fromPixels(int x, int y);

    /**
     * Converts a distance in meters (along the equator) to one in (horizontal)
     * pixels at the current zoomlevel.
     */
    float metersToEquatorPixels(float meters);

    /**
     * Converts the given GeoPoint to onscreen pixel coordinates, relative to
     * the top-left of the MapView that provided this Projection.
     */
    Point toPixels(GeoPoint in, Point out);
}
