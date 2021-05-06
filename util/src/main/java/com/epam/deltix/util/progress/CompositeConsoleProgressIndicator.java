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
