GRAMMAR?=DUMB

all: compile run

compile:
	antlr4 ./$(GRAMMAR).g4 && \
	antlr4 -Dlanguage=Python3 ./$(GRAMMAR).g4 && \
	CLASSPATH=$$CLASSPATH:./antlr-4.9.3-complete.jar javac *.java

run:
	grun $(GRAMMAR) startRule -tree $(GRAMMAR).asp
	python3.8 $(GRAMMAR)-runner.py $(GRAMMAR).asp

get-jar:
	curl -O https://www.antlr.org/download/antlr-4.9.3-complete.jar

.PHONY: all compile run get-jar
