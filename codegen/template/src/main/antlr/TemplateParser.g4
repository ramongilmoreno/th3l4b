parser grammar TemplateParser;

options { tokenVocab=TemplateLexer; }

document:
     Unit Colon (Entity| Model)
    Names Colon (JavaClass Identifier)*
    Filename
    Content Colon Newline Text
;

