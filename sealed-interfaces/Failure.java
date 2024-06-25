public record Failure(String ruleNumber, String message,String cause) implements ValidationResult{

  @Override
  public boolean isFailure() {
      return true;
  }

  @Override
  public boolean isSuccess() {
      return false;
  }
}