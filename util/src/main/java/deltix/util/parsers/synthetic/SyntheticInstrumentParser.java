package deltix.util.parsers.synthetic;

import com.epam.deltix.dfp.Decimal64;
import deltix.qsrv.hf.framework.mdp.impl.parser.generated.SyntheticRuleBaseVisitor;
import deltix.qsrv.hf.framework.mdp.impl.parser.generated.SyntheticRuleLexer;
import deltix.qsrv.hf.framework.mdp.impl.parser.generated.SyntheticRuleParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.LinkedHashSet;

public class SyntheticInstrumentParser {

    private static class Leg {
        private final String symbol;
        private Decimal64 ratio;

        private Leg(String symbol, String ratio) {
            this.symbol = symbol;
            this.ratio = Decimal64.parse(ratio);
            if (this.ratio.equals(Decimal64.ZERO)) {
                throw new IllegalArgumentException("ratio cannot be zero");
            }
        }

        private Leg negate() {
            this.ratio = this.ratio.negate();
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Leg leg = (Leg) o;

            return symbol.equals(leg.symbol);
        }

        @Override
        public int hashCode() {
            return symbol.hashCode();
        }
    }

    private static class ThrowingErrorListener extends BaseErrorListener {

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                                Object offendingSymbol,
                                int line, int charPositionInLine,
                                String msg,
                                RecognitionException e)
            throws ParseCancellationException
        {
            throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }

    private static final ThrowingErrorListener ERROR_LISTENER = new ThrowingErrorListener();

    private final SyntheticRuleLexer lexer = new SyntheticRuleLexer(null);
    private final SyntheticRuleParser parser = new SyntheticRuleParser(null);
    private final InputVisitor inputVisitor = new InputVisitor();

    public SyntheticInstrumentParser() {
        this(true);
    }

    private SyntheticInstrumentParser(boolean throwExceptions) {
        if (throwExceptions) {
            lexer.removeErrorListeners();
            lexer.addErrorListener(ERROR_LISTENER);
            parser.removeErrorListeners();
            parser.addErrorListener(ERROR_LISTENER);
        }
    }

    public static SyntheticInstrumentRule createParserAndParse(String textRule) {
        return new SyntheticInstrumentParser().parse(textRule);
    }

    public SyntheticInstrumentRule parse(String textRule) {
        if (textRule == null || textRule.isEmpty())
            throw new IllegalArgumentException("Synthetic rule cannot be empty");

        try {
            lexer.setInputStream(new ANTLRInputStream(textRule));
            parser.setInputStream(new CommonTokenStream(lexer));

            inputVisitor.init();
            return inputVisitor.visit(parser.input());
        } catch (ParseCancellationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static class InputVisitor extends SyntheticRuleBaseVisitor<SyntheticInstrumentRule> {

        private final SyntheticRuleVisitor syntheticRuleVisitor = new SyntheticRuleVisitor();

        void init() {
            syntheticRuleVisitor.clearLegs();
        }

        @Override
        public SyntheticInstrumentRule visitInput(SyntheticRuleParser.InputContext ctx) {
            LinkedHashSet<Leg> legs = syntheticRuleVisitor.visit(ctx.syntheticRule());
            if (legs.size() < 2)
                throw new IllegalArgumentException("Rule must specify at least two legs");

            String[] symbols = new String[legs.size()];
            Decimal64[] ratios = new Decimal64[legs.size()];
            int i = 0;
            for (final Leg leg : legs) {
                symbols[i] = leg.symbol;
                ratios[i] = leg.ratio;
                ++i;
            }

            return new SyntheticInstrumentRule(symbols, ratios);
        }
    }

    private static class SyntheticRuleVisitor extends SyntheticRuleBaseVisitor<LinkedHashSet<Leg>> {

        private final LinkedHashSet<Leg> legs = new LinkedHashSet<>();
        private final LegVisitor legVisitor = new LegVisitor();

        void clearLegs() {
            legs.clear();
        }

        @Override
        public LinkedHashSet<Leg> visitPlusSyntheticRule(SyntheticRuleParser.PlusSyntheticRuleContext ctx) {
            visit(ctx.syntheticRule());
            legs.add(checkDuplicate(legVisitor.visit(ctx.leg())));

            return legs;
        }

        @Override
        public LinkedHashSet<Leg> visitMinusSyntheticRule(SyntheticRuleParser.MinusSyntheticRuleContext ctx) {
            visit(ctx.syntheticRule());
            legs.add(checkDuplicate(legVisitor.visit(ctx.leg()).negate()));

            return legs;
        }

        @Override
        public LinkedHashSet<Leg> visitLegSyntheticRule(SyntheticRuleParser.LegSyntheticRuleContext ctx) {
            legs.add(checkDuplicate(legVisitor.visit(ctx.leg())));

            return legs;
        }

        private Leg checkDuplicate(final Leg leg) {
            if (legs.contains(leg))
                throw new IllegalArgumentException("Duplicate leg symbol: \"" + leg + "\"");

            return leg;
        }
    }

    private static class LegVisitor extends SyntheticRuleBaseVisitor<Leg> {

        private final RatioVisitor ratioVisitor = new RatioVisitor();

        @Override
        public Leg visitLegLeft(SyntheticRuleParser.LegLeftContext ctx) {
            return new Leg(unquote(ctx.symbol().getText()), ratioVisitor.visit(ctx.unaryRatio()));
        }

        @Override
        public Leg visitUnarySymbolMinus(SyntheticRuleParser.UnarySymbolMinusContext ctx) {
            return visit(ctx.unarySymbol()).negate();
        }

        @Override
        public Leg visitUnarySymbolPlus(SyntheticRuleParser.UnarySymbolPlusContext ctx) {
            return visit(ctx.unarySymbol());
        }

        @Override
        public Leg visitToSymbol(SyntheticRuleParser.ToSymbolContext ctx) {
            return new Leg(unquote(ctx.symbol().getText()), "1.0");
        }
    }

    private static class RatioVisitor extends SyntheticRuleBaseVisitor<String> {

        @Override
        public String visitUnaryRatioMinus(SyntheticRuleParser.UnaryRatioMinusContext ctx) {
            return "-" + visit(ctx.unaryRatio());
        }

        @Override
        public String visitToRatio(SyntheticRuleParser.ToRatioContext ctx) {
            return visit(ctx.ratio());
        }

        @Override
        public String visitRatio(SyntheticRuleParser.RatioContext ctx) {
            return ctx.getText();
        }

    }

    private static String unquote(final String s) {
        if (s.length() < 2)
            return s;

        if (s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"')
            return s.substring(1, s.length() - 1);

        return s;
    }

}

