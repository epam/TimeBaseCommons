package deltix.util.csvx;

/**
 *
 */
public class StringColumnDescriptor extends ColumnDescriptor {
    public Object                       parseValue (String s) {
        return (s.intern ());
    }
}
