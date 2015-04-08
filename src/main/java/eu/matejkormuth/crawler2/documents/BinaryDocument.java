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
package eu.matejkormuth.crawler2.documents;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;

import eu.matejkormuth.crawler2.Document;

/**
 * A binary Document.
 */
public class BinaryDocument extends Document {

    private byte[] content;

    public BinaryDocument(String contentType, String contentEncoding,
            long contentLength, InputStream content) {
        super(contentEncoding);
        try {
            this.content = IOUtils.toByteArray(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public ContentType getContentType() {
        return ContentType.DEFAULT_BINARY;
    }
}
