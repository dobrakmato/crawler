package eu.matejkormuth.crawler2.example;

import eu.matejkormuth.crawler2.Configuration;
import eu.matejkormuth.crawler2.Crawler;

public class MyCrawler {

    static Crawler crawler;
    static int lastCrawled;

    public static void main(String[] args) {
        Configuration config = Configuration
                .create()
                .workerThreads(8)
                .relaxLength(500)
                .build();

        crawler = new Crawler(MyHandler.class, config);
        crawler.includeUrl("http://slovnik.juls.savba.sk/locutio/");
        crawler.excludeUrl("http://slovnik.juls.savba.sk");
        crawler.start();

        // Report status each 500 ms.
        new Thread(new Runnable() {
            public void run() {
                while (true) {

                    int crawled = crawler.getVisitedCount();
                    int add = crawled - lastCrawled;
                    lastCrawled = crawled;
                    int notcrawled = crawler.getNotVisitedCount();

                    System.out.println(crawled + " (" + notcrawled + ") / "
                            + (crawled + notcrawled) + " +" + add);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }
}
