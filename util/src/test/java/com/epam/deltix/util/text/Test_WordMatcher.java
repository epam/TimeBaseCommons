package com.epam.deltix.util.text;

import com.epam.deltix.util.io.IOUtil;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

public class Test_WordMatcher {
    private WordMatcherBuilder                  wm;
    private String []                           words;
    
    @Before
    public void     setUp () throws Exception {
        wm = new WordMatcherBuilder ();
        
        words = IOUtil.readLinesFromClassPath ("deltix/util/text/tickers.txt");
        
        for (String s : words) 
            wm.add (s);        
    }
    
    private void    check (WordMatcher m, CharSequence s, boolean expectedResult) {
        byte []         bytes = new byte [s.length ()];
        
        for (int ii = 0; ii < bytes.length; ii++)
            bytes [ii] = (byte) s.charAt (ii);
        
        assertEquals ("CS match failed for '" + s + "'", expectedResult, m.matches (s));
        assertEquals ("byte match failed for '" + s + "'", expectedResult, m.matches (bytes, 0, bytes.length));
    }
    
    private void    testSuite (WordMatcher m) {
        check (m, "AESK", false);
        check (m, "CSD", false);
        
        for (String s : words)
            check (m, s, true);
        
        for (String s : words)
            check (m, s + "*", false);
        
        int     count = 0;
        
        for (Enumeration <CharSequence> e = m.vocabulary (); e.hasMoreElements (); ) {
            check (m, e.nextElement (), true);
            count++;
        }
        
        assertEquals (words.length, count);
    }
    
    @Test
    public void     testInterpreter () {
        testSuite (wm);
    }    
    
    @Test
    public void     testCompiler () throws Exception {        
        testSuite (wm.compile ());
    }
    
    @Test
    public void     testCompiler32 () throws Exception {        
        testSuite (wm.compile32 ());
    }


    /// FIX Parser application test

    public enum SecurityType {
        WILDCARD,
        ASSET_BACKED_SECURITIES,
        AMENDED_AND_RESTATED,
        OTHER_ANTICIPATION_NOTES,
        BANKERS_ACCEPTANCE,
        BANK_NOTES,
        BILL_OF_EXCHANGES,
        BRADY_BOND,
        BRIDGE_LOAN,
        BUY_SELLBACK,
        CONVERTIBLE_BOND,
        CERTIFICATE_OF_DEPOSIT,
        CALL_LOANS,
        CORP_MORTGAGE_BACKED_SECURITIES,
        COLLATERALIZED_MORTGAGE_OBLIGATION,
        CERTIFICATE_OF_OBLIGATION,
        CERTIFICATE_OF_PARTICIPATION,
        CORPORATE_BOND,
        COMMERCIAL_PAPER,
        CORPORATE_PRIVATE_PLACEMENT,
        COMMON_STOCK,
        DEFAULTED,
        DEBTOR_IN_POSSESSION,
        DEPOSIT_NOTES,
        DUAL_CURRENCY,
        EURO_CERTIFICATE_OF_DEPOSIT,
        EURO_CORPORATE_BOND,
        EURO_COMMERCIAL_PAPER,
        EURO_SOVEREIGNS,
        EURO_SUPRANATIONAL_COUPONS,
        FEDERAL_AGENCY_COUPON,
        FEDERAL_AGENCY_DISCOUNT_NOTE,
        FOREIGN_EXCHANGE_CONTRACT,
        FORWARD,
        FUTURE,
        GENERAL_OBLIGATION_BONDS,
        IOETTE_MORTGAGE,
        LETTER_OF_CREDIT,
        LIQUIDITY_NOTE,
        MATURED,
        MORTGAGE_BACKED_SECURITIES,
        MUTUAL_FUND,
        MORTGAGE_INTEREST_ONLY,
        MULTI_LEG_INSTRUMENT,
        MORTGAGE_PRINCIPAL_ONLY,
        MORTGAGE_PRIVATE_PLACEMENT,
        MISCELLANEOUS_PASS_THROUGH,
        MANDATORY_TENDER,
        MEDIUM_TERM_NOTES,
        NO_SECURITY_TYPE,
        OVERNIGHT,
        OPTION,
        PRIVATE_EXPORT_FUNDING,
        PFANDBRIEFE,
        PROMISSORY_NOTE,
        PREFERRED_STOCK,
        PLAZOS_FIJOS,
        REVENUE_ANTICIPATION_NOTE,
        REPLACED,
        REPURCHASE,
        RETIRED,
        REVENUE_BONDS,
        REVOLVER_LOAN,
        REVOLVER_TERM_LOAN,
        SECURITIES_LOAN,
        SECURITIES_PLEDGE,
        SPECIAL_ASSESSMENT,
        SPECIAL_OBLIGATION,
        SPECIAL_TAX,
        SHORT_TERM_LOAN_NOTE,
        STRUCTURED_NOTES,
        USD_SUPRANATIONAL_COUPONS,
        SWING_LINE_FACILITY,
        TAX_ANTICIPATION_NOTE,
        TAX_ALLOCATION,
        TO_BE_ANNOUNCED,
        US_TREASURY_BILL,
        US_TREASURY_BOND,
        PRINCIPAL_STRIP_OF_A_CALLABLE_BOND_OR_NOTE,
        TIME_DEPOSIT,
        TAX_EXEMPT_COMMERCIAL_PAPER,
        TERM_LOAN,
        INTEREST_STRIP_FROM_ANY_BOND_OR_NOTE,
        TREASURY_INFLATION_PROTECTED_SECURITIES,
        US_TREASURY_NOTE,
        PRINCIPAL_STRIP_FROM_A_NON_CALLABLE_BOND_OR_NOTE,
        TAX_AND_REVENUE_ANTICIPATION_NOTE,
        VARIABLE_RATE_DEMAND_NOTE,
        WARRANT,
        WITHDRAWN,
        EXTENDED_COMM_NOTE,
        INDEXED_LINKED,
        YANKEE_CORPORATE_BOND,
        YANKEE_CERTIFICATE_OF_DEPOSIT;

