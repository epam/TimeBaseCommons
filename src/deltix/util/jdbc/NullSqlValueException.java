package deltix.util.jdbc;

import java.sql.SQLException;

/**
 *
 */
public class NullSqlValueException extends SQLException {
    public NullSqlValueException () {
        super ("Query returned null");
    }
}
