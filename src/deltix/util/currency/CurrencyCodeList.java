package deltix.util.currency;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import deltix.custom.statestreet.fxa.utils.*;
import deltix.qsrv.hf.pub.*;
import deltix.util.collections.*;
import deltix.util.csvx.*;
import deltix.util.lang.*;

public class CurrencyCodeList {

    public static final List<CurrencyCode> CURRENCY_CODE_LIST;
    public static final TwoWayMap          CURRENCY_CODE_MAP;

    public static final int                CODE_IDX     = 0;
    public static final int                NUM_IDX      = 1;
    public static final int                CURRENCY_IDX = 2;
    public static final int                LOCATION_IDX = 3;

    private static int                     TEXT_MARKER  = 0x8000;

    static {
        CURRENCY_CODE_LIST = new ArrayList<CurrencyCode> ();
        CURRENCY_CODE_MAP = new TwoWayMap ();
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
                final String code = csv.getString (CODE_IDX,
                                                   true);

                final short numeric = (short) csv.getInt (NUM_IDX);

                CURRENCY_CODE_LIST.add (new CurrencyCode (code,
                                                          numeric,
                                                          csv.getString (CURRENCY_IDX,
                                                                         true),
                                                          csv.getString (LOCATION_IDX,
                                                                         true)));
                CURRENCY_CODE_MAP.put (code,
                                       String.valueOf (numeric));
            }
        } catch (final Throwable x) {
            Util.LOGGER.log (Level.SEVERE,
                             "Can not create currency code list",
                             x);
        }
    }

    public static String getCurrencyByNumeric (String numeric) {
        return (String) CURRENCY_CODE_MAP.getSecond (numeric);
    }

    public static String getNumericByCurrency (String currency) {
        return (String) CURRENCY_CODE_MAP.getFirst (currency);
    }

    public static CurrencyCode getCurrencyCodeByCode (String code) {
        if (code == null)
            return null;
        for (CurrencyCode currencyCode : CURRENCY_CODE_LIST) {
            if (currencyCode.code.equals (code))
                return currencyCode;
        }
        return null;
    }

    public static CurrencyCode getCurrencyCodeByNumeric (int numeric) {
        for (CurrencyCode currencyCode : CURRENCY_CODE_LIST) {
            if (currencyCode.numeric == numeric)
                return currencyCode;
        }
        return null;
    }

    public static CurrencyCode getCurrencyCodeByObject (final Object value) {
        if (value != null) {
            final String s = String.valueOf (value);
            if (!(Character.isLetter (s.charAt (0)))) {
                try {
                    final int n = Integer.parseInt (s);
                    if ((n & TEXT_MARKER) == 0)
                        return getCurrencyCodeByNumeric (n);
                    else
                        return getCurrencyCodeByCode (CurrencyCodec.intToCode (n));
                } catch (NumberFormatException e) {
                    //
                }
            }
            return getCurrencyCodeByCode (s);
        }
        return null;
    }

    public static class CurrencyCode {
        public final String code;
        public final short  numeric;
        public final String currency;
        public final String location;

        public CurrencyCode (final String code,
                             final short numeric,
                             final String currency,
                             final String location) {
            super ();
            this.code = code;
            this.numeric = numeric;
            this.currency = currency;
            this.location = location;
        }

        @Override
        public String toString () {
            return numeric + " (" + code + ")";
        }
    }

}
