package deltix.util.currency;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import deltix.qsrv.hf.pub.*;
import deltix.util.csvx.*;
import deltix.util.io.IOUtil;
import deltix.util.lang.*;

public class CurrencyCodeList {
    private static final CurrencyInfo []            numericIndex =
        new CurrencyInfo [1000];

    private static final Map <String, CurrencyInfo> symbolicIndex =
        new HashMap <String, CurrencyInfo> (1000);

    private static final int                CODE_IDX     = 0;
    private static final int                NUM_IDX      = 1;
    private static final int                CURRENCY_IDX = 2;
    private static final int                LOCATION_IDX = 3;

    private static int                     TEXT_MARKER  = 0x8000;

    static {
        try {
            final String path = "deltix/util/currency/currency.csv";
            final InputStream is = IOUtil.openResourceAsStream (path);

            final CSVXReader csv = 
                new CSVXReader (new InputStreamReader (is), ';', true, path);

            while (csv.nextLine ()) {
                CurrencyInfo    info =
                    new CurrencyInfo (
                        csv.getString (CODE_IDX, true),
                        (short) csv.getInt (NUM_IDX),
                        csv.getString (CURRENCY_IDX, true),
                        csv.getString (LOCATION_IDX, true)
                    );

                numericIndex [info.numericCode] = info;
                symbolicIndex.put (info.symbolicCode, info);
            }
        } catch (final Throwable x) {
            Util.LOGGER.log (Level.SEVERE,
                             "Can not create currency code list",
                             x);
        }
    }

    public static CurrencyInfo []       getCodes () {
        return (symbolicIndex.values ().toArray (new CurrencyInfo [symbolicIndex.size ()]));
    }

    public static String                numericToSymbolic (int code) {
        CurrencyInfo    info = getInfoByNumeric (code);

        return (info == null ? null : info.symbolicCode);
    }

    public static int                   symbolicToNumeric (String code, int notFoundValue) {
        CurrencyInfo    info = getInfoBySymbolic (code);

        return (info == null ? notFoundValue : info.numericCode);
    }

    public static CurrencyInfo          getInfoByNumeric (int code) {
        return (numericIndex [code]);
    }

    public static CurrencyInfo          getInfoBySymbolic (String code) {
        return (symbolicIndex.get (code));
    }

    public static CurrencyInfo          getCurrencyCodeByObject (final Object value) {
        if (value != null) {
            final String s = String.valueOf (value);

            if (!(Character.isLetter (s.charAt (0)))) {
                try {
                    final int n = Integer.parseInt (s);
                    if ((n & TEXT_MARKER) == 0)
                        return getInfoByNumeric (n);
                    else
                        return getInfoBySymbolic (CurrencyCodec.intToCode (n));
                } catch (NumberFormatException e) {
                    //
                }
            }
            
            return getInfoBySymbolic (s);
        }
        return null;
    }

    public static boolean isValidValue (final Object value) {
        return getCurrencyCodeByObject (value) != null;
    }

    public static class CurrencyInfo {
        public final String symbolicCode;
        public final short  numericCode;
        public final String description;
        public final String location;

        private CurrencyInfo (final String code,
                             final short numeric,
                             final String currency,
                             final String location) {
            super ();
            this.symbolicCode = code;
            this.numericCode = numeric;
            this.description = currency;
            this.location = location;
        }

        @Override
        public String toString () {
            return numericCode + " (" + symbolicCode + ")";
        }
    }
}
