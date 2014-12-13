// https://github.com/antlr/grammars-v4/blob/15df18159653f7ae2963cc73bec7d2b8939a9363/c/C.g4 
// https://github.com/antlr/grammars-v4/blob/master/java/Java.g4

grammar Model;

@parser::members {
    protected void setName(com.th3l4b.common.data.named.INamed named,
            String name) throws RecognitionException {
        try {
            named.setName(name);
        } catch (Exception e) {
            throw new org.antlr.v4.runtime.InputMismatchException(this);
        }
    }

   protected void setType(com.th3l4b.srm.model.base.IField field,
            String type) throws RecognitionException {
        try {
            field.setType(type);
        } catch (Exception e) {
            throw new org.antlr.v4.runtime.InputMismatchException(this);
        }
    }

    protected <N extends com.th3l4b.common.data.named.INamed, C extends com.th3l4b.common.data.named.IContainer<? super N>> void add(
            C container, N named) throws RecognitionException {
        try {
            container.add(named);
        } catch (Exception e) {
            throw new org.antlr.v4.runtime.InputMismatchException(this);
        }
    }

    protected java.util.Map<String, String> getProperties(
            com.th3l4b.common.data.propertied.IPropertied propertied)
            throws RecognitionException {
        try {
            return propertied.getProperties();
        } catch (Exception e) {
            throw new org.antlr.v4.runtime.InputMismatchException(this);
        }
    }

    protected String unquote (String input) { return input.substring(1, input.length() - 1); }
}

document[com.th3l4b.srm.model.base.IModel m]:
    model[m]
    entity[m]*
    ;

// http://stackoverflow.com/questions/18110010/rule-variables-in-antlr4
// http://stackoverflow.com/questions/11599943/antlr-pass-parameter-and-semantic-predicate
properties[com.th3l4b.common.data.propertied.IPropertied p]:
    'properties' '{'
    (key = string '=' value = string ';' { getProperties($p).put($key.r, $value.r); })*
    '}'
;

model [com.th3l4b.srm.model.base.IModel m]:
    'model' id = string { setName($m, $id.r); } properties[m]? ';'
    ;

entity [com.th3l4b.srm.model.base.IModel m]
    @init {
        com.th3l4b.srm.model.base.DefaultEntity e = new com.th3l4b.srm.model.base.DefaultEntity();
    }:
    'entity' id = string { setName(e, $id.r); add($m, e); }
    '{' field[e]+ '}'
    properties[e]? ';'
    ;

field [com.th3l4b.srm.model.base.IEntity e]
    @init {
        com.th3l4b.srm.model.base.DefaultField f = new com.th3l4b.srm.model.base.DefaultField();
    }:
    'field' name = string type = string { setName(f, $name.r); setType(f, $type.r); add($e, f); }
    properties[f]? ';'
    ;

string returns [ String r ]: s = StringLiteral { $r = unquote($s.getText()); };

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

LineComment2
    :
        '#' ~[\r\n]*
        -> skip
    ;
