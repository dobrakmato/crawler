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

import eu.matejkormuth.crawler2.documents.HtmlDocument;

/**
 * Represents analyzer of HTML documents which can perform many operations using
 * different strategies.
 */
public class HtmlAnalyzer {

    private LinkExtractingStrategy linkExtracting;

    public HtmlAnalyzer() {
        this.linkExtracting = new RegexLinkExtractingStrategy();
    }

    /**
     * Extracts links (hrefs) from specified HtmlDocument.
     * 
     * @param document
     *            document to extract hrefs (links) from
     * @return array of URLs presents in specified document
     */
    public URL[] extractLinks(HtmlDocument document) {
        return this.linkExtracting.extract(document);
    }
}
