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