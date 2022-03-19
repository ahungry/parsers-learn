all:
	antlr4 ./DUMB.g4 && CLASSPATH=$CLASSPATH:./antlr-4.9.3-complete.jar javac *.java && grun DUMB dumb -tree myscript.dumb

get-jar:
	curl -O https://www.antlr.org/download/antlr-4.9.3-complete.jar
