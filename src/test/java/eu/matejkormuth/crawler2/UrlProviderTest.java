package eu.matejkormuth.crawler2;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

public class UrlProviderTest extends TestCase {

    public void testAddUrl() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addUrl(new URL("http://google.com"));
        assertTrue(provider.isEmpty() == false);
    }

    public void testAddUrls() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addUrls(new URL[] { new URL("http://google.com"),
                new URL("http://example.com") });
        assertTrue(provider.isEmpty() == false);
    }

    public void testAquireUrl() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        URL url = new URL("http://google.com");
        provider.addUrl(url);
        URL url2 = provider.aquireUrl();
        assertTrue(url == url2);
        assertTrue(provider.isEmpty() == true);
    }

    public void testIsEmpty() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        assertTrue(provider.isEmpty() == true);
    }

    public void testAddVisited() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addVisited(new URL("http://google.com"));
        assertTrue(provider.isEmpty() == true);
        assertTrue(provider.getVisitedCount() == 1);
    }

    public void testGetNotVisitedCount() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addUrls(new URL[] { new URL("http://google.com"),
                new URL("http://example.com") });
        assertTrue(provider.isEmpty() == false);
        assertTrue(provider.getNotVisitedCount() == 2);
    }

    public void testGetVisitedCount() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addUrls(new URL[] { new URL("http://google.com"),
                new URL("http://example.com") });
        provider.aquireUrl();
        provider.aquireUrl();
        assertTrue(provider.isEmpty() == true);
        assertTrue(provider.getVisitedCount() == 2);
    }

    public void testGetTotalCount() throws MalformedURLException {
        UrlProvider provider = new UrlProvider();
        provider.addUrls(new URL[] { new URL("http://google.com"),
                new URL("http://example.com") });
        provider.aquireUrl();
        assertTrue(provider.isEmpty() == false);
        assertTrue(provider.getVisitedCount() == 1);
    }

}
