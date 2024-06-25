
interface App {

  static void main(String... args) {
    var result = ValidationResult.failure("42","compile java");
    
    var message = switch (result) {
      case Success success -> success.message();
      case Failure failure -> failure.message() + "--" + failure.cause();

      case RuleNotFired notFired
      when notFired.ruleNumber().equals("42") -> "obvious";
      
      case RuleNotFired notFired
      when notFired.ruleNumber().equals("13") -> "problematic";

      case RuleNotFired notFired -> notFired.message();
    };

    System.out.println(message);
  }
}