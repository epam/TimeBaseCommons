package deltix.util.progress;

/**
 *  Displays a progress bar on the console.
 */
public class ConsoleProgressIndicator implements ProgressIndicator {
    private int                 width = 50;
    private int                 numBarsShown = -1;
    private String              prefix = "[";
    private String              suffix = "]";
    private char                bar = '*';
    private char                blank = ' ';
    private double              totalWork = 0;
    private double              workDone = 0;

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
            totalWork == 0 ? 
                0 :
                (int) ((workDone / totalWork) * width + 0.5);
        
        if (newNumBars != numBarsShown) {
            System.out.print ('\r');
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
