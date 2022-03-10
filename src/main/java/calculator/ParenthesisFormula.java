package calculator;

public class ParenthesisFormula implements Formula {
    private final Formula root;

    public ParenthesisFormula(Formula innerFormula) {
        this.root = innerFormula;
    }

    @Override
    public int solve() {
        return root.solve();
    }
}
