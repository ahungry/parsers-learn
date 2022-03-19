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

    def enterLiteral(self, ctx: DUMBParser.LiteralContext):
        return
    def enterIfThenElseStmt(self, ctx: DUMBParser.IfThenElseStmtContext):
        return
    def enterIfBlockStmt(self, ctx):
        p("IF (")
    def exitIfBlockStmt(self, ctx):
        p(")")
    def enterIfConditionStmt(self, ctx):
        p("")
    def exitIfConditionStmt(self, ctx):
        p(") {\n  ")
    def enterICS_S_VariableOrProcedureCall(self, ctx):
        p("")
    def exitICS_S_VariableOrProcedureCall(self, ctx):
        p("")
    def enterAmbiguousIdentifier(self, ctx):
        p(ctx.getText())
    def enterICS_S_MemberCall(self, ctx):
        p("::")
    def exitICS_S_MemberCall(self, ctx):
        p(" ")
    def enterValueStmt(self, ctx):
        p(ctx.getText())

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
    print("begin")
    main(sys.argv)

print(__name__)
