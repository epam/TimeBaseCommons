// Code source of this file:
//   http://grepcode.com/file/repo1.maven.org/maven2/
//     org.apache.maven/maven-artifact/3.1.1/
//       org/apache/maven/artifact/versioning/ComparableVersion.java/
//
// Modifications made on top of the source:
//   1. Changed
//        package org.apache.maven.artifact.versioning;
//      to
//        package org.apache.hadoop.util;
//      to package deltix.tools
//   2. Removed author tags to clear hadoop author tag warning
//        author <a href="mailto:kenney@apache.org">Kenney Westerhof</a>
//        author <a href="mailto:hboutemy@apache.org">Herve Boutemy</a>
//   3. Added ability to match artifact name and version
//   4. BigDecimal removed
//
package com.epam.deltix.util.io;

/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.*;

/**
 * Generic implementation of version comparison.
 * <p>Features:</p>
 * <ul>
 * <li>mixing of '<code>-</code>' (dash) and '<code>.</code>' (dot) separators,</li>
 * <li>transition between characters and digits also constitutes a separator:
 * <code>1.0alpha1 =&gt; [1, 0, alpha, 1]</code></li>
 * <li>unlimited number of version components,</li>
 * <li>version components in the text can be digits or strings,</li>
 * <li>strings are checked for well-known qualifiers and the qualifier ordering is used for version ordering.
 * Well-known qualifiers (case insensitive) are:<ul>
 * <li><code>alpha</code> or <code>a</code></li>
 * <li><code>beta</code> or <code>b</code></li>
 * <li><code>milestone</code> or <code>m</code></li>
 * <li><code>rc</code> or <code>cr</code></li>
 * <li><code>snapshot</code></li>
 * <li><code>(the empty string)</code> or <code>ga</code> or <code>final</code></li>
 * <li><code>sp</code></li>
 * </ul>
 * Unknown qualifiers are considered after known qualifiers, with lexical order (always case insensitive),
 * </li>
 * <li>a dash usually precedes a qualifier, and is always less important than something preceded with a dot.</li>
 * </ul>
 *
 * @see <a href="https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning">"Versioning" on Maven Wiki</a>
 * @see <a href="https://semver.org">Semantic Versioning</a>
 *
 */
public class SemanticVersion implements Comparable<SemanticVersion> {
    private final String value;
    private final String description;
    private String id;

    private String name; // name without version component

    private String canonical;

    private ListItem items;

    private interface Item {
        int INTEGER_ITEM = 0;
        int STRING_ITEM = 1;
        int LIST_ITEM = 2;

        int compareTo(Item item);

        int getType();

        boolean isNull();
    }

    /**
     * Represents a numeric item in the version item list.
     */
    private static class IntegerItem implements Item {
        static final IntegerItem ZERO = new IntegerItem();

        private static final int NULL_VALUE = 0;
        private final long value;

        private IntegerItem() {
            this.value = NULL_VALUE;
        }

        IntegerItem(String str) {
            this.value = Long.parseLong(str);
        }

        public int getType() {
            return INTEGER_ITEM;
        }

        public boolean isNull() {
            return NULL_VALUE == value;
        }

        public int compareTo(Item item) {
            if (item == null)
                return NULL_VALUE == value ? 0 : 1; // 1.0 == 1, 1.1 > 1

            switch (item.getType()) {
                case INTEGER_ITEM:
                    return Long.compare(value, ((IntegerItem) item).value);

                case STRING_ITEM:
                    return 1; // 1.1 > 1-sp

                case LIST_ITEM:
                    return 1; // 1.1 > 1-1

                default:
                    throw new RuntimeException("invalid item: " + item.getClass());
            }
        }

        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * Represents a string in the version item list, usually a qualifier.
     */
    private static class StringItem implements Item {

        private static final String[] QUALIFIERS = {"alpha", "beta", "milestone", "rc", "snapshot", "", "sp"};
        private static final List<String> _QUALIFIERS = Arrays.asList(QUALIFIERS);
        private static final Properties ALIASES = new Properties();

