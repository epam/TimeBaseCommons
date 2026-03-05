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

/**
*
*/
public class CompositeProgressIndicator implements MsgProgressIndicator {

    double totalWork = 0;
    MsgProgressIndicator listener;

    public CompositeProgressIndicator(MsgProgressIndicator listener) {
        this.listener = listener;
    }

    @Override
    public void message(final String text) {
        listener.message(text);
    }

    @Override
    public void setTotalWork(double v) {
        totalWork += v;
        listener.setTotalWork(totalWork);
    }

    @Override
    public void setWorkDone(double v) {
        listener.incrementWorkDone(v);
    }

    @Override
    public void incrementWorkDone(double inc) {
        listener.incrementWorkDone(inc);
    }

    void reset() {
        totalWork = 0;
        listener.setTotalWork(0);
    }
}