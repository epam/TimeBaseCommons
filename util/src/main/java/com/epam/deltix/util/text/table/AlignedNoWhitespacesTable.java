package com.epam.deltix.util.text.table;

import com.epam.deltix.util.collections.CharSubSequence;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class AlignedNoWhitespacesTable implements Table {
    
    private static final ANWColumn[]    EMPTY_HEADER = new ANWColumn[0];
    
    private Iterator<CharSequence>      text;
    private ANWColumn[]                 columns;
    private ANWRow                      row;

    public AlignedNoWhitespacesTable(final CharSequence text) {
        this(
                new Iterator<CharSequence>() {

                    final Scanner scanner = new Scanner(text.toString());

                    @Override
                    public boolean hasNext() {
                        return scanner.hasNextLine();
                    }

                    @Override
                    public CharSequence next() {
                        return scanner.nextLine();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                });            
    }    
    
    public AlignedNoWhitespacesTable(final String[] text) {
        this(
                new Iterator<CharSequence>() {
                    int i = 0;
                    
                    @Override
                    public boolean hasNext() {
                        return i < text.length;
                    }
                    
                    @Override
                    public CharSequence next() {
                        return text[i++];
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Not supported.");
                    }
                });
    }
    
    public AlignedNoWhitespacesTable(Iterator<CharSequence> text) {

        this.text = text;
        
        final CharSequence line = text.hasNext() ? text.next() : null;
        
        if (line != null) {

            final List<ANWColumn> parsedCols = new ArrayList<ANWColumn>();

            final StringBuilder colName = new StringBuilder();
            int colNameStart = 0;

            final int len = line.length();

            for (int i = 0; i < len; i++) {
                final char c = line.charAt(i);

                final boolean isChar = !Character.isWhitespace(c);

                if (isChar) {
                    if (colName.length() == 0) {
                        colNameStart = i;
                    }

                    colName.append(c);
                }

                if ((!isChar || i == len - 1)
                        && colName.length() > 0) {

                    parsedCols.add(new ANWColumn(parsedCols.size(),
                            colName.toString(),
                            colNameStart,
                            !isChar ? i - 1 : i));

                    colName.setLength(0);
                }
            }

            columns = parsedCols.toArray(new ANWColumn[parsedCols.size()]);

            row = new ANWRow();
        }
    }

    @Override
    public Column[] getColumns() {
        return columns != null ? columns : EMPTY_HEADER;
    }

    @Override
    public Column getColumn(int index) {
        return getColumns()[index];
    }

    @Override
    public Column getColumn(String name) {
        for (Column c : getColumns()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }        
        return null;
    }
    
    @Override
    public Iterator<Row> iterator() {
        
        if (columns == null) {
            return new Iterator<Row>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public Row next() {
                    throw new UnsupportedOperationException("Empty table.");
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Not supported.");
                }
            };
        }
        
        return new Iterator<Row>() {

            @Override
            public boolean hasNext() {
                return text.hasNext();
            }

            @Override
            public Row next() {
                return row.parse(text.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }
            
    private static class ANWColumn implements Column {
        private final int       index;
        private final String    name;
        private final int       nameStart;
        private final int       nameEnd;

        private ANWColumn(int index, String name, int nameStart, int nameEnd) {
            this.index = index;
            this.name = name;
            this.nameStart = nameStart;
            this.nameEnd = nameEnd;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public String getName() {
            return name;
        }                
    }
    
    private class ANWRow implements Row {

        private final CharSubSequence[]     values;
        
        private ANWRow() {
            values = new CharSubSequence[columns.length];
            
            for (int i = 0; i < values.length; i++) {
                values[i] = new CharSubSequence();
            }
        }
                
        private ANWRow parse(CharSequence line) {
            for (int i = 0; i < values.length; ++i) {
                
                final CharSubSequence value = values[i];
                
                value.delegate = line;

                final ANWColumn col = columns[i];

                value.start = col.nameStart;
                value.end = col.nameEnd;
                
                // start of the value
                if (Character.isWhitespace(line.charAt(value.start))) {
                    final int last = Math.min(line.length() - 1, col.nameEnd);

                    for (int ii = value.start + 1; ii <= last; ++ii) {
                        
                        value.start = ii;                        

                        if (!Character.isWhitespace(line.charAt(ii))) {
                            break;
                        }
                    }
                } else {
                            
                    final int firstPossible = (i == 0) ? 0 : columns[i - 1].nameEnd + 1;
                    
                    for (int ii = value.start - 1; ii >= firstPossible; --ii) {
                        if (Character.isWhitespace(line.charAt(ii))) {
                            break;
                        }                        

                        value.start = ii;                        
                    }                    
                }

                // end of the value
                if (i == values.length - 1) {
                    
                    for (int ii = line.length() - 1; ii >= value.start; --ii) {
                        
                        value.end = ii;
                        
                        if (!Character.isWhitespace(line.charAt(ii))) {
                            break;
                        }
                    }                    
                } else if (Character.isWhitespace(line.charAt(value.end))) {
                    
                    for (int ii = value.end - 1; ii >= value.start; --ii) {

                        value.end = ii;
                        
                        if (!Character.isWhitespace(line.charAt(ii))) {
                            break;
                        }
                    }
                } else {

                    for (int ii = value.end + 1; ii <= columns[i + 1].nameStart - 1; ++ii) {
                        
                        if (Character.isWhitespace(line.charAt(ii))) {
                            break;
                        }
                        
                        value.end = ii;
                    }
                }
                
                ++value.end; // CharSubSequence requires 'end' to be set to the index next after the the last char            }
            }
            
            return this;
        }
        
        @Override
        public CharSequence getValue(int column) {
            return values[column];
        }
    }        
}
