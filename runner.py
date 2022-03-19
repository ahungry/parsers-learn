#!/usr/bin/env python3

import sys

from antlr4 import FileStream, CommonTokenStream, ParseTreeWalker

from VBLexer import VBLexer
from VBParser import VBParser
from VBListener import VBListener

def p(s):
    print(s, end="")

class MyVBListener(VBListener):
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

    def enterLiteral(self, ctx: VBParser.LiteralContext):
        return
    def enterIfThenElseStmt(self, ctx: VBParser.IfThenElseStmtContext):
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
    lexer = VBLexer(inp)
    stream = CommonTokenStream(lexer)
    parser = VBParser(stream)
    # Parse the input starting at the "per" rule.
    tree = parser.startRule()

    #listener = VBListener()
    listener = MyVBListener('/dev/stdout')
    walker = ParseTreeWalker()
    walker.walk(listener, tree)

if __name__ == "__main__":
    print("begin")
    main(sys.argv)

print(__name__)
