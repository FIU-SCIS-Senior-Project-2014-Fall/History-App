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

/**
 * Description of a source of map tiles
 * 
 * {@hide}
 * @author Jim Ancona
 */
interface MapSourceInfo {
    /**
     * @return the name of this source
     */
    String getName();

    /**
     * @return A tile URI
     */
    String getTileUri(int tileX, int tileY, int zoomLevel);

    /**
     * @return the maximum zoom supported by this source
     */
    int getMaxZoom();

    /**
     * @return the tile size in pixels
     */
    int getTileSize();
}