        static {
            ALIASES.put("ga", "");
            ALIASES.put("final", "");
            ALIASES.put("cr", "rc");
        }

        /**
         * A comparable value for the empty-string qualifier. This one is used to determine if a given qualifier makes
         * the version older than one without a qualifier, or more recent.
         */
        private static final String RELEASE_VERSION_INDEX = String.valueOf(_QUALIFIERS.indexOf(""));

        private String value;

        StringItem(String value, boolean followedByDigit) {
            if (followedByDigit && value.length() == 1) {
                // a1 = alpha-1, b1 = beta-1, m1 = milestone-1
                switch (value.charAt(0)) {
                    case 'a':
                        value = "alpha";
                        break;
                    case 'b':
                        value = "beta";
                        break;
                    case 'm':
                        value = "milestone";
                        break;
                    default:
                        // ignore
                }
            }
            this.value = ALIASES.getProperty(value, value);
        }

        public int getType() {
            return STRING_ITEM;
        }

        public boolean isNull() {
            return (comparableQualifier(value).compareTo(RELEASE_VERSION_INDEX) == 0);
        }

        /**
         * Returns a comparable value for a qualifier.
         * <p>
         * This method takes into account the ordering of known qualifiers then unknown qualifiers with lexical ordering.
         * <p>
         * just returning an Integer with the index here is faster, but requires a lot of if/then/else to check for -1
         * or QUALIFIERS.size and then resort to lexical ordering. Most comparisons are decided by the first character,
         * so this is still fast. If more characters are needed then it requires a lexical sort anyway.
         *
         * @param qualifier qualifier
         * @return an equivalent value that can be used with lexical comparison
         */
        static String comparableQualifier(String qualifier) {
            int i = _QUALIFIERS.indexOf(qualifier);

            return i == -1 ? (_QUALIFIERS.size() + "-" + qualifier) : String.valueOf(i);
        }

        public int compareTo(Item item) {
            if (item == null) {
                // 1-rc < 1, 1-ga > 1
                return comparableQualifier(value).compareTo(RELEASE_VERSION_INDEX);
            }
            switch (item.getType()) {
                case INTEGER_ITEM:
                    return -1; // 1.any < 1.1 ?

                case STRING_ITEM:
                    return comparableQualifier(value).compareTo(comparableQualifier(((StringItem) item).value));

                case LIST_ITEM:
                    return -1; // 1.any < 1-1

                default:
                    throw new RuntimeException("invalid item: " + item.getClass());
            }
        }

        public String toString() {
            return value;
        }
    }

    /**
     * Represents a version list item. This class is used both for the global item list and for sub-lists (which start
     * with '-(number)' in the version specification).
     */
    private static class ListItem extends ArrayList<Item> implements Item {

        @Override
        public int getType() {
            return LIST_ITEM;
        }

        @Override
        public boolean isNull() {
            return (size() == 0);
        }

        void normalize() {
            for (ListIterator<Item> iterator = listIterator(size()); iterator.hasPrevious(); ) {
                Item item = iterator.previous();
                if (item.isNull()) {
                    iterator.remove(); // remove null trailing items: 0, "", empty list
                } else {
                    break;
                }
            }
        }

        @Override
        public int compareTo(Item item) {
            if (item == null) {
                if (size() == 0) {
                    return 0; // 1-0 = 1- (normalize) = 1
                }
                Item first = get(0);
                return first.compareTo(null);
            }
            switch (item.getType()) {
                case INTEGER_ITEM:
                    return -1; // 1-1 < 1.0.x

                case STRING_ITEM:
                    return 1; // 1-1 > 1-sp

                case LIST_ITEM:
                    Iterator<Item> left = iterator();
                    Iterator<Item> right = ((ListItem) item).iterator();

                    while (left.hasNext() || right.hasNext()) {
                        Item l = left.hasNext() ? left.next() : null;
                        Item r = right.hasNext() ? right.next() : null;

                        // if this is shorter, then invert the compare and mul with -1
                        int result = l == null ? -1 * r.compareTo(l) : l.compareTo(r);

                        if (result != 0) {
                            return result;
                        }
                    }

                    return 0;

                default:
                    throw new RuntimeException("invalid item: " + item.getClass());
            }
        }

