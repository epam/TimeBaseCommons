package com.epam.deltix.util.currency;

import com.epam.deltix.util.io.BasicIOUtil;
import com.epam.deltix.util.lang.Depends;
import com.epam.deltix.util.lang.StringUtils;
import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.text.CharSequenceParser;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.concurrent.ThreadSafe;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Depends("deltix/util/currency/CurrencyCodes.xml")
@ThreadSafe
public class CurrencyCodeList {
    private static final int                             amount        =   10000;
    private static final CurrencyInfo[]                  numericIndex  = new CurrencyInfo[amount];
    private static final ThreeLetterToObjectMapQuick<CurrencyInfo> symbolicIndex = new ThreeLetterToObjectMapQuick<>(amount);

    static {
        read ();
    }

    @SuppressFBWarnings(value="PATH_TRAVERSAL_IN", justification = "Parsing XML of certain predetermined format")
    private static void read () {
        InputStream is = null;
        try {
            String alternativeLocation = System.getProperty("deltix.qsrv.currency.codes");
            if (StringUtils.isEmpty(alternativeLocation))
                is = BasicIOUtil.openResourceAsStream ("deltix/util/currency/CurrencyCodes.xml");
            else
                is = new FileInputStream(alternativeLocation);
            read(is);
        } catch (final Throwable x) {
            Util.logException("Can not create currency code list", x);
        } finally {
            Util.close (is);
        }
    }

    private static void read(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance ();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final DocumentBuilder db = dbf.newDocumentBuilder ();
        final Document doc = db.parse (is);
        doc.getDocumentElement ().normalize ();

        final NodeList nodeLst = doc.getElementsByTagName ("Currency");

        for (int s = 0; s < nodeLst.getLength (); s++) {

            final Node node = nodeLst.item (s);

            if (node.getNodeType () == Node.ELEMENT_NODE) {
                final Element element = (Element) node;

                String legacyNumericCode = getText (element, "LegacyNumericCode");
                final CurrencyInfo info = new CurrencyInfo(getText (element,
                                                                     "AlphabeticCode"),
                                                            CharSequenceParser.parseShort (getText (element,
                                                                                                    "NumericCode")),
                                                            getText (element,
                                                                     "Name"),
                                                            getText (element,
                                                                     "Country"),
                                                            legacyNumericCode == null ? Short.MIN_VALUE:
                                                                    CharSequenceParser.parseShort (legacyNumericCode));
                numericIndex[info.numericCode] = info;
                if (info.legacyNumericCode != Short.MIN_VALUE) {
                    if (numericIndex[info.legacyNumericCode] != null) {
                        System.out.printf("ERROR while loading CurrencyCodes.xml. Codes are equal in currencies %s and %s\n",
                                numericIndex[info.legacyNumericCode], info);
                        System.out.printf("Writing currency %s\n", info);
                    }
                    numericIndex[info.legacyNumericCode] = info;
                }
                symbolicIndex.put (info.symbolicCode, info);
            }

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
        CurrencyInfo[] result = new CurrencyInfo[symbolicIndex.size()];
        if (symbolicIndex.size() > 0)
            symbolicIndex.valuesToArray(result);
        return result;
    }

    public static String numericToSymbolic (final int code) {
        final CurrencyInfo info = getInfoByNumeric (code);

        return (info == null ? null : info.symbolicCode);
    }

    public static int symbolicToNumeric (String code, final int notFoundValue) {
        final CurrencyInfo info = getInfoBySymbolic (code);
        return (info == null ? notFoundValue : info.numericCode);
    }

    public static short symbolicToNumeric (CharSequence code, final short notFoundValue) {
        final CurrencyInfo info = getInfoBySymbolic (code);
        return (info == null ? notFoundValue : info.numericCode);
    }

    public static short symbolicToNumeric (CharSequence code, int start, int end, final short notFoundValue) {
        final CurrencyInfo info = getInfoBySymbolic (code, start, end);
        return (info == null ? notFoundValue : info.numericCode);
    }

    public static CurrencyInfo getInfoByNumeric (final int code) {
        return (code >= 0  && code < amount ?  numericIndex[code] : null);
    }

    public static CurrencyInfo getInfoBySymbolic (CharSequence code) {
        return getInfoBySymbolic(code, 0, code.length());
    }

    public static CurrencyInfo getInfoBySymbolic (CharSequence code, int start, int end) {
        return symbolicIndex.get(code, start, end, null);
    }

    public static CurrencyInfo getInfoBySymbolic (String code) {
        return getInfoBySymbolic((CharSequence) code);
    }

    public static class CurrencyInfo implements Comparable<CurrencyInfo> {
        public final String symbolicCode;
        public final short  numericCode;
        public final String description;
        public final String location;
        public final short legacyNumericCode;

        private CurrencyInfo (final String code,
                              final short numeric,
                              final String currency,
                              final String location,
                              final short legacyNumericCode) {
            super ();
            this.symbolicCode = code;
            this.numericCode = numeric;
            this.description = currency;
            this.location = location;
            this.legacyNumericCode = legacyNumericCode;
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
