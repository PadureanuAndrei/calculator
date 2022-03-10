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
            if (isNumeric(text)) {
                formulaParts.add(parseOperand(text));
            }
            else if (isOpenParenthesis(text)) {
                formulaParts.add(parseParenthesis(text));
            }
            else if (isCloseParenthesis(text)) {
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

        /*
            Second part of a formula is always an operator
            Also the root of the tree is always an operator
         */
        Operator root = (Operator) formulaParts.pop();
        root.setLeft(firstOperand);

        while (!formulaParts.isEmpty()) {
            Formula current = formulaParts.pop();

            if (current instanceof Operator currentOperator) {
                if (currentOperator.compareTo(root) > 0) {
                    currentOperator.setLeft(root.getRight());
                    root.setRight(currentOperator);
                }
                else {
                    currentOperator.setLeft(root);
                    root = currentOperator;
                }
            }
            else if (root.getRight() == null) {
                root.setRight(current);
            }
            else {
                ((Operator) root.getRight()).setRight(current);
            }
        }

        return root;
    }

    private static boolean isNumeric(StringBuilder text, int index) {
        return text.charAt(index) >= '0' && text.charAt(index) <= '9';
    }

    private static boolean isNumeric(StringBuilder text) {
        return isNumeric(text, 0);
    }

    private static boolean isOpenParenthesis(StringBuilder text) {
        return text.charAt(0) == '(';
    }

    private static boolean isCloseParenthesis(StringBuilder text) {
        return text.charAt(0) == ')';
    }

}