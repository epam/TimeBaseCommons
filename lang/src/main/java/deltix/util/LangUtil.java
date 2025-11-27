package deltix.util;

public final class LangUtil {
    // This method is preserved for binary compatibility with older versions
    public static RuntimeException rethrowUnchecked(Exception e) {
        throw LangUtil.<RuntimeException>throwUnchecked(e);
    }

    public static RuntimeException rethrowUnchecked(Throwable e) {
        throw LangUtil.<RuntimeException>throwUnchecked(e);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> T throwUnchecked(Throwable e) throws T {
        throw (T) e;
    }

    /**
     * If provided throwable is not an {@link Exception}, rethrows it using "sneaky throw".
     * <p>
     * This way major errors like {@link OutOfMemoryError} or {@link StackOverflowError} are propagated out.
     * <p>
     * This method mainly intended to be used in catch blocks that catch {@link Throwable}.
     * Normally, only {@link Exception} instances should be caught.
     * However, we have a lot of legacy code that catches {@link Throwable} and suppresses it without rethrowing (possibly with logging).
     * With this method, we can fix such code to rethrow major errors without major refactoring.
     * <p>
     * Additionally, this method is useful when we use global default exception handler ({@link Thread#setDefaultUncaughtExceptionHandler})
     * to detect OOM events. So OOM would be propagated to such handler even if caught by existing "catch Throwable" blocks.
     */
    public static void propagateError(Throwable e) {
        if (!(e instanceof Exception)) {
            throwUnchecked(e);
        }
    }
}
