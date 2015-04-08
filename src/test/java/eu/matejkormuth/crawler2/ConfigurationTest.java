package eu.matejkormuth.crawler2;
import junit.framework.TestCase;

public class ConfigurationTest extends TestCase {
    public void testConfigurationBuilder() {
        Configuration conf = Configuration
                .create()
                .relaxLength(800)
                .workerThreads(6)
                .ignoreNoFollow(true)
                .build();
        assertNotNull(conf);
        assertEquals(conf.getIgnoreNoFollow(), true);
        assertEquals(conf.getRelaxLength(), 800);
        assertEquals(conf.getWorkerCount(), 6);
    }
}
