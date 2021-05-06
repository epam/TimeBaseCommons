package com.epam.deltix.util.text.table;

public interface Table extends Iterable<Row> {
    
    Column[] getColumns();
    
    Column getColumn(int index);

    Column getColumn(String name);
        
}
