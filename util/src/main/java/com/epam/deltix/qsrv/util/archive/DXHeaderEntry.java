package com.epam.deltix.qsrv.util.archive;

public class DXHeaderEntry extends DXDataEntry {

    public final byte[] data;

    public DXHeaderEntry(String name, byte[] data) {
        super(name);
        
        this.data = data;
        this.size = data.length;
    }

    @Override
    public long getSize() {
        return data.length;
    }

}