        public String toString() {
            StringBuilder buffer = new StringBuilder("(");
            for (Iterator<Item> iter = iterator(); iter.hasNext(); ) {
                buffer.append(iter.next());
                if (iter.hasNext()) {
                    buffer.append(',');
                }
            }
            buffer.append(')');
            return buffer.toString();
        }
    }

    public SemanticVersion(String version) {
        this(version, null);
    }

    public SemanticVersion(String version, String description) {
        this.value = version;
        this.description = description;
        parse(version);
    }

    private void parse(String version) {
        items = new ListItem();
        version = version.toLowerCase(Locale.ENGLISH);

        StringBuilder idBuilder = new StringBuilder();

        String[] parts = version.split("[\\-+ ]");

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            if (part.length() > 0) {
                if (Character.isDigit(part.charAt(0))) {
                    parseInternal(part);
                } else
                    items.add(new StringItem(part, false));

                if (part.contains(".") && Character.isDigit(part.charAt(0))) {
                    if (id == null)
                        id = idBuilder.toString();

                    if (name == null) {
                        if (i > 0) {
                            String previous = parts[i - 1];
                            name = version.substring(0, version.indexOf(previous) + previous.length());
                        } else {
                            name = "";
                        }
                    }
                } else {
                    if (idBuilder.length() > 0)
                        idBuilder.append(";");
                    idBuilder.append(part);
                }
            }
        }

        if (id == null)
            id = idBuilder.toString();

        if (name == null)
            name = version;
    }

    private void parseInternal(String version) {

        ListItem list = items;
        Stack<Item> stack = new Stack<>();
        stack.push(list);

        boolean isDigit = false;
        int startIndex = 0;

        for (int i = 0; i < version.length(); i++) {
            char c = version.charAt(i);

            if (c == '.') {
                if (i == startIndex)
                    list.add(IntegerItem.ZERO);
                else
                    list.add(parseItem(isDigit, version.substring(startIndex, i)));

                startIndex = i + 1;
            }
            else if (Character.isDigit(c)) {
                // digits can start after separators only

                if (!isDigit && i > startIndex) {
                    list.add(new StringItem(version.substring(startIndex, i), true));
                    startIndex = i;
                }
                isDigit = true;
            } else {
                if (isDigit && i > startIndex) {
                    list.add(parseItem(true, version.substring(startIndex, i)));
                    startIndex = i;
                }

                isDigit = false;
            }
        }

        if (version.length() > startIndex)
            list.add(parseItem(isDigit, version.substring(startIndex)));

        while (!stack.isEmpty()) {
            list = (ListItem) stack.pop();
            list.normalize();
        }

        canonical = items.toString();
    }

    private static Item parseItem(boolean isDigit, String buf) {
        return isDigit ? new IntegerItem(buf) : new StringItem(buf, false);
    }

    public int compareTo(SemanticVersion v) {
        if (v != null)
            return items.compareTo(v.items);

        return -1;
    }

    /**
     * Returns original version string
     */
    public String   getVersion() {
        return value;
    }

    /**
     * Returns semicolon separated parts of the name component
     * <code>for the input commons-lang3-3.4 returns commons;lang3</code>
     */
    public String   getId() {
        return id;
    }

    public String   getName() {
        return name;
    }

    @Override
    public String toString() {
        return Arrays.toString(items.toArray(new Object[items.size()]));
    }

    public String getDescription() {
        return description;
    }

    public boolean equals(Object o) {
        return (o instanceof SemanticVersion) && canonical.equals(((SemanticVersion) o).canonical);
    }

    public int hashCode() {
        return canonical.hashCode();
    }
}