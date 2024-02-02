package deltix.util.time;

import deltix.qsrv.hf.pub.TimeSource;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DefaultTimeSourceProviderTest {

    @After
    public void after() {
        System.clearProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP);
        DefaultTimeSourceProvider.unconfigure();
    }

    @Test
    public void testDefault() {
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof KeeperTimeSource);
    }

    @Test
    public void testConfiguredByProperty() {
        System.setProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP, "MonotonicRealTimeSource");
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof MonotonicRealTimeSource);
    }

    @Test
    public void testConfiguredExplicitly() {
        DefaultTimeSourceProvider.configure("testConf", MonotonicRealTimeSource.getInstance());
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof MonotonicRealTimeSource);
    }

    @Test
    public void testExplicitConfigOverridesProperty() {
        DefaultTimeSourceProvider.configure("testConf", KeeperTimeSource.getInstance());
        System.setProperty(DefaultTimeSourceProvider.TIME_SOURCE_SYS_PROP, "MonotonicRealTimeSource");
        TimeSource timeSource = DefaultTimeSourceProvider.getTimeSourceForApp("test");
        assertTrue(timeSource instanceof KeeperTimeSource);
    }
}
