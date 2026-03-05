package com.epam.deltix.util.archive;

import com.epam.deltix.qsrv.util.archive.DXDataInputStream;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class Test_DXData {

    @Test
    public void test() throws IOException {

        File file = File.createTempFile("X31", "dxdata");

        DXDataInputStream in = new DXDataInputStream(file);
    }
}
