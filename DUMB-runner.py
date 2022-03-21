#!/usr/bin/env python3.8

import sys

from antlr4 import FileStream, CommonTokenStream, ParseTreeWalker

from DUMBLexer import DUMBLexer
from DUMBParser import DUMBParser
from DUMBListener import DUMBListener

def p(s):
    print(s, end="")

class MyDUMBListener(DUMBListener):
    def __init__(self, out_stream, indent='  '):
        self.__out = out_stream
        self.__indent = indent
        self.__indent_level = 0

    def __write(self, s):
        self.__out.write(s)

    def __line(self, s):
        self.__write('\n')
        pfix = self.__indent * self.__indent_level
        self.__write(pfix)
        self.__write(s)

    def enterSingleRule(self, ctx):
        print('')

    def exitSingleRule(self, ctx):
        print('')

    def enterIfthen(self, ctx):
        p("(" + ctx.getChild(0).getText() + "")

    def exitIfthen(self,ctx):
        p(")")

    def exitComment(self, ctx):
        p("\n")

    def enterComment(self, ctx):
        p("  ;;" + ctx.getText().rstrip())

    def enterStatement(self, ctx):
        p("\n  ")

    def enterAtom(self, ctx):
        p(" " + ctx.getText())

    def enterString(self, ctx):
        p(" " + ctx.getText())

    def logicalExprAndOr(self, ctx):
        p("({} ".format(ctx.getChild(1).getText()))

    def exitLogical_expr(self, ctx):
        if ctx.getChildCount() == 3:
            p(")")

    def enterLogical_expr(self, ctx):
        if ctx.getChildCount() == 3:
            p(" ")
            self.logicalExprAndOr(ctx)

    def exitComparison_expr(self, ctx):
        p(")")

    def enterComparison_expr(self, ctx):
        p(" (" + ctx.getChild(1).getText() + " ")

    def enterCall(self, ctx):
        p(" (")

    def exitCall(self, ctx):
        p(")")

    def enterMcall(self, ctx):
        p("(.")

    def exitMcall(self, ctx):
        p(")")

    def enterProp_acc(self, ctx):
        p("(.")

    def exitProp_acc(self, ctx):
        p(")")

    def exitMethod(self, ctx):
        p(")")

    def enterMethod(self, ctx):
        p("\n  (defmethod")

    def exitClass_def(self, ctx):
        p(")")

    def enterClass_def(self, ctx):
        p("(defclass")

    def enterArglist(self, ctx):
        p(" " + ctx.getText())

def main(argv):
    inp = FileStream(argv[1])
    lexer = DUMBLexer(inp)
    stream = CommonTokenStream(lexer)
    parser = DUMBParser(stream)
    # Parse the input starting at the "per" rule.
    tree = parser.startRule()

    #listener = DUMBListener()
    listener = MyDUMBListener('/dev/stdout')
    walker = ParseTreeWalker()
    walker.walk(listener, tree)

if __name__ == "__main__":
    print("begin\n")
    main(sys.argv)
    print("\n")
