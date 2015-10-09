package deltix.util.lang;

public final class Assert {
    public static boolean  ENABLED = false;
    private Assert() {}

    static {
        try {
            assert false;
        } catch (AssertionError x) {
            ENABLED = true;
        }
    }
}
