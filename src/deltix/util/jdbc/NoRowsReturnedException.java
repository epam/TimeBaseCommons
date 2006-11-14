package deltix.util.jdbc;

import java.sql.SQLException;

/**
 *
 */
public class NoRowsReturnedException extends SQLException {
    public NoRowsReturnedException () {
        super ("Query returned 0 rows");
    }
}
