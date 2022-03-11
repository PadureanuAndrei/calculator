package calculator;

import calculator.exceptions.InvalidExpressionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorImplTest {
    private final CalculatorImpl calculator = new CalculatorImpl();

    @Test
    void testSum() {
        int calculated = calculator.calculate("(((1 + 2)))");
        int reference = 1 + 2;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testDiff() {
        int calculated = calculator.calculate("1 - 2");
        int reference = 1 - 2;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testMul() {
        int calculated = calculator.calculate("10 * 234");
        int reference = 10 * 234;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testDivide() {
        int calculated = calculator.calculate("1000 / 24");
        int reference = 1000 / 24;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testParenthesis() {
        int calculated = calculator.calculate("100 + 200 - (100 + 200)");
        int reference = 100 + 200 - (100 + 200);

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void newTest() {
        int calculated = calculator.calculate("3234");
        int reference = 3234;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testComplexFormula() {
        int calculated = calculator.calculate("10 + 2 * 6 + 33 / 3 + 100 + 20 + (00100 + 2 + 3) * 5");
        int reference = 10 + 2 * 6 + 33 / 3 + 100 + 20 + (100 + 2 + 3) * 5;

        Assertions.assertEquals(calculated, reference);
    }

    @Test
    void testException() {
        Assertions.assertThrows(InvalidExpressionException.class, () -> calculator.calculate("10 + 22 + +"));
    }
}
