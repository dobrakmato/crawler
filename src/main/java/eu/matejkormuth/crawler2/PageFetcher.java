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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.matejkormuth.crawler2.documents.ErrorDocument;

/**
 * Class that creates HTTP connections and fetches documents.
 */
public class PageFetcher {

    private static final Logger LOG = LoggerFactory
            .getLogger(PageFetcher.class);
    private static final int STATUS_OK = 200;
    private final HttpClient client;
    private final DefaultResponseHandler responseHandler;

    /**
     * Creates a new PageFetcher instance using Apache commons-io library
     * HttpClient.
     */
    public PageFetcher() {
        this.client = HttpClients.createDefault();
        this.responseHandler = new DefaultResponseHandler();
    }

    /**
     * Returns Document representation of content on specified URL.
     * 
     * @param url
     *            URL of document
     * @return Document representating content of specified URL
     */
    public Document fetch(URL url) {
        HttpUriRequest request = this.createRequest(url);
        try {
            LOG.debug("Fetching " + url.toString() + "...");
            return this.execute(request, url);
        } catch (IOException e) {
            LOG.error("Can't execute request.", e);
            return null;
        }

    }

    private Document execute(HttpUriRequest request, URL url)
            throws ClientProtocolException, IOException {
        return addUrl(this.client.execute(request, this.responseHandler), url);
    }

    private Document addUrl(Document doc, URL url) {
        doc.url = url;
        return doc;
    }

    private HttpUriRequest createRequest(URL url) {
        HttpGet getRequest = new HttpGet(toUri(url));
        this.addHeaders(getRequest);
        return getRequest;
    }

    private URI toUri(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void addHeaders(HttpGet request) {
        request.setHeader("a", "b");
    }

    private final class DefaultResponseHandler implements
            ResponseHandler<Document> {

        public Document handleResponse(HttpResponse response)
                throws ClientProtocolException, IOException {
            if (response.getStatusLine().getStatusCode() == STATUS_OK) {
                return this.handleOk(response);
            } else {
                return this.handleError(response);
            }

        }

        private Document handleError(HttpResponse response) {
            return new ErrorDocument(response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        }

        private Document handleOk(HttpResponse response) throws IOException {
            HttpEntity entity = response.getEntity();

            String contentEncoding = "UTF-8";
            if (entity.getContentEncoding() != null) {
                contentEncoding = entity.getContentEncoding().getValue();
            }

            String contentType = ContentType.TEXT_HTML.getMimeType();
            if (entity.getContentType().getValue().contains(";")) {
                String val = entity.getContentType().getValue();
                contentType = val.split(";")[0].trim();
                String charset = val.split(";")[1].trim();
                if (charset.startsWith("charset=")) {
                    contentEncoding = charset.substring(8);
                }
            } else {
                contentType = entity.getContentType().getValue();
            }

            return Document.create(contentType, contentEncoding,
                    entity.getContentLength(), entity.getContent());
        }
    }

}
