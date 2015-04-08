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
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.matejkormuth.crawler2.collections.Stack;
import eu.matejkormuth.crawler2.collections.UrlStack;
import eu.matejkormuth.crawler2.documents.HtmlDocument;

/**
 * Represents a worker thread.
 */
public class Worker extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
    private static final AtomicInteger workerId = new AtomicInteger();

    private Crawler crawler;
    private Handler handler;
    private PageFetcher fetcher;
    private HtmlAnalyzer htmlAnalyzer;
    private final long relaxLength;
    private boolean isRunning;

    /**
     * Creates a new Worker with specified Handler and controller (Crawler).
     * 
     * @param crawler
     *            crawler
     * @param handler
     *            handler
     */
    public Worker(Crawler crawler, Handler handler) {
        super("Worker-" + workerId.incrementAndGet());
        this.crawler = crawler;
        this.handler = handler;
        this.relaxLength = crawler.getConfiguration().getRelaxLength();

        this.fetcher = new PageFetcher();
        this.htmlAnalyzer = new HtmlAnalyzer();
    }

    @Override
    public void start() {
        this.isRunning = true;
        super.start();
    }

    @Override
    public void run() {
        LOG.info("Worker {} is running.", this.getName());
        while (this.isRunning) {
            this.work();
        }
        LOG.info("Worker {} has stopped.", this.getName());
    }

    private void work() {
        if (this.crawler.hasUrls()) {
            this.fetch();
        } else {
            this.relax();
        }
    }

    private void relax() {
        try {
            Thread.sleep(this.relaxLength);
        } catch (InterruptedException e) {
            // Thread has been interrupted.
            LOG.error("{} thread has been interrupted!", this.getName());
        }
    }

    private void fetch() {
        // Get URL and fetch.
        URL url = this.crawler.aquireUrl();
        if (url == null) {
            return;
        }

        Document document = this.fetcher.fetch(url);
        if (document == null) {
            return;
        }

        if (document instanceof HtmlDocument) {
            this.processHrefs((HtmlDocument) document);
        }
        this.forwardDocument(document);
    }

    private void forwardDocument(Document document) {
        this.handler.visited(document);
    }

    private void processHrefs(HtmlDocument document) {
        URL[] hrefs = this.htmlAnalyzer.extractLinks(document);
        Stack<URL> shouldVisitUrls = new UrlStack();
        for (URL href : hrefs) {
            if (this.handler.shouldVisit(href)) {
                shouldVisitUrls.push(href);
            }
        }
        this.crawler.addUrls(shouldVisitUrls.toArray());
    }

    /**
     * Sends signal to shutdown the thread.
     */
    public void shutdownGracefully() {
        this.isRunning = false;
    }

}
