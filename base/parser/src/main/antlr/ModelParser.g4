// https://github.com/antlr/grammars-v4/blob/15df18159653f7ae2963cc73bec7d2b8939a9363/c/C.g4 
// https://github.com/antlr/grammars-v4/blob/master/java/Java.g4

grammar ModelParser;

document:
    model
    entity*
    ;

properties:
    'properties' ('{' StringLiteral '=' StringLiteral '}')*
    ;

model:
    'model' Identifier properties? ';'
    ;

entity:
    'entity' Identifier '{' field+ '}' properties? ';'
    ;

field:
    Identifier StringLiteral properties? ';'
    ;

Identifier
    :   Letter LetterOrDigit*
    ;

fragment
Letter
    :   [a-zA-Z$_] 
    ;

fragment
LetterOrDigit
    :   [a-zA-Z0-9$_]
    ;

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    | UnicodeEscape  
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

Whitespace
    :   [ \t]+
        -> skip
    ;

Newline
    :   (   '\r' '\n'?
        |   '\n'
        )
        -> skip
    ;

BlockComment
    :   '/*' .*? '*/'
        -> skip
    ;

LineComment
    :   '//' ~[\r\n]*
        -> skip
    ;
