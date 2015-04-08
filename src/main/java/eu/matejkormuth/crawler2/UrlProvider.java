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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that represents thread-safe storage unit for crawled and not crawled
 * URLs.
 */
public class UrlProvider {

    private static final Logger LOG = LoggerFactory
            .getLogger(UrlProvider.class);

    private HashSet<URL> crawled = new HashSet<URL>();
    private Deque<URL> notCrawled = new ArrayDeque<URL>();
    private Object lock = new Object();

    /**
     * Adds specified URL to not crawled objects collection. When you are adding
     * bunch of URLs you should use {@link #addUrls(URL[])} methods cause that
     * synchronizes only once per URL array.
     * 
     * @param url
     *            URL to add
     */
    public void addUrl(URL url) {
        synchronized (lock) {
            if (!crawled.contains(url)) {
                this.notCrawled.addLast(url);
            }
        }
    }

    /**
     * Adds specified URLs to not crawled objects collection.
     * 
     * @param urls
     *            URLs to add
     */
    public void addUrls(URL[] urls) {
        synchronized (lock) {
            for (URL url : urls) {
                if (!crawled.contains(url)) {
                    LOG.debug("Adding url {}...", url.toString());
                    this.notCrawled.addLast(url);
                }
            }
        }
    }

    /**
     * Returns next not crawled URL if available and moves it from <i>not
     * crawled</i> collection to <i>crawled</i> collection. This method may
     * return null when <i>not crawled</i> collection is empty.
     * 
     * @return URL that should be crawled
     */
    public URL aquireUrl() {
        synchronized (lock) {
            if (notCrawled.isEmpty()) {
                return null;
            }

            URL url = notCrawled.pop();
            crawled.add(url);
            return url;
        }
    }

    /**
     * Returns whether <i>not crawled</i> collection of this UrlProvider is
     * empty.
     * 
     * @return true if there are any URL(s) in <i>not crawled</i> collection
     */
    public boolean isEmpty() {
        return notCrawled.isEmpty();
    }

    /**
     * Adds URL to visited (crawled) collection.
     * 
     * @param url
     *            URL to be added
     */
    public void addVisited(URL url) {
        synchronized (lock) {
            this.crawled.add(url);
        }
    }

    /**
     * Returns the amount of not visited collection items.
     * 
     * @return amount of not visited collection items
     */
    public int getNotVisitedCount() {
        return this.notCrawled.size();
    }

    /**
     * Returns the amount of visited collection items.
     * 
     * @return amount of visited collection items
     */
    public int getVisitedCount() {
        return this.crawled.size();
    }

    /**
     * Returns the total amount of not visited + visited collection items.
     * 
     * @return total amount of not visited + visited collection items
     */
    public int getTotalCount() {
        return this.crawled.size() + this.notCrawled.size();
    }
}
