package deltix.util.awt;

import java.awt.*;

/**
 *  A utility for displaying text at precise locations.
 */
public class TextDisplay {
	public static final int			CENTER = 1;
	
	private int		mWidth;
	private int		mHeight;
	private int		mX;
	private int		mY;
	private String	mText;
	
    public static Dimension     getStringSize (
        String                      text,
        FontMetrics                 fm 
    )
    {
        return (new Dimension (fm.stringWidth (text), fm.getAscent () + fm.getDescent ()));
    }

    public static void          centerText (
        Graphics                    g,
        String                      text,
        int                         x,
        int                         y
    )
    {
        FontMetrics                 fm = g.getFontMetrics ();

        int			asc = fm.getAscent ();
    	int			desc = fm.getDescent();
        int         h = asc + desc;

        g.drawString (text, x - fm.stringWidth (text) / 2, y + h / 2 - desc);
    }

	/**
	 *	Sets up the text to be displayed at the given point.
	 *
	 *	@param fm	The FontMetrics of the font in question.
	 *	@param text	The string to measure.
	 *	@param x	The x coordinate of the display point.
	 *	@param y	The y coordinate of the display point.
	 *	@param mode	Mode of locating the text with respect to the 
	 *					specified point.
	 */
    public TextDisplay (
        FontMetrics                 fm, 
        String                      text,
        int							x,
        int							y,
        int							mode
    )
    {
    	mText = text;
    	mWidth = fm.stringWidth (mText);
    	
    	int			asc = fm.getAscent ();
    	int			desc = fm.getDescent();
    	
        mHeight = asc + desc;
        
        switch (mode) {
        	case CENTER:
        		mX = x - mWidth / 2;
        		mY = y + mHeight / 2 - desc;
        		break;
        	
        	default:
        		throw new IllegalArgumentException ("Illegal mode: " + mode);        
        }
    }
    
    /**
     *	Returns the width of the text.
     */
    public int		getWidth () {
    	return (mWidth);
    }
    
    /**
     *	Returns the height of the text (not including the leading).
     */
    public int		getHeight () {
    	return (mHeight);
    }
    
    /**
     *	Draws the specified string at the specified location.
     */
    public void		draw (Graphics g) {
    	g.drawString (mText, mX, mY);
    }
}
