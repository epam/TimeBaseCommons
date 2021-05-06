package com.epam.deltix.util.id;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;

public abstract class FileBasedHiLowIdentifierGenerator extends HiLowIdentifierGenerator {

	protected final File file;
	
	@SuppressFBWarnings(value = "PATH_TRAVERSAL_IN", justification = "Reading several first bytes of given file, interpreting them as last used sequence number")
	protected FileBasedHiLowIdentifierGenerator(File dir, String key, int blockSize, long startId) {
		super(key, blockSize, startId);
		dir.getAbsoluteFile().mkdirs();
        file = new File (dir, "sequence-"+key+".id");
	}
}
