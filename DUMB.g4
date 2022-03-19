grammar DUMB;

/*
* Parser
*/
dumb      : ( function | statement | WS )+ EOF ;
statement : ( call )+ ;
call      : ID+ WS* parens ;
parens    : LP WS* statement* WS* RP ;
braces    : LB WS* statement* WS* RB ;
function  : FN WS+ ID+ WS* parens WS* braces ;

/*
* Lexer
*/
WS : ( ' ' | '\t' | NL ) ;
NL : ( '\n' | '\r\n' ) ;
FN : 'function' ;
LP : '(' ;
RP : ')' ;
LB : '{' ;
RB : '}' ;
ID : [A-Za-z\-,]+ ;
