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
package com.epam.deltix.util.concurrent;

/**
 *
 */
public class Signal {
    private boolean             state = false;
    private Throwable           error;

    public synchronized void    set() {
        state = true;
        error = null;

        notify();
    }

    public synchronized void    set(Throwable e) {
        state = true;
        error = e;

        notify();
    }

    public synchronized void    verify() throws IllegalStateException {
        if (error != null)
            throw new IllegalStateException(error);
    }

    public synchronized void    reset() {
        state = false;
        error = null;
    }

    public synchronized boolean await() throws InterruptedException {
        while (!state)
            wait(5000);

        state = false;
        return true;
    }


    /**
     *  If not signalled, wait given timeout and returns state value.
     */
    public synchronized boolean await(int timeout) throws InterruptedException {
        if (!state)
            wait(timeout);

        if(state){
            state = false;
            return true;
        }

        return state;
    }

}