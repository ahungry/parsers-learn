// https://ivanyu.me/blog/2014/09/13/creating-a-simple-parser-with-antlr/
grammar DUMB;

// Parser
startRule  : (singleRule | NL | WS)* EOF ;

singleRule
  : ifthen
  | method
  | comment
  ;

ifthen : IF condition THEN statement ENDIF ;
method : VISIBILITY SUB atom arglist NL+ statement+ ENDSUB ;
atom : IDENTIFIER ;

condition : logical_expr ;
statement : (atom | mcall | call | prop_acc | call) (NL | comment)+ ;

comment: COMMENT ;

logical_expr
  : logical_expr AND logical_expr
  | logical_expr OR logical_expr
  | comparison_expr
  | LPAREN logical_expr RPAREN
  | logical_entity
  ;

comparison_expr
  : comparison_operand comp_operator comparison_operand
  | logical_entity comp_operator logical_entity
  | LPAREN comparison_expr RPAREN
  ;

comparison_operand
  : arithmetic_expr ;

comp_operator
  : EQ ;

arithmetic_expr
  : arithmetic_expr OP arithmetic_expr
  | LPAREN arithmetic_expr RPAREN
  | numeric_entity
  ;

logical_entity
  : (TRUE | FALSE)
  | mcall
  | call
  | prop_acc
  | string
  | atom
  ;

numeric_entity
  : DECIMAL
  | IDENTIFIER
  ;

args      : atom ','? ;
arglist   : LPAREN args* RPAREN ;
mcall     : atom DOT atom WS* LPAREN WS* value WS* RPAREN ;
prop_acc  : atom DOT atom ;
string    : STRING ;
value     : string | IDENTIFIER ;
call      : atom LPAREN value RPAREN ;

// Lexer
CLASS      : 'class' ;
VISIBILITY : 'public' | 'protected' | 'private' ;
SUB        : 'sub' ;
ENDSUB     : 'end sub' ;
TRUE       : 'true' ;
FALSE      : 'false' ;
IF         : 'if' ;
THEN       : 'then' ;
AND        : 'and' ;
OR         : 'or' ;
OP         : '*' | '+ ' | '/' | '-' ;
EQ         : '=' ;
COMMENT    : '\'' .+? (NL | EOF) ;
NL         : ('\r\n' | '\n' | EOF) ;
WS         : [ \t\u000C]+ -> skip ;
DECIMAL    : '-'?[0-9]+('.'[0-9]+)? ;
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]* ;
LPAREN     : '(' ;
RPAREN     : ')' ;
SEMI       : ';' ;
ENDIF      : 'end if' ;
DOT        : '.' ;
STRING     : '"' ~["]* '"' ;
