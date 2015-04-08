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

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents factory that creates Handler objects.
 */
public class HandlerFactory {

    private static final Logger LOG = LoggerFactory
            .getLogger(HandlerFactory.class);

    private Class<? extends Handler> type;
    private Constructor<? extends Handler> ctr;

    public HandlerFactory(Class<? extends Handler> handlerClass) {
        this.type = handlerClass;
    }

    /**
     * Returns new instance of Handler.
     * 
     * @return new instance of handler
     */
    public Handler create() {
        if (this.ctr == null) {
            try {
                this.initializeConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Handler handler = ctr.newInstance();
            return handler;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private void initializeConstructor() throws NoSuchMethodException {
        LOG.info("Initializing construcor for handler {}.", this.type.getName());
        for (Constructor<?> constructor : this.type.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                this.ctr = (Constructor<? extends Handler>) constructor;
                return;

            }
        }
        throw new NoSuchMethodException(
                "Handler must have accessible no-arg constructor!");
    }
}
