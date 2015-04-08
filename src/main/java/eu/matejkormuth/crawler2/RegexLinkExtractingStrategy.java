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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.matejkormuth.crawler2.collections.Stack;
import eu.matejkormuth.crawler2.collections.UrlStack;
import eu.matejkormuth.crawler2.documents.HtmlDocument;

/**
 * Regular expressions based implementation of LinkExtractingStrategy.
 */
public class RegexLinkExtractingStrategy implements LinkExtractingStrategy {

    private static final Logger LOG = LoggerFactory
            .getLogger(RegexLinkExtractingStrategy.class);
    private static final Pattern HREF_PATTERN = Pattern.compile(
            "href=\"(.*?)\"", Pattern.DOTALL);

    public URL[] extract(HtmlDocument document) {
        Stack<URL> urlStack = new UrlStack(10);
        Matcher m = HREF_PATTERN.matcher(document.getHtml());

        String urlString = null;
        String urlStart = document.getUrl().toString();
        while (m.find()) {
            urlString = m.group(1);

            if (urlString.contains("../")) {
                // Skip this one.
                continue;
            }

            if (!urlString.contains("http://")) {
                urlString = urlStart + urlString;
            }

            try {
                urlStack.push(new URL(urlString));
            } catch (MalformedURLException e) {
                LOG.warn("URL {} is malformed!", urlString);
            }
        }
        return urlStack.toArray();
    }

}
