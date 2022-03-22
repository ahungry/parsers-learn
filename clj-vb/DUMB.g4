// https://ivanyu.me/blog/2014/09/13/creating-a-simple-parser-with-antlr/
grammar DUMB;

// Parser
startRule  : (singleRule | NL )* EOF ;

singleRule
  : ifthen
  | class_def
  | comment
  | dim
  | assignment
  ;

ifthen : IF condition THEN statement+ ENDIF ;
method : VISIBILITY SUB atom arglist NL+ statement+ ENDSUB NL+ ;
atom : IDENTIFIER ;

condition : logical_expr ;
statement : NL* (atom | mcall | call | prop_acc | call) (NL | comment)+ ;

comment: COMMENT ;

class_def : CLASS atom (NL | comment)+ method* ENDCLASS ;

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
  : boolean
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

boolean    : ( TRUE | FALSE ) ;
assignment : atom EQ (atom | numeric_entity | string | logical_entity) ;
dim        : DIM atom (COMMA atom)*;
args       : atom ','? ;
arglist    : LPAREN args* RPAREN ;
mcall      : atom DOT atom LPAREN value RPAREN ;
prop_acc   : atom DOT atom ;
string     : STRING ;
value      : string | IDENTIFIER ;
call       : atom LPAREN value RPAREN ;


// Fragments for case insensitivity
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');

// Lexer
DIM        : D I M ;
CLASS      : C L A S S ;
VISIBILITY : P U B L I C | P R O T E C T E D | P R I V A T E ;
SUB        : S U B ;
ENDSUB     : E N D ' ' S U B;
TRUE       : T R U E ;
FALSE      : F A L S E ;
IF         : I F ;
THEN       : T H E N ;
AND        : A N D ;
OR         : O R ;
ENDIF      : E N D ' ' I F ;
ENDCLASS   : E N D ' ' C L A S S ;
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
COMMA      : ',' ;
DOT        : '.' ;
STRING     : '"' ~["]* '"' ;
