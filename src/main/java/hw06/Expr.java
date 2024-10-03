package hw06;

import java.util.Collections;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.regex.Pattern;

record Expr(String text) {
    static ArrayDeque<String> stack;

    int eval() {
        stack = new ArrayDeque<String>();

        for (var cdpt : text.chars().toArray()) {
            var ch = Character.toString(cdpt);

            if (isSpace(ch) || ch.equals("(")) {
                continue;
            }

            if (isOperator(ch)) {
                stack.push(ch);
                continue;
            }

            if (isDigits(ch)) {
                if (isDigits(stack.peek())) {
                    String num = stack.pop();
                    var xx = num + ch;
                    stack.push(xx);
                }
                else {
                    stack.push(ch);
                }
                continue;
            }

            // TODO:
            /*
                                   (2.4.2) To op, assign the popped element
                                           Pop a number from integer stack and assign it op2
                                           Pop another number from integer stack and assign it
                                           to op1
                                           Calculate op1 op op2 and push the result into the integer
                                           stack
                                           Convert into character and push into stack
                                           Go to the step (2.4)
    New line character            (2.5)  Print the result after popping from the stack
                                         STOP
             */
            if (ch.equals(")")) {
                String a2 = stack.pop();
                String op = stack.pop();
                String a1 = stack.pop();

                String result = applyOp(a1, op, a2);
                stack.push(result);
            }
        }

        try {
            return Integer.parseInt(stack.pop());
        }
        catch (NumberFormatException ee) {
            dumpStack();
            throw new RuntimeException("expected number: " + ee.toString());
        }
    }

    static boolean isSpace(String xx) {
        return xx != null && Pattern.matches("^\\s+$", xx);
    }

    static boolean isDigits(String xx) {
        return xx != null && Pattern.matches("^[0-9]+$", xx);
    }

    static boolean isOperator(String xx) {
        String ops = "+-*/";
        return xx != null && ops.contains(xx); 
    }

    static String applyOp(String a1, String op, String a2) {
        //System.out.printf("applyOp(%s %s %s)\n", a1, op, a2);
        try {
            var xx = Integer.parseInt(a1);
            var yy = Integer.parseInt(a2);

            if (op.equals("+")) {
                return Integer.toString(xx + yy);
            }

            if (op.equals("-")) {
                return Integer.toString(xx - yy);
            }

            if (op.equals("*")) {
                return Integer.toString(xx * yy);
            }

            if (op.equals("/")) {
                return Integer.toString(xx / yy);
            }

            dumpStack();
            throw new RuntimeException("bad operator: " + op);
        }
        catch (NumberFormatException ee) {
            dumpStack();
            throw new RuntimeException("bad number: " + ee.toString());
        }
    }

    static void dumpStack() {
        var xs = new ArrayList(stack);
        Collections.reverse(xs);
        for (var xx : xs) {
            System.out.println(xx);
        }
    }
}
