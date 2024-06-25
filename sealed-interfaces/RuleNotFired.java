public record RuleNotFired(String ruleNumber) implements ValidationResult{    
  public String message(){
      return "rule: %s not fired".formatted(ruleNumber);
  }

  @Override
  public boolean isFailure() {
      return false;
  }

  @Override
  public boolean isSuccess() {
      return false;
  }
}