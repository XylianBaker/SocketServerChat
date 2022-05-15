package jkammellander.questions;

public class MathQuestion {

    private final double a = Math.random()*(100-1+1)+1;
    private final double b = Math.random()*(100-1+1)+1;
    private final double operatorPick = Math.random()*(3 +1)+0;

    public String getQuestion() {
        return switch ((int) this.operatorPick) {
            case 0 -> String.valueOf(a + b);
            case 1 -> String.valueOf(a - b);
            case 2 -> String.valueOf(a * b);
            case 3 -> String.valueOf(a / b);
            default -> null;
        };
    }

    public double getSolution() {
        return switch ((int) this.operatorPick) {
            case 0 -> a + b;
            case 1 -> a - b;
            case 2 -> a * b;
            case 3 -> a / b;
            default -> 0;
        };
    }
}
