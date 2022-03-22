GRAMMAR?=DUMB

all: compile run

compile:
	antlr4 ./$(GRAMMAR).g4 && \
	antlr4 -Dlanguage=Python3 ./$(GRAMMAR).g4 && \
	CLASSPATH=$$CLASSPATH:./antlr-4.9.3-complete.jar javac *.java

run:
	grun $(GRAMMAR) startRule -tree $(GRAMMAR).asp
	python3 $(GRAMMAR)-runner.py $(GRAMMAR).asp

clojure-repl:
	clj -Sdeps '{:deps {clj-antlr/clj-antlr {:mvn/version "0.2.12"}}}' -e "(require ['clj-antlr.core :as 'antlr])" -r

clojure:
	clj -Sdeps '{:deps {clj-antlr/clj-antlr {:mvn/version "0.2.12"}}}' \
	-e "(require ['clj-antlr.core :as 'antlr])" \
	-e '(def dumb (antlr/parser (str (System/getProperty "user.dir") "/DUMB.g4")))' \
	-e '(prn (dumb (slurp "DUMB.asp")))'

get-jar:
	curl -O https://www.antlr.org/download/antlr-4.9.3-complete.jar

.PHONY: all compile run get-jar
