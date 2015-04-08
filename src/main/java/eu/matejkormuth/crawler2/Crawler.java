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

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents controller of all crawling operations.
 */
public class Crawler {

    private static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private Worker[] workers;
    private Configuration configuration;
    private UrlProvider provider;
    private HandlerFactory handlerFactory;
    private boolean isRunning;

    /**
     * Creates a new Crawler with specified Handler class and configuration used
     * for crawling. Don't forget to add at least one seed URL with
     * {@link #includeUrl(String)} method.
     * <p>
     * At the runtime there may be many instances of class implementing Handler
     * interface.
     * </p>
     * 
     * @param handlerClass
     *            handler class
     * @param configuraion
     *            configuration for crawler
     */
    public Crawler(Class<? extends Handler> handlerClass,
            Configuration configuraion) {
        LOG.info("Initializing Controller...");
        this.configuration = configuraion;
        this.handlerFactory = new HandlerFactory(handlerClass);

        LOG.info("Initializing {} workers...", configuraion.getWorkerCount());
        this.workers = new Worker[configuraion.getWorkerCount()];
        this.createWorkers();
        this.provider = new UrlProvider();
    }

    /**
     * Starts the worker threads and starts crawling.
     */
    public void start() {
        if (!this.hasUrls()) {
            throw new IllegalStateException(
                    "Can't start crawling with no seed urls.");
        }

        if (this.isRunning) {
            throw new IllegalStateException("Workers had been already started!");
        }

        for (int i = 0; i < this.workers.length; i++) {
            LOG.info("Starting worker #{}...", i);
            this.workers[i].start();
        }
        this.isRunning = true;
    }

    /**
     * Sends shutdown signal to all worker threads. This method returns
     * immediately but worker threads are closed after a short period of time.
     * The amount will be in many cases maximally worker relax time.
     */
    public void shutdownGracefully() {
        if (!this.isRunning) {
            throw new IllegalStateException(
                    "Can't shutdown workers before starting them!");
        }

        for (int i = 0; i < this.workers.length; i++) {
            LOG.info("Shutting down worker #{}...", i);
            this.workers[i].shutdownGracefully();
        }
    }

    private void createWorkers() {
        for (int i = 0; i < this.workers.length; i++) {
            this.workers[i] = new Worker(this, createHandler());
        }
    }

    private Handler createHandler() {
        return this.handlerFactory.create();
    }

    /**
     * Adds specified URL to collection of URLs that should be <b>included</b>
     * in crawling process (seed URLs). This method should be called before
     * starting the crawler.
     * 
     * @param url
     *            URL to include in crawling process
     */
    public void includeUrl(String url) {
        if (this.isRunning) {
            throw new IllegalStateException("Workers had been already started!");
        }

        try {
            this.addUrl(new URL(url));
        } catch (MalformedURLException e) {
            LOG.error("Can't include url!", e);
        }
    }

    /**
     * Adds specified URL to collection of URLs that should be <b>excluded</b>
     * in crawling process. This method should be called before starting the
     * crawler.
     * 
     * @param url
     *            URL to include in crawling process
     */
    public void excludeUrl(String url) {
        if (this.isRunning) {
            throw new IllegalStateException("Workers had been already started!");
        }

        try {
            this.provider.addVisited(new URL(url));
        } catch (MalformedURLException e) {
            LOG.error("Can't include url!", e);
        }
    }

    void addUrl(URL url) {
        this.provider.addUrl(url);
    }

    void addUrls(URL... urls) {
        this.provider.addUrls(urls);
    }

    public URL aquireUrl() {
        return this.provider.aquireUrl();
    }

    /**
     * Returns the amount of visited pages.
     * 
     * @return amount of visited pages
     */
    public int getVisitedCount() {
        return this.provider.getVisitedCount();
    }

    /**
     * Returns the amount of not visited pages.
     * 
     * @return amount of not visited pages
     */
    public int getNotVisitedCount() {
        return this.provider.getNotVisitedCount();
    }

    /**
     * Returns the amount of all (visited and not visited) pages present in
     * crawler's UrlProvider.
     * 
     * @return total amount of known URLs
     */
    public int getTotalCount() {
        return this.provider.getTotalCount();
    }

    /**
     * Returns the configuration of this crawler.
     * 
     * @return configuration of this crawler
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public boolean hasUrls() {
        return !this.provider.isEmpty();
    }
}
