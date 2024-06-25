
public sealed interface ValidationResult permits Success, Failure, RuleNotFired {
    String ruleNumber();

    String message();

    boolean isFailure();

    boolean isSuccess();

    public static ValidationResult success(String rule, String message) {
        return new Success(rule, message);
    }

    public static ValidationResult failure(String rule, String message) {
        return new Failure(rule, message, "too warm");
    }

}
