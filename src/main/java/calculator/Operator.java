package calculator;

import calculator.exceptions.InvalidExpressionException;

public class Operator implements Formula, Comparable<Operator> {
    private final OperatorType operatorType;
    private Formula left;
    private Formula right;

    public Operator(OperatorType operatorType) {
        this.operatorType = operatorType;
    }

    public Operator(char operatorType) {
        this(OperatorType.getOperatorType(operatorType));
    }

    public void setLeft(Formula left) {
        this.left = left;
    }

    public void setRight(Formula right) {
        this.right = right;
    }

    public Formula getLeft() {
        return left;
    }

    public Formula getRight() {
        return right;
    }

    @Override
    public int solve() {
        return switch (operatorType) {
            case SUM -> left.solve() + right.solve();
            case DIVIDE -> left.solve() / right.solve();
            case MULTIPLY -> left.solve() * right.solve();
            case DIFFERENCE -> left.solve() - right.solve();
        };
    }

    @Override
    public int compareTo(Operator o) {
        return operatorType.compareTo(o.operatorType);
    }

    public enum OperatorType {
        SUM('+'),
        DIFFERENCE('-'),
        MULTIPLY('*'),
        DIVIDE('/');

        OperatorType(char type) {
        }

        public static OperatorType getOperatorType(char type) throws InvalidExpressionException {
            return switch (type) {
                case '-' -> OperatorType.DIFFERENCE;
                case '+' -> OperatorType.SUM;
                case '*' -> OperatorType.MULTIPLY;
                case '/' -> OperatorType.DIVIDE;

                default -> throw new InvalidExpressionException();
            };
        }
    }

}
