/*
 *  crawler2 - crawler for java
 *  Copyright (C) 2015 Matej Kormuth 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.matejkormuth.crawler2;

import java.net.URL;

/**
 * <p>
 * Represents object that can handle crawling logic.
 * </p>
 * <p>
 * At the runtime there may be many instances of class implementing this
 * interface.
 * </p>
 */
public interface Handler {
    /**
     * Returns whether the specified url should be visited. This method
     * <b>must</b> return true for all pages (even documents ending with html).
     * 
     * @param url
     *            url that should be visited
     * @return true if specified url should be visited, false otherwise
     */
    boolean shouldVisit(URL url);

    /**
     * Called after the Document has been fetched from server and all links had
     * been added to UrlProvider.
     * 
     * @param document
     *            downloaded document
     */
    void visited(Document document);
}
