package eu.matejkormuth.crawler2;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import eu.matejkormuth.crawler2.RegexLinkExtractingStrategy;
import eu.matejkormuth.crawler2.documents.HtmlDocument;
import junit.framework.TestCase;

public class RegexLinkExtractingStrategyTest extends TestCase {

    public void testExtract() throws MalformedURLException {
        byte[] input = "<html><body><a href=\"http://google.com\">link</a></body></html>"
                .getBytes(Charset.forName("UTF-8"));
        HtmlDocument doc = new HtmlDocument("text/html", "UTF-8", input);
        doc.url = new URL("http://facebook.com");
        RegexLinkExtractingStrategy extractor = new RegexLinkExtractingStrategy();
        URL[] urls = extractor.extract(doc);
        assertTrue(urls.length == 1);
        assertTrue(urls[0].toExternalForm().equals("http://google.com"));
    }
}
