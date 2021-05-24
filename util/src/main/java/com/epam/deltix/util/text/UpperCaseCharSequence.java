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
package com.epam.deltix.util.text;

public class UpperCaseCharSequence implements CharSequence {

    private CharSequence            delegate;

    public UpperCaseCharSequence() {
    }    
    
    public UpperCaseCharSequence(CharSequence delegate) {
        this.delegate = delegate;
    }

    public void setCharSequence(CharSequence delegate) {
        this.delegate = delegate;
    }        
    
    @Override
    public int length() {
        return delegate.length();
    }

    @Override
    public char charAt(int index) {
        final char result = delegate.charAt(index);
        return Character.isLowerCase(result) ? Character.toUpperCase(result) : result;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
}