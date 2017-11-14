/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import jdk.nashorn.internal.parser.TokenType;
import token.token;
import token.tokenType;

/**
 *
 * @author FORSAKEN MYSTERY
 */
public class LexicalAnalyzer {

    boolean flag;
    ArrayList<String> tokens;
    private String content;
    ArrayList<token> token;
    private int length;

    public String getContent() {
        return content;
    }

    public ArrayList<token> getToken() {
        return token;
    }

    public LexicalAnalyzer(String fileName) {
        flag = false;
        tokens = new ArrayList<>();
        token = new ArrayList<>();
        content = getContent.GetContent.get().GetContent(fileName);
        if (content == null) {
            try {
                throw new Exception("file " + fileName + "couldn't open");
            } catch (Exception ex) {
                Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(1);
        }
        length = content.length();
    }

    public void tokenize() {//   -> 
        String word = "";
        int row = 0;
        tokens.clear();
        token.clear();
        int col = 0;
        char next;
        try {
            while (true) {
                next = nextChar();
                word += next;
                row++;
                if (equalRelOp(next)) {
                    switch (next) {
                        case '<': {
                            next = nextChar();
                            if (other(next, '=', '<')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.LT, row, col, "less than"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '=') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.LTE, row, col, "less than equal"));
                                word = "";
                            } else {
                                word += next;
                                next = nextChar();
                                row++;
                                if (other(next, '=')) {
                                    tokens.add(word);
                                    token.add(new token(word, tokenType.leftShift, row, col, "left shift"));
                                    word = "";
                                    retrack();
                                } else {
                                    word += next;
                                    tokens.add(word);
                                    token.add(new token(word, tokenType.shiftLeftAssign, row, col, "left shift assignment"));
                                    word = "";
                                }
                            }
                            break;
                        }
                        case '=': {
                            next = nextChar();
                            if (other(next, '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.assignment, row, col, "assignment"));
                                word = "";
                                retrack();
                                row--;
                            } else {
                                word += next;
                                tokens.add(word);

                                token.add(new token(word, tokenType.equality, row, col, "equal"));
                                word = "";
                            }
                            break;
                        }
                        case '>': {
                            next = nextChar();
                            if (other(next, '=', '>')) {
                                tokens.add(word);

                                token.add(new token(word, tokenType.GT, row, col, "greater than"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '=') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.GTE, row, col, "greater than equal"));
                                word = "";
                            } else {
                                word += next;
                                next = nextChar();
                                row++;
                                if (other(next, '=')) {
                                    tokens.add(word);
                                    token.add(new token(word, tokenType.rightShift, row, col, "right shift"));
                                    word = "";
                                    retrack();
                                } else {
                                    word += next;
                                    tokens.add(word);
                                    token.add(new token(word, tokenType.shiftRightAssign, row, col, "right shift assignment"));
                                    word = "";
                                }
                            }
                            break;
                        }
                    }
                } else if (equalSpace(next)) {
                    word = "";

                    if (next == '\t') {
                        row += 7;
                    } else if (next == '\r') {
                        row = 0;
                        col++;
                    } else if (next == '\n') {
                        row = 0;
                        col++;
                    }
                } else if (equalSpecialCharacter(next)) {
                    switch (next) {
                        case ('~'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.compl, row, col, "complement"));
                            word = "";
                            break;
                        }
                        case ('#'): {
                            next = nextChar();
                            while (equalLetterUnderLine(next)) {
                                word += next;
                                row++;
                                next = nextChar();
                            }
                            if (word.equals("#include") || word.equals("#define") || word.equals("ifndef") || word.equals("if")) {
                                if (word.equals("#include")) {
                                    word += next;
                                    String s = "";
                                    if (next == '<') {
                                        next = nextChar();
                                        row++;
                                        int length = 0;
                                        while (other(next, '/', '\\', ':', '?', '*', '<', '>', '"', '|')) {
                                            word += next;
                                            s += next;
                                            next = nextChar();
                                            length++;
                                        }
                                        word += next;
                                        if (next == '>' && length != 0) {
                                            tokens.add("#include");
                                            token.add(new token("#include", tokenType.include, row, col, "include"));
                                            tokens.add("<");
                                            token.add(new token("<", tokenType.LT, row, col, "less than"));
                                            tokens.add(s);
                                            token.add(new token(s, tokenType.identifier, row, col, "name of file"));
                                            tokens.add(">");
                                            token.add(new token(">", tokenType.GT, row, col, "greater than"));
                                            word = "";
                                        } else {
                                            LexicalError(word, row, col);
                                        }
                                    } else {
                                        LexicalError(word, row, col);
                                    }
                                } else {
                                    tokens.add(word);
                                    if (word.equals("#define")) {
                                        token.add(new token(word, tokenType.define, row, col, "define"));
                                    } else {
                                        token.add(new token(word, tokenType.KeyWord_if_ifndef, row, col, "ifndef if"));
                                    }
                                }
                            } else {
                                LexicalError(word, row, col);
                            }
                            word = "";
                            break;
                        }
                        case ('.'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.dot, row, col, "dot"));
                            word = "";
                            break;
                        }
                        case ('&'): {
                            next = nextChar();
                            if (other(next, '&', '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.bitWiseAnd, row, col, "bit and"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '&') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.logicalAnd, row, col, "logic and"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.andAssign, row, col, "and assign"));
                                word = "";
                            }
                            break;
                        }
                        case (','): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.comma, row, col, "virgule"));
                            word = "";
                            break;
                        }
                        case ('|'): {
                            next = nextChar();
                            if (other(next, '|', '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.bitwiseOr, row, col, "bit or"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '|') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.logicalOr, row, col, "logic or"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.orAssign, row, col, "or assign"));
                                word = "";
                            }
                            break;
                        }
                        case ('^'): {
                            next = nextChar();
                            row++;
                            if (other(next, '=')) {
                                tokens.add(word);
                                retrack();
                                token.add(new token(word, tokenType.xor, row, col, "xor"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.xorAssign, row, col, "xor assign"));
                                word = "";
                            }
                            break;
                        }
                        case (';'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.semiColon, row, col, "semiColon"));
                            word = "";
                            break;
                        }

                        case ('('): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.leftParanthesis, row, col, "left Paranthesis"));
                            word = "";
                            break;
                        }

                        case (')'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.rightParanthesis, row, col, "right Paranthesis"));
                            word = "";
                            break;
                        }

                        case ('['): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.leftBracket, row, col, "left Bracket"));
                            word = "";
                            break;
                        }
                        case (']'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.rightBracket, row, col, "right Bracket"));
                            word = "";
                            break;
                        }

                        case ('{'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.leftAqulad, row, col, "left aqulad"));
                            word = "";
                            break;
                        }

                        case ('}'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.rightAqulad, row, col, "right aqulad"));
                            word = "";
                            break;
                        }
                        case ((char) 39): {// age ' bod
                            next = nextChar();
                            row++;
                            if (next == (char) 39) {
                                LexicalError(word, row, col);
                                word = "";
                            } else {
                                word += next;
                                next = nextChar();
                                row++;
                                word += next;
                                if (next == (char) 39) {
                                    tokens.add(word);
                                    token.add(new token(word, tokenType.qotation, row, col, "qutation and it's character"));
                                    word = "";

                                } else {
                                    LexicalError(word, row, col);//can't recognize '\r'
                                    word = "";
                                }

                            }
                            break;
                        }

                        case (':'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.colon, row, col, "colon"));
                            word = "";
                            break;
                        }

                        case ('?'): {
                            tokens.add(word);
                            token.add(new token(word, tokenType.questionMark, row, col, "question mark"));
                            word = "";
                            break;
                        }
                        case ('"'): {
                            next = nextChar();
                            while (next != '"') {
                                word += next;
                                row++;
                                if (equalSpace(next)) {
                                    if (next == '\r') {
                                        row = 0;
                                        col++;
                                    } else if (next == '\n') {
                                        row = 0;
                                        col++;
                                    } else if (next == '\t') {
                                        row += 7;
                                    }
                                }
                                next = nextChar();
                                if (next == (char) 92) {// age \ bod
                                    word += next;
                                    next = nextChar();
                                    row++;
                                    if (next == 0) {
                                        LexicalError(word, row, col);
                                    }
                                    word += next;
                                    next = nextChar();
                                }
                                if (next == 0) {
                                    LexicalError(word, row, col);
                                }

                            }
                            word += next;
                            char t = '"';
                            String a = "" + t;
                            tokens.add(a);
                            token.add(new token(word, tokenType.doubleQotation, row, col, " double qutation and string"));
                            word = "";
                            break;
                        }
                        case ('!'): {
                            next = nextChar();
                            if (other(next, '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.not, row, col, "not"));
                                word = "";
                                retrack();
                                row--;
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.NE, row, col, "not equal"));
                                word = "";
                            }
                            break;
                        }
                    }
                } else if (equalLetterUnderLine(next)) {
                    next = nextChar();
                    while (equalLetterUnderLine(next) || equalDigit(next)) {
                        word += next;
                        row++;
                        next = nextChar();
                    }
                    retrack();
                    row--;
                    tokens.add(word);
                    if (word.equals("break")) {
                        token.add(new token(word, tokenType.KeyWord_break, row, col, "break"));
                    } else if (word.equals("case")) {
                        token.add(new token(word, tokenType.KeyWord_case, row, col, "case"));
                    } else if (word.equals("char")) {
                        token.add(new token(word, tokenType.KeyWord_char, row, col, "char"));
                    } else if (word.equals("const")) {
                        token.add(new token(word, tokenType.KeyWord_const, row, col, "const"));
                    } else if (word.equals("auto")) {
                        token.add(new token(word, tokenType.KeyWord_auto, row, col, "auto"));
                    } else if (word.equals("continue")) {
                        token.add(new token(word, tokenType.KeyWord_continue, row, col, "continue"));
                    } else if (word.equals("default")) {
                        token.add(new token(word, tokenType.KeyWord_default, row, col, "default"));
                    } else if (word.equals("do")) {
                        token.add(new token(word, tokenType.KeyWord_do, row, col, "do"));
                    } else if (word.equals("double")) {
                        token.add(new token(word, tokenType.KeyWord_double, row, col, "double"));
                    } else if (word.equals("else")) {
                        token.add(new token(word, tokenType.KeyWord_else, row, col, "else"));
                    } else if (word.equals("enum")) {
                        token.add(new token(word, tokenType.KeyWord_enum, row, col, "enum"));
                    } else if (word.equals("extern")) {
                        token.add(new token(word, tokenType.KeyWord_extern, row, col, "extern"));
                    } else if (word.equals("float")) {
                        token.add(new token(word, tokenType.KeyWord_float, row, col, "float"));
                    } else if (word.equals("for")) {
                        token.add(new token(word, tokenType.KeyWord_for, row, col, "for"));
                    } else if (word.equals("goto")) {
                        token.add(new token(word, tokenType.KeyWord_goto, row, col, "go to"));
                    } else if (word.equals("if")) {
                        token.add(new token(word, tokenType.KeyWord_if, row, col, "if"));
                    } else if (word.equals("inline")) {
                        token.add(new token(word, tokenType.KeyWord_inline, row, col, "inline"));
                    } else if (word.equals("int")) {
                        token.add(new token(word, tokenType.KeyWord_int, row, col, "int"));
                    } else if (word.equals("long")) {
                        token.add(new token(word, tokenType.KeyWord_long, row, col, "long"));
                    } else if (word.equals("register")) {
                        token.add(new token(word, tokenType.KeyWord_register, row, col, "register"));
                    } else if (word.equals("restrict")) {
                        token.add(new token(word, tokenType.KeyWord_restrict, row, col, "restrict"));
                    } else if (word.equals("return")) {
                        token.add(new token(word, tokenType.KeyWord_return, row, col, "return"));
                    } else if (word.equals("short")) {
                        token.add(new token(word, tokenType.KeyWord_short, row, col, "short"));
                    } else if (word.equals("signed")) {
                        token.add(new token(word, tokenType.KeyWord_signed, row, col, "signed"));
                    } else if (word.equals("static")) {
                        token.add(new token(word, tokenType.KeyWord_static, row, col, "static"));
                    } else if (word.equals("struct")) {
                        token.add(new token(word, tokenType.KeyWord_struct, row, col, "struct"));
                    } else if (word.equals("switch")) {
                        token.add(new token(word, tokenType.KeyWord_switch, row, col, "switch"));
                    } else if (word.equals("typedef")) {
                        token.add(new token(word, tokenType.KeyWord_typedef, row, col, "typedef"));
                    } else if (word.equals("union")) {
                        token.add(new token(word, tokenType.KeyWord_union, row, col, "union"));
                    } else if (word.equals("unsigned")) {
                        token.add(new token(word, tokenType.KeyWord_unsigned, row, col, "unsigned"));
                    } else if (word.equals("void")) {
                        token.add(new token(word, tokenType.KeyWord_void, row, col, "void"));
                    } else if (word.equals("while")) {
                        token.add(new token(word, tokenType.KeyWord_while, row, col, "while"));
                    } else if (word.equals("xor")) {
                        token.add(new token(word, tokenType.KeyWord_xor, row, col, "xor"));
                    } else if (word.equals("or")) {
                        token.add(new token(word, tokenType.KeyWord_or, row, col, "or"));
                    } else if (word.equals("and")) {
                        token.add(new token(word, tokenType.KeyWord_and, row, col, "and"));
                    } else if (word.equals("not_eq")) {
                        token.add(new token(word, tokenType.KeyWord_not_eq, row, col, "not equal"));
                    } else if (word.equals("not")) {
                        token.add(new token(word, tokenType.KeyWord_not, row, col, "not"));
                    } else if (word.equals("compl")) {
                        token.add(new token(word, tokenType.KeyWord_compl, row, col, "complement"));
                    } else if (word.equals("alignof")) {
                        token.add(new token(word, tokenType.alignofOperand, row, col, "align of"));
                    } else if (word.equals("sizeof")) {
                        token.add(new token(word, tokenType.sizeofOperand, row, col, "size of"));
                    } else {
                        token.add(new token(word, tokenType.identifier, row, col, "id"));
                    }
                    word = "";
                } else if (equalDigit(next)) {

                    if (next == '0') {
                        next = nextChar();
                        word += next;
                        if (next == 'x' || next == 'X') {
                            next = nextChar();
                            if (equalHex(next)) {
                                while (equalHex(next)) {
                                    word += next;
                                    row++;
                                    next = nextChar();
                                }
                            } else {
                                LexicalError(word, row, col);
                            }
                            if (next == '.') {
                                word += next;
                                next = nextChar();
                                row++;
                                if (equalHex(next)) {
                                    while (equalHex(next)) {
                                        word += next;
                                        row++;
                                        next = nextChar();
                                    }
                                    if (next == 'E' || next == 'e') {
                                        next = nextChar();
                                        row++;
                                        if (next == '+' || next == '-') {
                                            word += next;
                                            next = nextChar();
                                        }
                                        if (equalHex(next)) {
                                            word += next;
                                            next = nextChar();
                                            while (equalHex(next)) {
                                                word += next;
                                                row++;
                                                next = nextChar();
                                            }
                                        } else {
                                            LexicalError(word, row, col);
                                        }
                                    } else {
                                        tokens.add(word);
                                        token.add(new token(word, tokenType.hexNumber, row, col, "hex number float"));
                                        word = "";
                                        retrack();
                                        row--;
                                    }
                                } else {
                                    LexicalError(word, row, col);
                                }
                            } else {
                                tokens.add(word);
                                token.add(new token(word, tokenType.hexNumber, row, col, "hex number int"));
                                word = "";
                                retrack();
                                row--;
                            }
                        } else if (next == 'b') {
                            next = nextChar();
                            if (equalbinary(next)) {
                                while (equalbinary(next)) {
                                    word += next;
                                    row++;
                                    next = nextChar();
                                }
                            } else {
                                LexicalError(word, row, col);
                            }
                            if (next == '.') {
                                word += next;
                                next = nextChar();
                                row++;
                                if (equalbinary(next)) {
                                    while (equalbinary(next)) {
                                        word += next;
                                        row++;
                                        next = nextChar();
                                    }
                                    if (next == 'E' || next == 'e') {
                                        next = nextChar();
                                        row++;
                                        if (next == '+' || next == '-') {
                                            word += next;
                                            next = nextChar();
                                        }
                                        if (equalbinary(next)) {
                                            word += next;
                                            next = nextChar();
                                            while (equalbinary(next)) {
                                                word += next;
                                                row++;
                                                next = nextChar();
                                            }
                                        } else {
                                            LexicalError(word, row, col);
                                        }
                                    } else {
                                        tokens.add(word);

                                        token.add(new token(word, tokenType.binaryNumber, row, col, "binary number float"));
                                        word = "";
                                        retrack();
                                        row--;
                                    }
                                } else {
                                    LexicalError(word, row, col);
                                }
                            } else {
                                tokens.add(word);

                                token.add(new token(word, tokenType.binaryNumber, row, col, "binary number int"));
                                word = "";
                                retrack();
                                row--;
                            }
                        } else if (next <= '7' && next >= '0') {
                            next = nextChar();
                            if (equalOctal(next)) {
                                while (equalOctal(next)) {
                                    word += next;
                                    row++;
                                    next = nextChar();
                                }
                            } else {
                                LexicalError(word, row, col);
                            }
                            if (next == '.') {
                                word += next;
                                next = nextChar();
                                row++;
                                if (equalOctal(next)) {
                                    while (equalOctal(next)) {
                                        word += next;
                                        row++;
                                        next = nextChar();
                                    }
                                    if (next == 'E' || next == 'e') {
                                        next = nextChar();
                                        row++;
                                        if (next == '+' || next == '-') {
                                            word += next;
                                            next = nextChar();
                                        }
                                        if (equalOctal(next)) {
                                            word += next;
                                            next = nextChar();
                                            while (equalOctal(next)) {
                                                word += next;
                                                row++;
                                                next = nextChar();
                                            }
                                        } else {
                                            LexicalError(word, row, col);
                                        }
                                    } else {
                                        tokens.add(word);

                                        token.add(new token(word, tokenType.octalNumber, row, col, "octal number float"));
                                        word = "";
                                        retrack();
                                        row--;
                                    }
                                } else {
                                    LexicalError(word, row, col);
                                }
                            } else {
                                tokens.add(word);

                                token.add(new token(word, tokenType.octalNumber, row, col, "octal number int"));
                                word = "";
                                retrack();
                                row--;
                            }
                        }
                    } else {
                        next = nextChar();
                        while (equalDigit(next)) {
                            word += next;
                            row++;
                            next = nextChar();
                        }
                        if (next == '.') {
                            word += next;
                            next = nextChar();
                            row++;
                            if (equalDigit(next)) {
                                while (equalDigit(next)) {
                                    word += next;
                                    row++;
                                    next = nextChar();
                                }
                                if (next == 'E' || next == 'e') {
                                    next = nextChar();
                                    row++;
                                    if (next == '+' || next == '-') {
                                        word += next;
                                        next = nextChar();
                                    }
                                    if (equalDigit(next)) {
                                        word += next;
                                        next = nextChar();
                                        while (equalDigit(next)) {
                                            word += next;
                                            row++;
                                            next = nextChar();
                                        }
                                    } else {
                                        LexicalError(word, row, col);
                                    }
                                } else {
                                    tokens.add(word);

                                    token.add(new token(word, tokenType.decimalNumber, row, col, "decimal number float"));
                                    word = "";
                                    retrack();
                                    row--;
                                }
                            } else {
                                LexicalError(word, row, col);
                            }
                        } else if (equalLetterUnderLine(next)) {
                            LexicalError(word, row, col);
                        } else {
                            tokens.add(word);
                            token.add(new token(word, tokenType.decimalNumber, row, col, "decimal number int"));
                            word = "";
                            retrack();
                            row--;
                        }
                    }
                } else if (equalToOperational(next)) {
                    switch (next) {
                        case ('/'): {
                            next = nextChar();
                            row++;
                            if (other(next, '/', '*', '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.divide, row, col, "divide"));
                                word = "";
                                retrack();
                                row--;
                            } else {
                                word += next;
                                switch (next) {
                                    case ('/'): {
                                        next = nextChar();
                                        row++;
                                        while (next != '\n') {
                                            row++;
                                            word += next;
                                            next = nextChar();
                                        }
                                        word += next;
                                        row = 0;
                                        col++;
                                        break;
                                    }
                                    case ('*'): {
                                        char previous = nextChar();
                                        next = nextChar();
                                        word += previous;
                                        row++;
                                        while (previous != '*' || next != '/') {
                                            row++;
                                            if (next == '\r') {
                                                row = 0;
                                                col++;
                                            } else if (next == '\n') {
                                                row = 0;
                                                col++;
                                            }

                                            word += next;
                                            previous = next;
                                            next = nextChar();
                                        }
                                        word += next;
                                        tokens.add(word);
                                        token.add(new token(word, tokenType.comment, row, col, "comment"));
                                        word = "";
                                        break;
                                    }
                                    case ('='): {
                                        tokens.add(word);
                                        token.add(new token(word, tokenType.divassign, row, col, "divide assign"));
                                        word = "";
                                        break;
                                    }
                                }

                            }
                            break;
                        }
                        case ('*'): {
                            next = nextChar();
                            row++;
                            if (other(next, '=')) {
                                retrack();
                                tokens.add(word);
                                token.add(new token(word, tokenType.multiply, row, col, "multiply"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.multassign, row, col, "multy assign"));
                                word = "";
                            }
                            break;
                        }
                        case ('-'): {
                            next = nextChar();
                            row++;
                            if (other(next, '-', '=', '>')) {
                                tokens.add(word);

                                token.add(new token(word, tokenType.minus, row, col, "minus"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '-') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.minusminus, row, col, "minus minus"));
                                word = "";
                            } else if (next == '=') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.minusassign, row, col, "minus assign"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.memberOfOp, row, col, "member operand"));
                                word = "";
                            }
                            break;
                        }
                        case ('+'): {
                            next = nextChar();
                            row++;
                            if (other(next, '+', '=')) {
                                tokens.add(word);
                                token.add(new token(word, tokenType.plus, row, col, "plus"));
                                word = "";
                                retrack();
                                row--;
                            } else if (next == '+') {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.plusplus, row, col, "plus plus"));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.plusassign, row, col, "plus assign"));
                                word = "";
                            }
                            break;
                        }
                        case ('%'): {
                            next = nextChar();
                            row++;
                            if (other(next, '=')) {
                                retrack();
                                tokens.add(word);
                                token.add(new token(word, tokenType.modoluar, row, col, "modular "));
                                word = "";
                            } else {
                                word += next;
                                tokens.add(word);
                                token.add(new token(word, tokenType.modoluarAssign, row, col, "modular assign"));
                                word = "";
                            }
                            break;
                        }
                    }
                } else if (next == '\0') {
                    word = "";
                    System.out.println("done");
                } else {
                    LexicalError(word, row, col);
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            if (!"".equals(word)) {
                //System.out.println("word = " + word);
                //LexicalError(word, row, col);
            }
            Iterator.get().reset();
        } finally {
            //System.out.println("tokens = " + tokens);
            //System.out.println("token = " + token);
        }
    }

    @Override
    public String toString() {
        return content;
    }

    private char nextChar() {
        if (flag == false) {
            if (Iterator.get().hasNext(length)) {
                return content.charAt(Iterator.get().next());
            }
            flag = true;
            return '\0';
        } else {
            return content.charAt(Iterator.get().next());
        }
    }

    private void retrack() {
        Iterator.get().retrack();
    }

    private boolean equalSpace(char charAt) {
        return (charAt == '\t' || charAt == ' ' || charAt == '\r' || charAt == '\n');
    }

    private boolean equalLetterUnderLine(char charAt) {
        return (charAt <= 'Z' && charAt >= 'A') || (charAt <= 'z' && charAt >= 'a') || charAt == '_' || charAt == '$';
    }

    private boolean equalDigit(char charAt) {
        return ((charAt <= '9' && charAt >= '0'));
    }

    private void LexicalError(String word, int row, int col) {
        try {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have Error!");
            alert.setContentText("you have error\n"
                    + " on line : " + (col / 2 + 1) + " letter : " + row + "  on word : " + word);
            alert.showAndWait();
            throw new Exception("you have error\n"
                    + " on line : " + (col / 2 + 1) + " letter : " + row + "  on word : " + word);
        } catch (Exception ex) {
            Logger.getLogger(LexicalAnalyzer.class.getName()).log(Level.SEVERE, "ERROR!", ex);
        }
        System.exit(1);
    }

    private boolean equalRelOp(char charAt) {
        return (charAt == '>' || charAt == '<' || charAt == '=');
    }

    private boolean other(char charAt, char... Item) {
        boolean forReturn = true;
        for (char item : Item) {
            if (item == charAt) {
                forReturn = false;
                break;
            }
        }
        return forReturn;
    }

    private boolean equalToOperational(char charAt) {
        return (charAt == '/' || charAt == '*' || charAt == '+' || charAt == '-' || charAt == '%');
    }

    private boolean equalSpecialCharacter(char charAt) {
        return ((charAt == '#') || charAt == '!' || charAt == (char) 39 || charAt == '"' || charAt == '&' || charAt == '|' || charAt == '^' || charAt == '.' || charAt == ',' || charAt == ';' || charAt == ':' || charAt == '{' || charAt == '}' || charAt == '[' || charAt == ']' || charAt == '(' || charAt == ')' || charAt == '?' || charAt == '~');
    }

    private boolean equalHex(char next) {
        return (equalDigit(next) || (next <= 'f' && next >= 'a') || (next <= 'F' && next >= 'A'));
    }

    private boolean equalOctal(char next) {
        boolean f = equalDigit(next);
        if (next == '8' || next == '9') {
            f = false;
        }
        return f;
    }

    private boolean equalbinary(char next) {
        return next == '0' || next == '1';
    }

}
