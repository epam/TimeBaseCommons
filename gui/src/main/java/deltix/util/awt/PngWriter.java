package deltix.util.awt;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.zip.*;


/**
 *
 * @author  yuri
 */
public class PngWriter {
	private static final byte [] TAG_ID = {-119, 'P', 'N', 'G', 13, 10, 26, 10, 0, 0, 0, 13};	// Identifier
    private static final byte [] TAG_IHDR = {'I', 'H', 'D', 'R'};  // Header
	private static final byte [] TAG_IDAT = {'I', 'D', 'A', 'T'};  // Pixels
	private static final byte [] TAG_IEND = {'I', 'E', 'N', 'D'};  // End
	private static final byte [] TAG_PLTE = {'P', 'L', 'T', 'E'}; // Palette
    private static final byte [] TAG_TRNS = {'t', 'R', 'N', 'S'}; // Transparency

	private static final byte [] COMPRESSION_FILTER_INTERLACE = {0, 0, 0};

	
	private boolean		mEncodeAlpha = false;
	private int			mCompressionLevel = 9;
	
	private static class Target {
		byte []	mTarget;
		int		mCurrentPosition;
		int		mMaxPosition;
		CRC32	mCRC;
		
		private Target (int initialSize) {
			mTarget = new byte [initialSize];
			mCRC = new CRC32 ();
		}
		
		private void	updateCRC (int start) {
			mCRC.reset ();
			mCRC.update (mTarget, start, mCurrentPosition - start);
			int crcValue = (int) mCRC.getValue ();
			write (crcValue);
		}
		
		private void	updateCRC (byte [] data) {
			mCRC.reset ();
			mCRC.update (data);
			int crcValue = (int) mCRC.getValue ();
			write (crcValue);
		}
		
		private void	updateCRC (byte [] data, int start) {
			mCRC.reset ();
			mCRC.update (data);
			mCRC.update (mTarget, start, mCurrentPosition - start);
			int crcValue = (int) mCRC.getValue ();
			write (crcValue);
		}
		
		private void write (short n) {
			byte[] temp = {(byte) ((n >> 8) & 0xff), (byte) (n & 0xff)};
			write (temp);
		}

		private void write (int n) {
			byte[] temp = {(byte) ((n >> 24) & 0xff),
						   (byte) ((n >> 16) & 0xff),
						   (byte) ((n >> 8) & 0xff),
						   (byte) (n & 0xff)};
			write (temp);
		}

		private void  write (byte n) {
			byte[] temp = {n};
			write (temp);
		}

		private void write ( byte [] data) {
			mMaxPosition = Math.max (mMaxPosition, mCurrentPosition + data.length);
			if (data.length + mCurrentPosition > mTarget.length) {
				mTarget = resizeByteArray (mTarget, mTarget.length + Math.max (1000, mTarget.length));
			}

			System.arraycopy (data, 0, mTarget, mCurrentPosition, data.length);
			mCurrentPosition += data.length;
		}

	}
    
    private static byte[] resizeByteArray (byte [] array, int newLength) {
        byte[]  newArray = new byte[newLength];
        int     oldLength = array.length;

        System.arraycopy(array, 0, newArray, 0, Math.min(oldLength, newLength));
        return (newArray);
    }

	public static int []	grabPixels (Image image, int width, int height) 
		throws InterruptedException
	{
		int	[] pixels = new int [width * height];
		PixelGrabber	pg = new PixelGrabber (
				image, 0, 0,
				width, height, 
				pixels, 0, width);
		
		pg.grabPixels();
		if ((pg.getStatus () & ImageObserver.ABORT) != 0)
			return (null);

		return (pixels);

 	}
	
    private static void writeHeader (
		Target	target,
		int		width,
		int		height,
		boolean	encodeAlpha,
		byte	model
	)
    {
		target.write (TAG_ID);
		
		int startPos = target.mCurrentPosition;

        target.write (TAG_IHDR);
        target.write (width);
        target.write (height);
		target.write ((int) 8); // bit depth

		target.write ((byte) model);
							
		target.write (COMPRESSION_FILTER_INTERLACE);
		
		target.updateCRC (startPos);
    }

