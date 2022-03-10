package calculator;

import calculator.exceptions.InvalidExpressionException;

public class CalculatorImpl implements Calculator {
    private final FormulaParser parser = new FormulaParserImpl();

    @Override
    public int calculate(String expression) throws InvalidExpressionException {
        try {
            return parser.parse(expression).solve();
        }
        catch (Exception e) {
            throw new InvalidExpressionException(e);
        }
    }
}
