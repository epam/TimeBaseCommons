package deltix.util.currency;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import deltix.custom.statestreet.fxa.utils.*;
import deltix.util.csvx.*;
import deltix.util.lang.*;

public class CurrencyCodeList {

    public static List<CurrencyCode> CURRENCY_CODE_LIST;

    public static final int          CODE_IDX     = 0;
    public static final int          NUM_IDX      = 1;
    public static final int          CURRENCY_IDX = 2;
    public static final int          LOCATION_IDX = 3;

    static {
        CURRENCY_CODE_LIST = new ArrayList<CurrencyCode> ();
        try {

            final String path = "deltix/util/currency/currency.csv";
            final InputStream is =
                    Common.class.getClassLoader ().getResourceAsStream (path);
            if (is == null)
                throw new FileNotFoundException (path);

            final CSVXReader csv = new CSVXReader (new InputStreamReader (is),
                                                   ';',
                                                   true,
                                                   "");
            while (csv.nextLine ()) {
                CURRENCY_CODE_LIST.add (new CurrencyCode (csv.getString (CODE_IDX,
                                                                         true),
                                                          csv.getString (NUM_IDX,
                                                                         true),
                                                          csv.getString (CURRENCY_IDX,
                                                                         true),
                                                          csv.getString (LOCATION_IDX,
                                                                         true)));
            }
        } catch (final Throwable x) {
            Util.LOGGER.log (Level.SEVERE,
                             "Can not create currency code list",
                             x);
        }
    }

    public static String[] getCurrencyCodes () {
        String[] result = new String[CURRENCY_CODE_LIST.size ()];

        for (int i = 0; i < CURRENCY_CODE_LIST.size (); i++) {
            result[i] = CURRENCY_CODE_LIST.get (i).code;
        }

        return result;
    }

    public static String[] getCurrencyNumericCodes () {
        String[] result = new String[CURRENCY_CODE_LIST.size ()];

        for (int i = 0; i < CURRENCY_CODE_LIST.size (); i++) {
            result[i] = CURRENCY_CODE_LIST.get (i).numeric;
        }

        return result;
    }

    public static class CurrencyCode {
        public final String code;
        public final String numeric;
        public final String currency;
        public final String location;

        public CurrencyCode (final String code,
                             final String numeric,
                             final String currency,
                             final String location) {
            super ();
            this.code = code;
            this.numeric = numeric;
            this.currency = currency;
            this.location = location;
        }

    }

}
