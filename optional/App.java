import java.util.Optional;

interface App {

  static Optional<String> hello() {
    return Optional.empty();
  }

  static void main(String... args) {
    var message = hello()
        .map(String::toUpperCase)
        .orElse("no duke");
    System.out.println(message);
  }

}