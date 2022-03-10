package calculator;

import calculator.exceptions.InvalidExpressionException;

public interface Calculator {
    int calculate(String expression) throws InvalidExpressionException;
}