        public SecurityType fromString(String s) {
            switch (s) {
                case "?" : return WILDCARD;
                case "ABS" : return ASSET_BACKED_SECURITIES;
                case "AMENDED" : return AMENDED_AND_RESTATED;
                case "AN" : return OTHER_ANTICIPATION_NOTES;
                case "BA" : return BANKERS_ACCEPTANCE;
                case "BN" : return BANK_NOTES;
                case "BOX" : return BILL_OF_EXCHANGES;
                case "BRADY" : return BRADY_BOND;
                case "BRIDGE" : return BRIDGE_LOAN;
                case "BUYSELL" : return BUY_SELLBACK;
                case "CB" : return CONVERTIBLE_BOND;
                case "CD" : return CERTIFICATE_OF_DEPOSIT;
                case "CL" : return CALL_LOANS;
                case "CMBS" : return CORP_MORTGAGE_BACKED_SECURITIES;
                case "CMO" : return COLLATERALIZED_MORTGAGE_OBLIGATION;
                case "COFO" : return CERTIFICATE_OF_OBLIGATION;
                case "COFP" : return CERTIFICATE_OF_PARTICIPATION;
                case "CORP" : return CORPORATE_BOND;
                case "CP" : return COMMERCIAL_PAPER;
                case "CPP" : return CORPORATE_PRIVATE_PLACEMENT;
                case "CS" : return COMMON_STOCK;
                case "DEFLTED" : return DEFAULTED;
                case "DINP" : return DEBTOR_IN_POSSESSION;
                case "DN" : return DEPOSIT_NOTES;
                case "DUAL" : return DUAL_CURRENCY;
                case "EUCD" : return EURO_CERTIFICATE_OF_DEPOSIT;
                case "EUCORP" : return EURO_CORPORATE_BOND;
                case "EUCP" : return EURO_COMMERCIAL_PAPER;
                case "EUSOV" : return EURO_SOVEREIGNS;
                case "EUSUPRA" : return EURO_SUPRANATIONAL_COUPONS;
                case "FAC" : return FEDERAL_AGENCY_COUPON;
                case "FADN" : return FEDERAL_AGENCY_DISCOUNT_NOTE;
                case "FOR" : return FOREIGN_EXCHANGE_CONTRACT;
                case "FORWARD" : return FORWARD;
                case "FUT" : return FUTURE;
                case "GO" : return GENERAL_OBLIGATION_BONDS;
                case "IET" : return IOETTE_MORTGAGE;
                case "LOFC" : return LETTER_OF_CREDIT;
                case "LQN" : return LIQUIDITY_NOTE;
                case "MATURED" : return MATURED;
                case "MBS" : return MORTGAGE_BACKED_SECURITIES;
                case "MF" : return MUTUAL_FUND;
                case "MIO" : return MORTGAGE_INTEREST_ONLY;
                case "MLEG" : return MULTI_LEG_INSTRUMENT;
                case "MPO" : return MORTGAGE_PRINCIPAL_ONLY;
                case "MPP" : return MORTGAGE_PRIVATE_PLACEMENT;
                case "MPT" : return MISCELLANEOUS_PASS_THROUGH;
                case "MT" : return MANDATORY_TENDER;
                case "MTN" : return MEDIUM_TERM_NOTES;
                case "NONE" : return NO_SECURITY_TYPE;
                case "ONITE" : return OVERNIGHT;
                case "OPT" : return OPTION;
                case "PEF" : return PRIVATE_EXPORT_FUNDING;
                case "PFAND" : return PFANDBRIEFE;
                case "PN" : return PROMISSORY_NOTE;
                case "PS" : return PREFERRED_STOCK;
                case "PZFJ" : return PLAZOS_FIJOS;
                case "RAN" : return REVENUE_ANTICIPATION_NOTE;
                case "REPLACD" : return REPLACED;
                case "REPO" : return REPURCHASE;
                case "RETIRED" : return RETIRED;
                case "REV" : return REVENUE_BONDS;
                case "RVLV" : return REVOLVER_LOAN;
                case "RVLVTRM" : return REVOLVER_TERM_LOAN;
                case "SECLOAN" : return SECURITIES_LOAN;
                case "SECPLEDGE" : return SECURITIES_PLEDGE;
                case "SPCLA" : return SPECIAL_ASSESSMENT;
                case "SPCLO" : return SPECIAL_OBLIGATION;
                case "SPCLT" : return SPECIAL_TAX;
                case "STN" : return SHORT_TERM_LOAN_NOTE;
                case "STRUCT" : return STRUCTURED_NOTES;
                case "SUPRA" : return USD_SUPRANATIONAL_COUPONS;
                case "SWING" : return SWING_LINE_FACILITY;
                case "TAN" : return TAX_ANTICIPATION_NOTE;
                case "TAXA" : return TAX_ALLOCATION;
                case "TBA" : return TO_BE_ANNOUNCED;
                case "TBILL" : return US_TREASURY_BILL;
                case "TBOND" : return US_TREASURY_BOND;
                case "TCAL" : return PRINCIPAL_STRIP_OF_A_CALLABLE_BOND_OR_NOTE;
                case "TD" : return TIME_DEPOSIT;
                case "TECP" : return TAX_EXEMPT_COMMERCIAL_PAPER;
                case "TERM" : return TERM_LOAN;
                case "TINT" : return INTEREST_STRIP_FROM_ANY_BOND_OR_NOTE;
                case "TIPS" : return TREASURY_INFLATION_PROTECTED_SECURITIES;
                case "TNOTE" : return US_TREASURY_NOTE;
                case "TPRN" : return PRINCIPAL_STRIP_FROM_A_NON_CALLABLE_BOND_OR_NOTE;
                case "TRAN" : return TAX_AND_REVENUE_ANTICIPATION_NOTE;
                case "VRDN" : return VARIABLE_RATE_DEMAND_NOTE;
                case "WAR" : return WARRANT;
                case "WITHDRN" : return WITHDRAWN;
                case "XCN" : return EXTENDED_COMM_NOTE;
                case "XLINKD" : return INDEXED_LINKED;
                case "YANK" : return YANKEE_CORPORATE_BOND;
                case "YCD" : return YANKEE_CERTIFICATE_OF_DEPOSIT;
                default: return null;
            }
        }
    }

