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
package com.epam.deltix.util.progress;

import java.util.ArrayList;
import java.util.List;

public class CompositeConsoleProgressIndicator {
    
    private static final int                    TOTAL_WIDTH = 80;

    private final List<CompositedIndicator>     indicators = new ArrayList<>();
    
    public CompositedIndicator addIndicator(String prefix) {
        final CompositedIndicator result = new CompositedIndicator();

        result.prefix = prefix;
        
        if (!indicators.isEmpty()) {
            result.setInlined(true);
        }
        indicators.add(result);
        
        for (CompositedIndicator p : indicators) {
            p.width = TOTAL_WIDTH / indicators.size();
        }        
        
        return result;
    }
    
    public void clean() {
        indicators.clear();
    }
    
    public void show() {
        for (CompositedIndicator p : indicators) {
            p.show();
        }
    }
    
    public class CompositedIndicator extends ConsoleProgressIndicator {        
        @Override
        public void incrementWorkDone(double inc) {
            workDone += inc;
            CompositeConsoleProgressIndicator.this.show();
        }

        @Override
        public void setTotalWork(double v) {
            totalWork = v;
            CompositeConsoleProgressIndicator.this.show();
        }

        @Override
        public void setWorkDone(double v) {
            workDone = v;
            CompositeConsoleProgressIndicator.this.show();
        }
    }
}