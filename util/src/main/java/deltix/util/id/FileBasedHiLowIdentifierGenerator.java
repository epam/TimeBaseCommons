package deltix.util.id;

import java.io.File;

public abstract class FileBasedHiLowIdentifierGenerator extends HiLowIdentifierGenerator {

	protected final File file;
	
	protected FileBasedHiLowIdentifierGenerator(File dir, String key, int blockSize, long startId) {
		super(key, blockSize, startId);
		dir.getAbsoluteFile().mkdirs();
        file = new File (dir, "sequence-"+key+".id");
	}
}
