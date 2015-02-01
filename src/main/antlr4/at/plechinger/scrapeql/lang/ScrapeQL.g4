
grammar ScrapeQL;


parse
 : ( statement_list | error )* EOF
 ;

error
 : UNEXPECTED_CHAR 
   { 
     throw new RuntimeException("UNEXPECTED_CHAR=" + $UNEXPECTED_CHAR.text); 
   }
 ;


statement_list
 : ';'* statement ( ';'+ statement )* ';'*
 ;

statement
 : select_single
 | select_every
 | load
 | output
 ;

expr
 : element_name
 | string_name
 | selector_name
 | function_name '(' expr? (',' expr)* ')'
 ;


variable
 : expr (K_AS element_name)?
 ;

variable_list
 : variable ( ',' variable )*
 ;


load
 :K_LOAD document_name
 ;

select_single
 : K_SELECT K_FIRST? variable_list K_FROM from_name
 ;

select_every
 : K_SELECT K_EVERY variable_list K_FROM element_name K_INTO element_name
 ;

output
 : K_OUTPUT element_name
 ;

keyword
 : K_LOAD
 | K_SELECT
 | K_AS
 | K_FROM
 | K_FIRST
 | K_EVERY
 | K_INTO
 | K_OUTPUT
 ;


document_name
 : STRING_LITERAL
 ;

from_name
 : element_name
 ;

function_name
 : element_name
 ;

element_name
 : IDENTIFIER 
 ;

selector_name
 : SELECTOR
 ;

string_name
 : STRING_LITERAL
 ;



SCOL : ';';
DOT : '.';
OPEN_PAR : '(';
CLOSE_PAR : ')';
COMMA : ',';

//keywords

K_LOAD: L O A D;
K_SELECT: S E L E C T;
K_AS: A S;
K_FROM: F R O M;
K_FIRST: F I R S T;
K_EVERY: E V E R Y;
K_INTO: I N T O;
K_OUTPUT:O U T P U T;


IDENTIFIER
 : '`' (~'`' | '``')* '`'
 | [a-zA-Z_] [a-zA-Z_0-9]* // TODO check: needs more chars in set
 ;


SELECTOR
 : '\'' ( ~'\'' | '\'\'' )* '\''
 ;

STRING_LITERAL
 : '"' (~'"' | '""')* '"'
 ;

SINGLE_LINE_COMMENT
 : '--' ~[\r\n]* -> channel(HIDDEN)
 ;

MULTILINE_COMMENT
 : '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
 ;

SPACES
 : [ \u000B\t\r\n] -> channel(HIDDEN)
 ;

UNEXPECTED_CHAR
 : .
 ;

fragment DIGIT : [0-9];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];