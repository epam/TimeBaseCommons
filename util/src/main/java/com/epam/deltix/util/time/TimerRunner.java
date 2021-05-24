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
package com.epam.deltix.util.time;

import com.epam.deltix.util.lang.Util;

/**
 *  Isolates java.util.Timer from exceptions thrown by TimerTasks.
 */
public abstract class TimerRunner extends java.util.TimerTask {
    /**
     *  Override {@link #runInternal} instead.
     */
    @Override
    public final void      run () {
        try {
            runInternal();
        }
        catch (Throwable e) {
            try {
                onError(e);
            } catch (Throwable ex) {
                Util.handleException(ex);
            }
        }
    }

    /**
     *  Override to handle errors thrown by {@link #runInternal}.
     *
     *  @param e    The exception.
     */
    protected void          onError (Throwable e) {
        Util.handleException (e);
    }

    /**
     *  Override this method to perform timer task, instead of overriding run ().
     */
    protected abstract void runInternal () throws Exception;
}