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