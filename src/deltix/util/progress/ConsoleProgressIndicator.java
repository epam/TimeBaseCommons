package deltix.util.progress;

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

    public boolean isInlined() {
        return inlined;
    }

    public void setInlined(boolean inlined) {
        this.inlined = inlined;
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
        int newNumBars =
            workDone > totalWork ?
                width :
            totalWork == 0 ? 
                0 :
                (int) ((workDone / totalWork) * width + 0.5);
        
        if (newNumBars != numBarsShown) {
            if (!inlined) {
                System.out.print ('\r');
            }
            System.out.print (prefix);
            
            int     ii = 0;
            
            while (ii < newNumBars) {
                System.out.print (bar);
                ii++;
            }
            
            while (ii < width) {
                System.out.print (blank);
                ii++;
            }
            
            System.out.print (suffix);
            
            numBarsShown = newNumBars;
        }
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
