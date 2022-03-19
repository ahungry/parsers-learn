#!/usr/bin/env python3

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
        p("(" + ctx.getChild(0).getText())

    def exitSingleRule(self, ctx):
        p("\n  {})".format(ctx.getChild(3).getText()))

    def enterLogical_entity(self, ctx):
        p(ctx.getText())

    def logicalExprAndOr(self, ctx):
        p("({}".format(ctx.getChild(1).getText()))

    def exitLogical_expr(self, ctx):
        if ctx.getChildCount() == 3:
            p(")")

    def enterLogical_expr(self, ctx):
        p(" ")
        if ctx.getChildCount() == 3:
            self.logicalExprAndOr(ctx)

        # for c in ctx.getChildren():
        #     print(c.getText())

        # print(ctx.getChildCount())
        # print(ctx.getChildren())
        # print(ctx.getText())

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
