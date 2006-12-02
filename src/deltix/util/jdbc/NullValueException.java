package deltix.util.jdbc;

import java.sql.SQLException;

/**
 *
 */
public class NullValueException extends SQLException {
    public NullValueException () {
        super ("Query returned null");
    }
}
