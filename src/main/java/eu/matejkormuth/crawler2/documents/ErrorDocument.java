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

import org.apache.http.entity.ContentType;

import eu.matejkormuth.crawler2.Document;

/**
 * Special Document implementation which represents error document.
 */
public class ErrorDocument extends Document {

    private final String reasonPhrase;
    private final int statusCode;

    public ErrorDocument(int statusCode, String reasonPhrase) {
        super("UTF-8");
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public byte[] getContent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ContentType getContentType() {
        throw new UnsupportedOperationException();
    }
}
