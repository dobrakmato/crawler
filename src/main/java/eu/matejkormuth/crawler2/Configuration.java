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

/**
 * Represents a {@link Crawler} configuration. Use
 * {@link Configuration#create()} method to create ConfigurationBuilder and then
 * {@link ConfigurationBuilder#build()} method to create Configuration instance.
 */
public class Configuration {
    private int workerCount = 4;
    private long relaxLength = 500;
    private boolean ignoreNoFollow = false;

    private Configuration() {
    }

    /**
     * Returns amount of worker threads.
     * 
     * @return amount of worker threads
     */
    public int getWorkerCount() {
        return this.workerCount;
    }

    /**
     * Returns length of <i>relax time</i> for worker thread in milliseconds.
     * <p>
     * Worker threads periodically tries to fetch content of URLs to crawl. When
     * there are no other available URLs to crawl, worker thread sleep this
     * amount of milliseconds before querying UrlProvider again.
     * </p>
     * 
     * @return length of relax time in milliseconds
     */
    public long getRelaxLength() {
        return this.relaxLength;
    }

    /**
     * Returns whether to ignore <code>rel="nofollow"</code> on links.
     * 
     * @return true if crawler should ignore nofollow values in rel attributes
     *         in hrefs, false otherwise
     */
    public boolean getIgnoreNoFollow() {
        return this.ignoreNoFollow;
    }

    /**
     * Creates a new ConfigurationBuilder.
     * 
     * @return new instance of ConfigurationBuilder
     */
    public static ConfigurationBuilder create() {
        return new ConfigurationBuilder();
    }

    public static class ConfigurationBuilder {
        private Configuration conf;

        public ConfigurationBuilder() {
            this.conf = new Configuration();
        }

        /**
         * Builds and returns Configuration based on previous method calls.
         * 
         * @return Configuration instance
         */
        public Configuration build() {
            return this.conf;
        }

        /**
         * Specifies amount of worker threads.
         * 
         * @param count
         *            amount of worker threads
         */
        public ConfigurationBuilder workerThreads(int count) {
            this.conf.workerCount = count;
            return this;
        }

        /**
         * Specifies length of relax time in milliseconds.
         * 
         * <p>
         * Worker threads periodically tries to fetch content of URLs to crawl.
         * When there are no other available URLs to crawl, worker thread sleep
         * this amount of milliseconds before querying UrlProvider again.
         * </p>
         * 
         * @param length
         *            length of relax time in milliseconds
         */
        public ConfigurationBuilder relaxLength(long length) {
            this.conf.relaxLength = length;
            return this;
        }

        /**
         * Specified whether the crawler should ignore
         * <code>rel="nofollow"</code> on links.
         * 
         * @param ignore
         *            true if crawler should ignore rel=nofollow
         */
        public ConfigurationBuilder ignoreNoFollow(boolean ignore) {
            this.conf.ignoreNoFollow = ignore;
            return this;
        }
    }
}
