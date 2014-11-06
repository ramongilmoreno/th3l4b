grammar Template;


document:
     'unit' Colon ('entity'|'model')
    'names' Colon JavaClass 'as' Identifier
 'filename' Colon Identifier
  'content' Colon
    ;

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
