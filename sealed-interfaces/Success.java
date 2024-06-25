public record Success(String ruleNumber, String message) implements ValidationResult{

  @Override
  public boolean isFailure() {
      return false;
  }

  @Override
  public boolean isSuccess() {
      return true;
  }
}