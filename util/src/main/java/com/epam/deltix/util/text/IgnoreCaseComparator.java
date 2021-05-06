package com.epam.deltix.util.text;

import java.text.Collator;
import java.util.Comparator;

public class IgnoreCaseComparator implements Comparator<String> {
    private final Collator c;

    public static final IgnoreCaseComparator INSTANCE = new IgnoreCaseComparator();

    IgnoreCaseComparator() {
        c = Collator.getInstance();
        c.setStrength(Collator.PRIMARY);
    }

    public int compare(String a, String b) {
        return c.compare(a, b);
    }
}
