package calculator;

import calculator.exceptions.InvalidExpressionException;

import java.util.Deque;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaParserImpl implements FormulaParser {
    private static final Pattern NUMBER_REGEX = Pattern.compile("[0-9]+");

    @Override
    public Formula parse(String text) {
        text = text.replaceAll("\\s+", "");

        Deque<Formula> formulaParts = parseFormulaParts(new StringBuilder(text));

        return composeFormula(formulaParts);
    }

    private Operand parseOperand(StringBuilder text) {
        Matcher matcher = NUMBER_REGEX.matcher(text);
        if (!matcher.find()) {
            throw new InvalidExpressionException();
        }

        String operand = matcher.group();
        int value = Integer.parseInt(operand);

        text.delete(0, operand.length());

        return new Operand(value);
    }

    private Operator parseOperator(StringBuilder text) {
        Operator operator = new Operator(text.charAt(0));
        text.delete(0, 1);

        return operator;
    }

    private Formula parseParenthesis(StringBuilder text) {
        text.delete(0, 1); // Remove '('
        Deque<Formula> formulaParts = parseFormulaParts(text);
        text.delete(0, 1); // Remove ')'

        Formula innerFormula = composeFormula(formulaParts);

        return new ParenthesisFormula(innerFormula);
    }

    private Deque<Formula> parseFormulaParts(StringBuilder text) {
        Deque<Formula> formulaParts = new LinkedList<>();

        while (!text.isEmpty()) {
            if (isNumeric(text.charAt(0))) {
                formulaParts.add(parseOperand(text));
            }
            else if (isOpenParenthesis(text.charAt(0))) {
                formulaParts.add(parseParenthesis(text));
            }
            else if (isCloseParenthesis(text.charAt(0))) {
                return formulaParts;
            }
            else {
                formulaParts.add(parseOperator(text));
            }
        }

        return formulaParts;
    }

    private Formula composeFormula(Deque<Formula> formulaParts) {
        Formula firstOperand = formulaParts.pop();

        if (formulaParts.isEmpty()) {
            // Formula contains only one operand
            return firstOperand;
        }

        // Second part of a formula is always an operator
        // Also the root of the tree is always an operator
        Operator root = (Operator) formulaParts.pop();
        root.setLeft(firstOperand);

        while (!formulaParts.isEmpty()) {
            Formula current = formulaParts.pop();

            if (current instanceof Operator currentOperator) {
                currentOperator.setLeft(root);
                root = currentOperator;
            }
            else {
                root.setRight(current);
            }
        }

        return arrangeTreeOperators(root);
    }

    private Operator arrangeTreeOperators(Operator root) {
        if (root.getRight().getClass() != Operator.class && root.getLeft().getClass() != Operator.class)
            return root;

        if (root.getLeft() instanceof Operator leftOperator) {
            root.setLeft( arrangeTreeOperators(leftOperator) );
        }
        if (root.getRight() instanceof Operator rightOperator) {
            root.setRight( arrangeTreeOperators(rightOperator) );
        }

        if (root.getRight() instanceof Operator right && right.compareTo(root) < 0) {
            root = leftRotateTree(root);
        }
        if (root.getLeft() instanceof Operator left && left.compareTo(root) < 0) {
            root = rightRotateTree(root);
        }

        return root;
    }

    private static Operator leftRotateTree(Operator root) {
        Operator right = (Operator) root.getRight();

        root.setRight(right.getLeft());
        right.setLeft(root);

        return right;
    }

    private static Operator rightRotateTree(Operator root) {
        Operator left = (Operator) root.getLeft();

        root.setLeft(left.getRight());
        left.setRight(root);

        return left;
    }

    private static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isOpenParenthesis(char c) {
        return c == '(';
    }

    private static boolean isCloseParenthesis(char c) {
        return c == ')';
    }

}
