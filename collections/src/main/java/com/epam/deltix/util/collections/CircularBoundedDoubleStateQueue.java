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
package com.epam.deltix.util.collections;


/**
 *
 * @author PaharelauK
 */
public abstract class CircularBoundedDoubleStateQueue<E> implements DoubleStateQueue<E> {

    private final FixedSizeStack<E> mEmptyElements;
    private final CircularBoundedQueue<E> mReadyElements;
    private boolean mCannibalizeStaleReadyElements = false;

    public CircularBoundedDoubleStateQueue(final int elementCount) {
        mReadyElements = new CircularBoundedQueue<E>(elementCount);
        mEmptyElements = new FixedSizeStack<E>(elementCount); // empty elements have no order, stack is faster than queue
        for (int idx = 0; idx < elementCount; idx++) {
            mEmptyElements.add(newEmptyElement());
        }

    }

    public boolean isCannibalizable() {
        return mCannibalizeStaleReadyElements;
    }

    public void setCannibalizable(boolean cannibalizable) {
        this.mCannibalizeStaleReadyElements = cannibalizable;
    }

    public int capacity() {
        return mEmptyElements.capacity();
    }

    public final void addEmptyElement(E e) {
        synchronized (mEmptyElements) {
            mEmptyElements.add(e);
            mEmptyElements.notify(); //TODO: notify only when this was empty?
        }
    }

    public final void addEmptyElements(int count, E[] elems) {
        synchronized (mEmptyElements) {
            for (int i = 0; i < count; i++) {
                mEmptyElements.add(elems[i]);
            }
            mEmptyElements.notify();
        }
    }


    public final void addReadyElement(E e) {
        synchronized (mReadyElements) {
            mReadyElements.add(e);
            mReadyElements.notify();
        }
    }

    public final void addReadyElements(final int count, E[] elems) {
        synchronized (mReadyElements) {
            for (int i = 0; i < count; i++) {
                mReadyElements.add(elems[i]);
            }
            mReadyElements.notify();
        }
    }


    public final E getEmptyElement() throws InterruptedException {
        synchronized (mEmptyElements) {
            //try {
                while (mEmptyElements.count() == 0) {
                    if (mCannibalizeStaleReadyElements) {
                        assert getCountReadyElements() > 0; // implied: when mEmptyElementsis empty, mReadyElements must have at least capacity-numChannels elements
                        //cannibalized();
                        return getReadyElement();
                    }

                    mEmptyElements.wait();
                }
                return mEmptyElements.remove();
            //} catch (InterruptedException ie) {
            //    ?.notifyAll();
            //    throw ie;
            //}
        }
    }

//    /** Message loss callback */
//    protected void cannibalized() {
//    }

    public final int getEmptyElements(int count, E[] result)
        throws InterruptedException
    {
        synchronized (mEmptyElements) {
            //try {
                int emptyCount;
                while ((emptyCount = mEmptyElements.count()) == 0) {
                    if (mCannibalizeStaleReadyElements) {
                        assert getCountReadyElements() > 0; // implied: when mEmptyElementsis empty, mReadyElements must have at least capacity-numChannels elements
                        result[0] = getReadyElement();
                        //cannibalized();
                        return 1;
                    }

                    mEmptyElements.wait();
                }
                if (emptyCount > count)
                    emptyCount = count;

                for (int i=0; i < emptyCount; i++)
                    result[i] = mEmptyElements.remove();

                return emptyCount;
            //} catch (InterruptedException ie) {
            //    ?.notifyAll();
            //    throw ie;
            //}
        }
    }


    public final E getReadyElement() throws InterruptedException {
        synchronized (mReadyElements) {
            //try {
                while (mReadyElements.count() == 0) {
                    mReadyElements.wait();
                }
                return mReadyElements.remove();
            //} catch (InterruptedException ie) {
            //    ?.notifyAll();
            //    throw ie;
            //}
        }
    }

    public final int getReadyElements(int count, E[] result)
        throws InterruptedException
    {
        synchronized (mReadyElements) {
            //try {
                int readyCount;
                while ((readyCount = mReadyElements.count()) == 0) {
                    mReadyElements.wait();
                }
                if (readyCount > count)
                    readyCount = count;

                for (int i=0; i < readyCount; i++)
                    result[i] = mReadyElements.remove();

                return readyCount;
            //} catch (InterruptedException ie) {
            //    ?.notifyAll();
            //    throw ie;
            //}
        }
    }

    public final int getCountReadyElements() {
        synchronized (mReadyElements) {
            return mReadyElements.count();
        }
    }

    public final int getCountEmptyElements() {
        synchronized (mEmptyElements) {
            return mEmptyElements.count();
        }
    }

    @Override
    public void clearUnsafe() {
        // deadlock prone
        synchronized (mEmptyElements) {
            synchronized (mReadyElements) {
                while (mReadyElements.count() > 0) {
                    E elem = mReadyElements.remove();
                    mEmptyElements.add(elem);
                }
            }
            mEmptyElements.notify();
        }
    }
}