    private static void writePalette (Target target, Color [] palette)
    {
		if (palette == null || palette.length <= 0)
			return;
		
        byte[] allPal = new byte [256 * 3];
        byte[] allTransp = new byte [256];
		
		int		numColors = Math.min (palette.length, 256);

		for (int iColor = 0; iColor < numColors; iColor ++) {
			Color	thisColor = palette [iColor];
			if (thisColor != null) {
				allPal [3 * iColor] = (byte) thisColor.getRed ();
				allPal[3 * iColor + 1] = (byte) thisColor.getGreen ();
				allPal[3 * iColor + 2] = (byte) thisColor.getBlue ();
				
				allTransp [iColor] = (byte) thisColor.getAlpha ();
			} else
				allTransp [iColor] = (byte) 0xff;
        }
			
        target.write ((int) 768);
		target.write (TAG_PLTE);
		target.write (allPal);
		target.updateCRC (allPal);
		
        target.write ((int) 256);
		target.write (TAG_TRNS);
		target.write (allTransp);
		target.updateCRC (allTransp);
    }

	private static void writeImageData (
		Target	target, 
		byte [] imageData, 
		int		width, 
		int		height, 
		boolean encodeAlpha,
		int		compressionLevel
	) 
		throws IOException
	{
        int rowsLeft = height;  // number of rows remaining to write
        int startRow = 0;       // starting row to process this time through
        int nRows;              // how many rows to grab at a time

        byte[] scanLines;       // the scan lines to be compressed
        int scanPos;            // where we are in the scan lines
        int startPos;           // where this line's actual pixels start (used for filtering)

        byte[] compressedLines; // the resultant compressed lines
        int nCompressed;        // how big is the compressed area?

        //int depth;              // color depth ( handle only 8 or 32 )

        PixelGrabber pg;

        int bytesPerPixel = (encodeAlpha) ? 4 : 3;

        Deflater scrunch = new Deflater(compressionLevel);
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream(1024);

        DeflaterOutputStream compBytes = new DeflaterOutputStream(outBytes, scrunch);
		
		int		positionInArray = 0;
		
		while (rowsLeft > 0) {
			nRows = Math.min(32767 / (width * (bytesPerPixel + 1)), rowsLeft);
			nRows = Math.max( nRows, 1 );

			int		numPixels = width * nRows;

			byte [] pixels = new byte [numPixels];
			numPixels = Math.min (numPixels, imageData.length - positionInArray);
			System.arraycopy (imageData, positionInArray, pixels, 0, numPixels);
			positionInArray += numPixels;

			/*
			 * Create a data chunk. scanLines adds "nRows" for
			 * the filter bytes.
			 */
			scanLines = new byte[width * nRows * bytesPerPixel +  nRows];


			scanPos = 0;
			startPos = 1;
			for (int i = 0; i < width * nRows; i++) {
				if (i % width == 0) {
					scanLines[scanPos++] = 0;
					startPos = scanPos;
				}
				scanLines[scanPos++] = (byte) ((pixels[i] >> 16) & 0xff);
				scanLines[scanPos++] = (byte) ((pixels[i] >>  8) & 0xff);
				scanLines[scanPos++] = (byte) ((pixels[i]) & 0xff);
				if (encodeAlpha) {
					scanLines[scanPos++] = (byte) ((pixels[i] >> 24) & 0xff);
				}
			}

			/*
			 * Write these lines to the output area
			 */
			compBytes.write(scanLines, 0, scanPos);

			startRow += nRows;
			rowsLeft -= nRows;
		}
		compBytes.close();

		/*
		 * Write the compressed bytes
		 */
		compressedLines = outBytes.toByteArray();
		nCompressed = compressedLines.length;


		target.write (nCompressed);
		target.write (TAG_IDAT);
		startPos = target.mCurrentPosition;
		target.write (compressedLines);
		target.updateCRC (TAG_IDAT, startPos);

		scrunch.finish();
    }

    private static void  writeEnd (Target target) {
		target.write ((int) 0);
		target.write (TAG_IEND);
		target.updateCRC (TAG_IEND);
    }
	
	public static byte [] encode (
		byte []		imageData,
		Color []	palette,
		int			width, 
		int			height, 
		boolean		encodeAlpha, 
		int			compressionLevel
	)
		throws IOException
	{ 
        Target	target = new Target (((width + 1) * height * 3) + 200);
		
        writeHeader (target, width, height, encodeAlpha, (byte) 3);
		writePalette (target, palette);
        writeImageData (target, imageData, width, height, encodeAlpha, compressionLevel);
        writeEnd (target);
		target.mTarget = resizeByteArray (target.mTarget, target.mMaxPosition);
		return (target.mTarget);
    }
}
