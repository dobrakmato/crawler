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
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.entity.ContentType;

import eu.matejkormuth.crawler2.documents.BinaryDocument;
import eu.matejkormuth.crawler2.documents.HtmlDocument;

/**
 * Represents page / file that has been fetched from server.
 */
public abstract class Document {

    protected URL url;
    protected Charset encoding;

    protected Document(String contentEncoding) {
        this.encoding = Charset.forName(contentEncoding);
    }

    static Document create(String contentType, String contentEncoding,
            long contentLength, InputStream content) {
        if (ContentType.TEXT_HTML.getMimeType().equalsIgnoreCase(contentType)) {
            return createHtmlDocument(contentType, contentEncoding,
                    contentLength, content);
        } else {
            return createBinaryDocument(contentType, contentEncoding,
                    contentLength, content);
        }
    }

    private static Document createBinaryDocument(String contentType,
            String contentEncoding, long contentLength, InputStream content) {
        return new BinaryDocument(contentType, contentEncoding, contentLength,
                content);
    }

    private static Document createHtmlDocument(String contentType,
            String contentEncoding, long contentLength, InputStream content) {
        return new HtmlDocument(contentType, contentEncoding, contentLength,
                content);
    }

    /**
     * Returns URL that this document has been fetched from.
     * 
     * @return url of this document
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns byte array representation of this document content.
     * 
     * @return content of this document
     */
    public abstract byte[] getContent();

    /**
     * Returns content type of this document.
     * 
     * @return content type of this document
     */
    public abstract ContentType getContentType();

    /**
     * Returns Charset / encoding used in this document.
     * 
     * @return doucment's encoding / charset
     */
    public Charset getEncoding() {
        return this.encoding;
    }

    /**
     * Saves this document to specified path.
     * 
     * @param path
     *            path to save document at
     */
    public void saveTo(Path path) {
        try {
            Files.write(path, this.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the name of this document. Beware that this can returns empty
     * string for index documents with URL like <code>example.com/page/</code>.
     * 
     * @return name of this file
     */
    public String getName() {
        return FilenameUtils.getName(this.url.toString());
    }

    /**
     * Returns the path of this document on server. This has same effect as
     * calling <code>getUrl().getPath()</code>.
     * 
     * @return path of this document on server
     */
    public String getPath() {
        return this.url.getPath();
    }

    /**
     * Returns extension of this document. Beware that this can returns empty
     * string for index documents with URL like <code>example.com/page/</code>.
     * 
     * @return extension of this document (file)
     */
    public String getExtension() {
        return FilenameUtils.getExtension(this.url.toString());
    }

}