    private static boolean match (String s) {
        switch (s) {
            case "?":
            case "ABS":
            case "AMENDED":
            case "AN":
            case "BA":
            case "BN":
            case "BOX":
            case "BRADY":
            case "BRIDGE":
            case "BUYSELL":
            case "CB":
            case "CD":
            case "CL":
            case "CMBS":
            case "CMO":
            case "COFO":
            case "COFP":
            case "CORP":
            case "CP":
            case "CPP":
            case "CS":
            case "DEFLTED":
            case "DINP":
            case "DN":
            case "DUAL":
            case "EUCD":
            case "EUCORP":
            case "EUCP":
            case "EUSOV":
            case "EUSUPRA":
            case "FAC":
            case "FADN":
            case "FOR":
            case "FORWARD":
            case "FUT":
            case "GO":
            case "IET":
            case "LOFC":
            case "LQN":
            case "MATURED":
            case "MBS":
            case "MF":
            case "MIO":
            case "MLEG":
            case "MPO":
            case "MPP":
            case "MPT":
            case "MT":
            case "MTN":
            case "NONE":
            case "ONITE":
            case "OPT":
            case "PEF":
            case "PFAND":
            case "PN":
            case "PS":
            case "PZFJ":
            case "RAN":
            case "REPLACD":
            case "REPO":
            case "RETIRED":
            case "REV":
            case "RVLV":
            case "RVLVTRM":
            case "SECLOAN":
            case "SECPLEDGE":
            case "SPCLA":
            case "SPCLO":
            case "SPCLT":
            case "STN":
            case "STRUCT":
            case "SUPRA":
            case "SWING":
            case "TAN":
            case "TAXA":
            case "TBA":
            case "TBILL":
            case "TBOND":
            case "TCAL":
            case "TD":
            case "TECP":
            case "TERM":
            case "TINT":
            case "TIPS":
            case "TNOTE":
            case "TPRN":
            case "TRAN":
            case "VRDN":
            case "WAR":
            case "WITHDRN":
            case "XCN":
            case "XLINKD":
            case "YANK":
            case "YCD":  return true;
            default:    return false;
        }
    }

