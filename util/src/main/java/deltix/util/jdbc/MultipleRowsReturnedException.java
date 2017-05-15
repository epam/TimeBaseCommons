package deltix.util.jdbc;

import java.sql.SQLException;

/**
 *
 */
public class MultipleRowsReturnedException extends SQLException {
    public MultipleRowsReturnedException () {
        super ("Query returned more than one row");
    }
}
