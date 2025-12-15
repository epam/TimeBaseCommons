grammar SyntheticRule;

SYMBOL          : [a-zA-Z_][a-zA-Z0-9_]*;
SYMBOL_FROM_NUM : [0-9]+[a-zA-Z_]+[a-zA-Z0-9_]*;
QUOTED_SYMBOL   : '"'~('\r'|'\n'|'"')+'"';
INT             : [0-9]+;
FLOAT           : [0-9]+'.'[0-9]+;
WHITESPACE      : [ \t\r]+ -> skip;

PLUS            : '+';
MINUS           : '-';
MULTIPLICATION  : '*';

input :
    syntheticRule EOF
    ;

syntheticRule :
    syntheticRule PLUS leg              # PlusSyntheticRule
    | syntheticRule MINUS leg           # MinusSyntheticRule
    | leg                               # LegSyntheticRule
    ;

leg :
    unaryRatio MULTIPLICATION symbol    # LegLeft
    | unarySymbol                       # LegUnarySymbol
    ;

unarySymbol :
    MINUS unarySymbol                   # UnarySymbolMinus
    | PLUS unarySymbol                  # UnarySymbolPlus
    | symbol                            # ToSymbol
    ;

unaryRatio :
    MINUS unaryRatio                    # UnaryRatioMinus
    | PLUS unaryRatio                   # UnaryRatioPlus
    | ratio                             # ToRatio
    ;

symbol :
    SYMBOL
    | QUOTED_SYMBOL
    | SYMBOL_FROM_NUM
    ;

ratio :
    INT
    | FLOAT
    ;