    @Test
    public void testPerformanceMediumSet() {
        String [] words = {
            "?",
            "ABS",
            "AMENDED",
            "AN",
            "BA",
            "BN",
            "BOX",
            "BRADY",
            "BRIDGE",
            "BUYSELL",
            "CB",
            "CD",
            "CL",
            "CMBS",
            "CMO",
            "COFO",
            "COFP",
            "CORP",
            "CP",
            "CPP",
            "CS",
            "DEFLTED",
            "DINP",
            "DN",
            "DUAL",
            "EUCD",
            "EUCORP",
            "EUCP",
            "EUSOV",
            "EUSUPRA",
            "FAC",
            "FADN",
            "FOR",
            "FORWARD",
            "FUT",
            "GO",
            "IET",
            "LOFC",
            "LQN",
            "MATURED",
            "MBS",
            "MF",
            "MIO",
            "MLEG",
            "MPO",
            "MPP",
            "MPT",
            "MT",
            "MTN",
            "NONE",
            "ONITE",
            "OPT",
            "PEF",
            "PFAND",
            "PN",
            "PS",
            "PZFJ",
            "RAN",
            "REPLACD",
            "REPO",
            "RETIRED",
            "REV",
            "RVLV",
            "RVLVTRM",
            "SECLOAN",
            "SECPLEDGE",
            "SPCLA",
            "SPCLO",
            "SPCLT",
            "STN",
            "STRUCT",
            "SUPRA",
            "SWING",
            "TAN",
            "TAXA",
            "TBA",
            "TBILL",
            "TBOND",
            "TCAL",
            "TD",
            "TECP",
            "TERM",
            "TINT",
            "TIPS",
            "TNOTE",
            "TPRN",
            "TRAN",
            "VRDN",
            "WAR",
            "WITHDRN",
            "XCN",
            "XLINKD",
            "YANK",
            "YCD"
        };

        performanceTest(words);
    }

    private void performanceTest(String[] words) {
        wm = new WordMatcherBuilder();
        for (String word : words)
            wm.add (word);
        WordMatcher matcher = wm.compile32();

        Map<String,String> hashSet = new HashMap<>();
        for (String word : words)
            hashSet.put(word, word);

        final int WARM_UP_COUNT = 25000, TEST_COUNT = 5000000;

        final Random rnd = new Random(1231312131L);

        final int NUMBER_OF_WORDS = words.length;
        final int NUMBER_OF_RUNS =  WARM_UP_COUNT + TEST_COUNT;
        final int [] indices = new int [NUMBER_OF_RUNS];
        for (int i=0; i < NUMBER_OF_RUNS; i++)
            indices[i] = rnd.nextInt(NUMBER_OF_WORDS);

        int i = 0, numMatches = 0;

        for (int j = 0; j < 1000; j++)
            if (System.currentTimeMillis() < 0)
                System.err.println("Strange...");

        // Test HashSet
        // warmup
        while(i < WARM_UP_COUNT) {
            if (hashSet.containsKey(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }

        // test
        final long hashStart = System.currentTimeMillis();
        while(i < TEST_COUNT) {
            if (hashSet.containsKey(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }
        final long hashTime = System.currentTimeMillis() - hashStart;


        i = 0;

        // Test Matcher
        // warmup
        while(i < WARM_UP_COUNT) {
            if (matcher.matches(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }

        // test
        final long matcherStart = System.currentTimeMillis();
        while(i < TEST_COUNT) {
            if (matcher.matches(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }
        final long matcherTime = System.currentTimeMillis() - matcherStart;

        i = 0;

        // Test StringSwitch

        // warmup
        while(i < WARM_UP_COUNT) {
            if (match(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }

        // test
        final long stringSwitchStart = System.currentTimeMillis();
        while(i < TEST_COUNT) {
            if (match(words[indices[i++]]))
                numMatches++; // just to trick optimizer
        }
        final long stringSwitchTime = System.currentTimeMillis() - stringSwitchStart;


        System.out.println ("Number of words: " + NUMBER_OF_WORDS + ", StringSwitch stringSwitchTime: " + stringSwitchTime + ", Matcher time: " + matcherTime + "ms, HashSet time: " + hashTime + "ms, matcher/hash ratio: " + (1.0*matcherTime/hashTime) + ". numMatches:"+numMatches);
    }
}
