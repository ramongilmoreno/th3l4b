lexer grammar TemplateLexer;

Unit: 'unit';
Entity: 'entity';
Model: 'model';
Names: 'names';
FilenameToken: 'filename';
Content: 'content';

Text: (.*? Dynamic)*?;

Dynamic: Open -> pushMode(DYNAMIC);

Filename:  FilenameToken Colon -> pushMode(TEXTSINGLELINE);


JavaClass: Identifier ('.' Identifier)+;

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
    :
        '#' ~[\r\n]*
        -> skip
    ;

Colon: ':';

Open: '<%';

// https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Lexer+Rules
mode DYNAMIC;

CLOSE: '%>' -> popMode;
    
mode TEXTSINGLELINE;

ENDOFLINE: [\r\n] -> popMode;


