package deltix.util.currency;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.w3c.dom.Node;

import deltix.qsrv.hf.pub.*;
import deltix.util.io.*;
import deltix.util.lang.*;
import deltix.util.text.*;

@Depends("deltix/util/currency/CurrencyCodes.xml")
public class CurrencyCodeList {
    private static final CurrencyInfo[]            numericIndex  = new CurrencyInfo[1000];
    private static final Map<CharSequence, CurrencyInfo> symbolicIndex = new HashMap<CharSequence, CurrencyInfo> (1000);
    private static int                             TEXT_MARKER   = 0x8000;

    static {
        read ("deltix/util/currency/CurrencyCodes.xml");
    }

    private static void read (final String path) {
        InputStream is = null;
        try {
            is = BasicIOUtil.openResourceAsStream (path);
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
            final DocumentBuilder db = dbf.newDocumentBuilder ();
            final Document doc = db.parse (is);
            doc.getDocumentElement ().normalize ();

            final NodeList nodeLst = doc.getElementsByTagName ("Currency");

            for (int s = 0; s < nodeLst.getLength (); s++) {

                final Node node = nodeLst.item (s);

                if (node.getNodeType () == Node.ELEMENT_NODE) {
                    final Element element = (Element) node;

                    final CurrencyInfo info = new CurrencyInfo (getText (element,
                                                                         "AlphabeticCode"),
                                                                CharSequenceParser.parseShort (getText (element,
                                                                                                        "NumericCode")),
                                                                getText (element,
                                                                         "Name"),
                                                                getText (element,
                                                                         "Country"));
                    numericIndex[info.numericCode] = info;
                    symbolicIndex.put (info.symbolicCode,
                                       info);
                }

            }
        } catch (final Throwable x) {
            Util.LOGGER.log (Level.SEVERE,
                             "Can not create currency code list",
                             x);
        } finally {
            Util.close (is);
        }
    }

    private static String getText (final Element element,
                                   final String tag) {
        NodeList fstNm;
        try {
            final NodeList nodes = element.getElementsByTagName (tag);
            if (nodes.getLength() == 0)
                return null;

            final Element fstNmElmnt = (Element) nodes.item (0);
            fstNm = fstNmElmnt.getChildNodes ();

            return (fstNm.item(0)).getNodeValue();
        } catch (final Throwable x) {
            return null;
        }

    }

    public static CurrencyInfo[] getCodes () {
        return (symbolicIndex.values ().toArray (new CurrencyInfo[symbolicIndex.size ()]));
    }

    public static String numericToSymbolic (final int code) {
        final CurrencyInfo info = getInfoByNumeric (code);

        return (info == null ? null : info.symbolicCode);
    }

    public static int symbolicToNumeric (final String code,
                                         final int notFoundValue) {
        final CurrencyInfo info = getInfoBySymbolic (code);

        return (info == null ? notFoundValue : info.numericCode);
    }

    public static CurrencyInfo getInfoByNumeric (final int code) {
        return (numericIndex[code]);
    }

    public static CurrencyInfo getInfoBySymbolic (final CharSequence code) {
        return (symbolicIndex.get (code));
    }

    public static CurrencyInfo getInfoBySymbolic (final String code) {
        return getInfoBySymbolic((CharSequence) code);
    }

    public static CurrencyInfo getCurrencyCodeByObject (final Object value) {
        if (value != null) {
            final String s = String.valueOf (value);

            if (!s.isEmpty ()) {

                if (!(Character.isLetter (s.charAt (0)))) {
                    try {
                        final int n = Integer.parseInt (s);
                        if ((n & TEXT_MARKER) == 0)
                            return getInfoByNumeric (n);
                        else
                            return getInfoBySymbolic (CurrencyCodec.intToCode (n));
                    } catch (final NumberFormatException e) {
                        e.printStackTrace ();
                        //
                    } 
                }

                return getInfoBySymbolic (s);
            }
        }
        return null;
    }

    public static boolean isValidValue (final Object value) {
        return getCurrencyCodeByObject (value) != null;
    }

    public static class CurrencyInfo implements Comparable<CurrencyInfo> {
        public final String symbolicCode;
        public final Short  numericCode;
        public final String description;
        public final String location;

        private CurrencyInfo (final String code,
                              final Short numeric,
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

        @Override
        public int compareTo (final CurrencyInfo o) {
            return Util.compare (symbolicCode,
                                 o == null ? null : o.symbolicCode,
                                 true);
        }
    }
}
