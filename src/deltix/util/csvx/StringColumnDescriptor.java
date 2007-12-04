package deltix.util.csvx;

/**
 *
 */
public class StringColumnDescriptor extends ColumnDescriptor {
    public Object                       parseValue (CharSequence s) {
        return (s.toString ());
    }
}
