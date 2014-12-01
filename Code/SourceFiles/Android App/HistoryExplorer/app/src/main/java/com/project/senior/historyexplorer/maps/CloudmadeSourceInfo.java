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
 * {@hide}
 * @author jim
 */
class CloudmadeSourceInfo implements MapSourceInfo {
    private final String apiKey; 
    private final int tileSize;
    private final int style;
    CloudmadeSourceInfo(String apiKey) {
        this(apiKey, 256);
    }
    CloudmadeSourceInfo(String apiKey, int tileSize) {
        this(apiKey, tileSize, 1);
    }
    CloudmadeSourceInfo(String apiKey, int tileSize, int style) {
        this.apiKey = apiKey;
        this.tileSize = tileSize;
        this.style = style;
    }
    public int getMaxZoom() {
        return 18;
    }

    public String getName() {
        return "Open Street Maps Cloudmade renderer";
    }

    public String getTileUri(int x, int y, int zoom) {
        return "http://b.tile.cloudmade.com/"+ apiKey +"/" + style + "/" + tileSize + "/" + zoom + "/" + x + "/" + y + ".png";
    }

    public int getTileSize() {
        return tileSize;
    }

}
