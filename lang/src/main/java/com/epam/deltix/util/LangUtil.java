package com.epam.deltix.util;

public final class LangUtil {

    public static RuntimeException rethrowUnchecked(Exception e) {
        throw LangUtil.<RuntimeException>throwUnchecked(e);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Exception> T throwUnchecked(Exception e) throws T {
        throw (T) e;
    }

}
