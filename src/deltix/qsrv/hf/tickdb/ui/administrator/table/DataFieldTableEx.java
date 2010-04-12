package deltix.qsrv.hf.tickdb.ui.administrator.table;

import javax.swing.table.*;

public abstract class DataFieldTableEx extends DataFieldTable {

    public DataFieldTableEx (final TableModel model) {
        super (model);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void doLayout () {
        final TableColumn resizingColumn = getResizingColumn ();
        if (resizingColumn == null) {
            setWidthsFromPreferredWidths (false);
        } else {
            // JTable behaves like a layout manger - but one in which the
            // user can come along and dictate how big one of the children
            // (columns) is supposed to be.

            // A column has been resized and JTable may need to distribute
            // any overall delta to other columns, according to the resize mode.
            final int columnIndex = viewIndexForColumn (resizingColumn);
            int delta = getWidth () - getColumnModel ().getTotalColumnWidth ();
            accommodateDelta (columnIndex,
                              delta);
            delta = getWidth () - getColumnModel ().getTotalColumnWidth ();

            // If the delta cannot be completely accomodated, then the
            // resizing column will have to take any remainder. This means
            // that the column is not being allowed to take the requested
            // width. This happens under many circumstances: For example,
            // AUTO_RESIZE_NEXT_COLUMN specifies that any delta be distributed
            // to the column after the resizing column. If one were to attempt
            // to resize the last column of the table, there would be no
            // columns after it, and hence nowhere to distribute the delta.
            // It would then be given entirely back to the resizing column,
            // preventing it from changing size.
            if (autoResizeMode == AUTO_RESIZE_OFF) {
                if (delta > 0) {
                    resizingColumn.setWidth (resizingColumn.getWidth () + delta);
                }
            } else {
                if (delta != 0) {
                    resizingColumn.setWidth (resizingColumn.getWidth () + delta);
                }
            }

            // At this point the JTable has to work out what preferred sizes
            // would have resulted in the layout the user has chosen.
            // Thereafter, during window resizing etc. it has to work off
            // the preferred sizes as usual - the idea being that, whatever
            // the user does, everything stays in synch and things don't jump
            // around.
            setWidthsFromPreferredWidths (true);
        }
        layout ();
    }

    // Distribute delta over columns, as indicated by the autoresize mode.
    private void accommodateDelta (final int resizingColumnIndex,
                                   final int delta) {
        final int columnCount = getColumnCount ();
        int from = resizingColumnIndex;
        int to = columnCount;

        // Use the mode to determine how to absorb the changes.
        switch (autoResizeMode) {
            case AUTO_RESIZE_NEXT_COLUMN:
                from = from + 1;
                to = Math.min (from + 1,
                               columnCount);
            break;
            case AUTO_RESIZE_SUBSEQUENT_COLUMNS:
                from = from + 1;
                to = columnCount;
            break;
            case AUTO_RESIZE_LAST_COLUMN:
                from = columnCount - 1;
                to = from + 1;
            break;
            default:
                return;
        }

        final int start = from;
        final int end = to;
        final TableColumnModel cm = columnModel;
        final Resizable3 r = new Resizable3 () {
            @Override
            public int getElementCount () {
                return end - start;
            }

            @Override
            public int getLowerBoundAt (final int i) {
                return cm.getColumn (i + start).getMinWidth ();
            }

            @Override
            public int getUpperBoundAt (final int i) {
                return cm.getColumn (i + start).getMaxWidth ();
            }

            @Override
            public int getMidPointAt (final int i) {
                return cm.getColumn (i + start).getWidth ();
            }

            @Override
            public void setSizeAt (final int s,
                                   final int i) {
                cm.getColumn (i + start).setWidth (s);
            }
        };

        int totalWidth = 0;
        for (int i = from; i < to; i++) {
            final TableColumn aColumn = columnModel.getColumn (i);
            final int input = aColumn.getWidth ();
            totalWidth = totalWidth + input;
        }

        adjustSizes (totalWidth + delta,
                     r,
                     false);

        return;
    }

    private void setWidthsFromPreferredWidths (final boolean inverse) {
        final int totalWidth = getWidth ();
        final int totalPreferred = getPreferredSize ().width;
        final int target = !inverse ? totalWidth : totalPreferred;

        final TableColumnModel cm = columnModel;
        final Resizable3 r = new Resizable3 () {
            @Override
            public int getElementCount () {
                return cm.getColumnCount ();
            }

            @Override
            public int getLowerBoundAt (final int i) {
                return cm.getColumn (i).getMinWidth ();
            }

            @Override
            public int getUpperBoundAt (final int i) {
                return cm.getColumn (i).getMaxWidth ();
            }

            @Override
            public int getMidPointAt (final int i) {
                if (!inverse) {
                    return cm.getColumn (i).getPreferredWidth ();
                }
                             else {
                                 return cm.getColumn (i).getWidth ();
                             }
                         }

            @Override
            public void setSizeAt (final int s,
                                   final int i) {
                if (!inverse) {
                    cm.getColumn (i).setWidth (s);
                }
                             else {
                                 cm.getColumn (i).setPreferredWidth (s);
                             }
                         }
        };

        adjustSizes (target,
                     r,
                     inverse);
    }

    private int viewIndexForColumn (final TableColumn aColumn) {
        final TableColumnModel cm = getColumnModel ();
        for (int column = 0; column < cm.getColumnCount (); column++) {
            if (cm.getColumn (column) == aColumn) {
                return column;
            }
        }
        return -1;
    }

    private void adjustSizes (final long target,
                              final Resizable3 r,
                              final boolean inverse) {
        final int N = r.getElementCount ();
        long totalPreferred = 0;
        for (int i = 0; i < N; i++) {
            totalPreferred += r.getMidPointAt (i);
        }
        Resizable2 s;
        if ((target < totalPreferred) == !inverse) {
            s = new Resizable2 () {
                @Override
                public int getElementCount () {
                    return r.getElementCount ();
                }

                @Override
                public int getLowerBoundAt (final int i) {
                    return r.getLowerBoundAt (i);
                }

                @Override
                public int getUpperBoundAt (final int i) {
                    return r.getMidPointAt (i);
                }

                @Override
                public void setSizeAt (final int newSize,
                                       final int i) {
                    r.setSizeAt (newSize,
                                 i);
                }

            };
        } else {
            s = new Resizable2 () {
                @Override
                public int getElementCount () {
                    return r.getElementCount ();
                }

                @Override
                public int getLowerBoundAt (final int i) {
                    return r.getMidPointAt (i);
                }

                @Override
                public int getUpperBoundAt (final int i) {
                    return r.getUpperBoundAt (i);
                }

                @Override
                public void setSizeAt (final int newSize,
                                       final int i) {
                    r.setSizeAt (newSize,
                                 i);
                }

            };
        }
        adjustSizes (target,
                     s,
                     !inverse);
    }

    private void adjustSizes (long target,
                              final Resizable2 r,
                              final boolean limitToRange) {
        long totalLowerBound = 0;
        long totalUpperBound = 0;
        for (int i = 0; i < r.getElementCount (); i++) {
            totalLowerBound += r.getLowerBoundAt (i);
            totalUpperBound += r.getUpperBoundAt (i);
        }

        if (limitToRange) {
            target = Math.min (Math.max (totalLowerBound,
                                         target),
                               totalUpperBound);
        }

        for (int i = 0; i < r.getElementCount (); i++) {
            final int lowerBound = r.getLowerBoundAt (i);
            final int upperBound = r.getUpperBoundAt (i);
            // Check for zero. This happens when the distribution of the delta
            // finishes early due to a series of "fixed" entries at the end.
            // In this case, lowerBound == upperBound, for all subsequent terms.
            int newSize;
            if (totalLowerBound == totalUpperBound) {
                newSize = lowerBound;
            } else {
                final double f = (double) (target - totalLowerBound) / (totalUpperBound - totalLowerBound);
                newSize = (int) Math.round (lowerBound + f * (upperBound - lowerBound));
                // We'd need to round manually in an all integer version.
                // size[i] = (int)(((totalUpperBound - target) * lowerBound +
                // (target - totalLowerBound) * upperBound)/(totalUpperBound-totalLowerBound));
            }
            r.setSizeAt (newSize,
                         i);
            target -= newSize;
            totalLowerBound -= lowerBound;
            totalUpperBound -= upperBound;
        }
    }

    private TableColumn getResizingColumn () {
        return (tableHeader == null) ? null
                                         : tableHeader.getResizingColumn ();
    }

    private interface Resizable2 {
        public int getElementCount ();

        public int getLowerBoundAt (int i);

        public int getUpperBoundAt (int i);

        public void setSizeAt (int newSize,
                               int i);
    }

    private interface Resizable3 extends Resizable2 {
        public int getMidPointAt (int i);
    }
}
