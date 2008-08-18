package deltix.util.swing;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: PaharelauK
 * Date: Feb 27, 2007
 * Time: 11:30:34 AM
 */
public class LocalObjectTransferable<T> implements Transferable, ClipboardOwner, Serializable {

	private static final long serialVersionUID = 1L;
    
    private T object;
	private DataFlavor dataFlavor;

    @SuppressWarnings ("unchecked")
	public LocalObjectTransferable(T object) {
		this(object, (Class<? extends T>)object.getClass());
	}

	public LocalObjectTransferable(T object, Class<? extends T> clazz) {
		this.object = object;
		dataFlavor = getLocalObjectFlavor(clazz);
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {dataFlavor};
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return dataFlavor.equals(flavor);
	}

	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if ( dataFlavor.equals(flavor) ) {
			return object;
		}
		throw new UnsupportedFlavorException(flavor);
	}

	public T getObject() {
		return object;
	}

	public static DataFlavor getLocalObjectFlavor(Class<?> clazz) {
		try {
			return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class="+clazz.getName());
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    // This method is called when this object is no longer
    // the owner of the item on the system clipboard.
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        //to do nothing
    }
}

