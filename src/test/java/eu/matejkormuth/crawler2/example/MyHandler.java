package eu.matejkormuth.crawler2.example;

import java.net.URL;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import eu.matejkormuth.crawler2.Document;
import eu.matejkormuth.crawler2.Handler;

public class MyHandler implements Handler {

    public boolean shouldVisit(URL url) {
        if (FilenameUtils.getExtension(url.toString()).equalsIgnoreCase("spx")) {
            return false;
        }
        return true;
    }

    public void visited(Document document) {
        String path = document.getPath();
        if("".equals(FilenameUtils.getExtension(path))) {
            return;
        }
        document.saveTo(Paths.get("C:/crawl", document.getName()));
    }
}
