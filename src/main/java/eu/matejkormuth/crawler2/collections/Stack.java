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

/**
 * Represents an array of objects which can be accessed using stack way. It also
 * resizes the stack as needed.
 * 
 * @param <T>
 *            type of the stack
 */
public interface Stack<T> {

    /**
     * Pushes the object on top of the stack.
     * 
     * @param obj
     *            object to push
     */
    public abstract void push(T obj);

    /**
     * Pops the object from top of the stack.
     * 
     * @return popped object
     */
    public abstract T pop();

    /**
     * Exposes backing array. Warning: this is optional operation and may not be
     * supported! Also the returned array can have different length as the
     * amount of objects in stack.
     * 
     * You should probably use {@link #toArray()} method.
     * 
     * @throws UnsupportedOperationException
     *             when this operation is not supported by current stack
     * 
     * @return stack's backing array
     */
    public abstract T[] exposeBackingArray();

    /**
     * Returns array which contains all objects added to stack in default order.
     * 
     * @return array containing all objects in stack in defualt order
     */
    public abstract T[] toArray();

    /**
     * Returns the current amount of objects in stack.
     * 
     * @return amount of objects in stack
     */
    public int size();

}