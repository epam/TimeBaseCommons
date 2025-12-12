package com.epam.deltix.qsrv.util.archive;

import org.apache.commons.compress.archivers.ArchiveEntry;

import java.util.Date;

public class DXDataEntry implements ArchiveEntry {

    protected long size = SIZE_UNKNOWN;
    protected String name;

    DXDataEntry() {
    }

    public DXDataEntry(long size, String name) {
        this.size = size;
        this.name = name;
    }

    public DXDataEntry(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public Date getLastModifiedDate() {
        return null;
    }
}
