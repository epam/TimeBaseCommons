package deltix.util.parsers.synthetic;

import com.epam.deltix.dfp.Decimal64;
import deltix.util.lang.Util;

/**
 *
 * Spread rule can be defined as text using the following BNF grammar:
 * <pre>
 *     &lt;spread-rule&gt; ::= [ &lt;sign&gt; ] &lt;leg&gt; ( &lt;sign&gt; &lt;leg&gt; )+
 *     &lt;leg&gt; :== [ &lt;ratio&gt; '*' ]  SYMBOL
 *     &lt;ratio&gt; :== ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9' )+
 *     &lt;sign&gt; :== '+' | '-'
 * </pre>
 * SYMBOL can contain any characters except '-', '+', and '*'. Ratio number cannot be equal to zero. Ratio 1 can be skipped, except when SYMBOL starts with a digit. Each symbol may not appear more than once.
 * <p>
 * Examples:
 * <ul>
 *     <li>A+B (is identical to 1*A+1*B)</li>
 *     <li>-A+B (is identical to B-A)</li>
 *     <li>1*A+2*B-3*C</li>
 * </ul>
 *
 */
public class SyntheticInstrumentRule {

    /**
     * System-wide constant that defines maximum number of legs a Synthetic Instrument may have. Some OMS code pre-allocates arrays based on this number.
     */
    public static final int MAX_NUMBER_OF_LEGS = Util.getIntSystemProperty("QuantServer.maxNumberOfSyntheticInstrumentLegs", 16, 2, 256);

    private static final char RATIO_TO_SYMBOL_SEPARATOR = '*';
    private static final char BUY_LEG_SEPARATOR = '+';
    private static final char SELL_LEG_SEPARATOR = '-';

    public final String[] symbols;
    public final double[] ratios;
    public final Decimal64[] decimalRatios;

    public SyntheticInstrumentRule(String[] symbols, Decimal64[] ratios) {
        assert symbols.length == ratios.length;
        assert symbols.length > 1;

        this.symbols = symbols;
        this.ratios = new double[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            this.ratios[i] = ratios[i].toDouble();
        }
        this.decimalRatios = ratios;
    }

    public SyntheticInstrumentRule(int numberOfLegs) {
        if (numberOfLegs < 2)
            throw new IllegalArgumentException("Rule must specify at least two legs");
        this.symbols = new String[numberOfLegs];
        this.ratios = new double[numberOfLegs];
        this.decimalRatios = new Decimal64[numberOfLegs];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbols.length; i++) {
            double ratio = ratios[i];

            String symbol = symbols[i];
            if (ratio < 0) {
                sb.append(SELL_LEG_SEPARATOR);
                ratio = -ratio;
            } else {
                if (i > 0)
                    sb.append(BUY_LEG_SEPARATOR);
            }

            if (ratio != 1 || Character.isDigit(symbol.charAt(0))) {
                if ((ratio - (int)ratio) != 0)
                    sb.append(ratio);
                else
                    sb.append((int)ratio);
                sb.append(RATIO_TO_SYMBOL_SEPARATOR);
            }
            sb.append(symbol);
        }

        return sb.toString();
    }

}

