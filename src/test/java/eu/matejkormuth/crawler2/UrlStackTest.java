package eu.matejkormuth.crawler2;

import java.net.MalformedURLException;
import java.net.URL;

import eu.matejkormuth.crawler2.collections.UrlStack;
import junit.framework.TestCase;

public class UrlStackTest extends TestCase {

    public void testPush() throws MalformedURLException {
        URL url1 = new URL("http://google.com");
        URL url2 = new URL("http://facebook.com");
        UrlStack stack = new UrlStack(11);
        stack.push(url1);
        stack.push(url2);
        assertTrue(stack.size() == 2);
    }

    public void testPop() throws MalformedURLException {
        URL url1 = new URL("http://google.com");
        URL url2 = new URL("http://facebook.com");
        UrlStack stack = new UrlStack(11);
        stack.push(url1);
        stack.push(url2);
        assertTrue(stack.size() == 2);
        URL url3 = stack.pop();
        assertTrue(url3.equals(url2));
        assertTrue(stack.size() == 1);
        URL url4 = stack.pop();
        assertTrue(url4.equals(url1));
        assertTrue(stack.size() == 0);
    }

    public void testExposeBackingArray() throws MalformedURLException {
        URL url1 = new URL("http://google.com");
        URL url2 = new URL("http://facebook.com");
        UrlStack stack = new UrlStack(11);
        stack.push(url1);
        stack.push(url2);
        URL[] array = stack.exposeBackingArray();
        assertTrue(array.length == 11);
    }

    public void testToArray() throws MalformedURLException {
        URL url1 = new URL("http://google.com");
        URL url2 = new URL("http://facebook.com");
        UrlStack stack = new UrlStack();
        stack.push(url1);
        stack.push(url2);
        URL[] array = stack.toArray();
        assertTrue(array.length == 2);
        assertTrue(array[0] == url1);
        assertTrue(array[1] == url2);
    }

    public void testSize() throws MalformedURLException {
        UrlStack stack = new UrlStack();
        stack.push(new URL("http://google.com"));
        stack.push(new URL("http://example.com"));
        stack.push(new URL("http://facebook.com"));

        assertTrue(stack.size() == 3);
    }

}
