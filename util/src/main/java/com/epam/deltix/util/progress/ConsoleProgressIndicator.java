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
 *  Displays a progress bar on the console.
 */
public class ConsoleProgressIndicator implements ProgressIndicator {
    protected int                 width = 50;
    protected int                 numBarsShown = -1;
    protected String              prefix = "[";
    protected String              suffix = "]";
    protected char                bar = '*';
    protected char                blank = ' ';
    protected double              totalWork = 0;
    protected double              workDone = 0;
    protected boolean             inlined;
    protected boolean             showPercentage;

    public boolean isInlined() {
        return inlined;
    }

    public void setInlined(boolean inlined) {
        this.inlined = inlined;
    }        

    public boolean isShowPercentage() {
        return showPercentage;
    }

    public void setShowPercentage(boolean showPercentage) {
        this.showPercentage = showPercentage;
    }
    
    public char                 getBlank () {
        return blank;
    }

    public void                 setBlank (char blank) {
        this.blank = blank;
    }
    
    public char                 getBar () {
        return bar;
    }

    public void                 setBar (char bar) {
        this.bar = bar;
    }

    public String               getPrefix () {
        return prefix;
    }

    public void                 setPrefix (String prefix) {
        this.prefix = prefix;
    }

    public String               getSuffix () {
        return suffix;
    }

    public void                 setSuffix (String suffix) {
        this.suffix = suffix;
    }

    public int                  getWidth () {
        return width;
    }

    public void                 setWidth (int width) {
        this.width = width;
    }
        
    public void                 show () {
        
        final StringBuilder line = new StringBuilder();
        
        final int numBars = (int) ((workDone / totalWork) * width + 0.5);
        
        int newNumBars =
            workDone > totalWork ?
                width :
            totalWork == 0 ? 
                0 : numBars;
        
        if (newNumBars != numBarsShown) {
            if (!inlined) {
                line.append ('\r');
            }                                    
            
            line.append (prefix);                        
            
            int     ii = 0;
            
            while (ii < newNumBars) {
                line.append (bar);                
                ii++;
                ii += appendPercentsIfRequired(line, ii, width);
            }
            
            while (ii < width) {
                line.append (blank);
                ii++;
                ii += appendPercentsIfRequired(line, ii, width);
            }                        
            
            line.append (suffix);                        
            
            numBarsShown = newNumBars;
            
            System.out.print(line);
        }
    }
    
    private int appendPercentsIfRequired(final StringBuilder sb, final int pos, final int width) {
        if (!showPercentage) {
            return 0;
        }
        
        final int p = (int) ((workDone / totalWork) * 100 + 0.5);
        
        int i = 2; // one digit and %                        
        if (p >= 10) {
            i++;
            if (p >= 100) {
                i++;
            }
        }

        if (pos == width / 2 - i) {
                        
            sb.append(p).append("%");
            
            return i;
        }
        
        return 0;
    }
    
    public boolean isEmpty() {
        return totalWork == 0;
    }
    
    public void                 incrementWorkDone (double inc) {
        workDone += inc;
        show ();
    }

    public void                 setTotalWork (double v) {
        totalWork = v;
        show ();
    }

    public void                 setWorkDone (double v) {        
        workDone = v;
        show ();
    }        
}