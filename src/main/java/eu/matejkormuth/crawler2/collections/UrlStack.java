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
package eu.matejkormuth.crawler2.collections;

import java.net.URL;

/**
 * Represents URL stack.
 */
public class UrlStack implements Stack<URL> {
    private URL[] stack;
    private int top;

    /**
     * Creates a new UrlStack with default starting capacity (16).
     */
    public UrlStack() {
        this(16);
    }

    /**
     * Creates a new UrlStack with specified starting capacity.
     * 
     * @param size
     *            starting capacity of stack
     */
    public UrlStack(int size) {
        this.stack = new URL[size];
        this.top = 0;
    }

    public void push(URL obj) {
        if (this.top == stack.length) {
            this.doubleSize();
        }

        this.stack[this.top++] = obj;
    }

    public URL pop() {
        return this.stack[this.top--];
    }

    public URL[] exposeBackingArray() {
        return this.stack;
    }

    public URL[] toArray() {
        URL[] array = new URL[this.top];
        System.arraycopy(this.stack, 0, array, 0, this.top);
        return array;
    }

    public int size() {
        return this.top;
    }

    /**
     * Doubles the size of backing array.
     */
    private void doubleSize() {
        URL[] newStack = new URL[this.stack.length * 2];
        System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
        this.stack = newStack;
    }
}
