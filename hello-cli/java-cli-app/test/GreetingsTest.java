import airhacks.greeting.control.Greetings;

void main() {
    var message = Greetings.friendlyMessage();

    // returns a non-blank message
    if (message == null || message.isBlank())
        throw new AssertionError("expected a non-blank message but got: " + message);

    // is friendly — mentions the reader
    if (!message.toLowerCase().contains("friend"))
        throw new AssertionError("expected a friendly message mentioning 'friend' but got: " + message);

    // is stable across calls
    var again = Greetings.friendlyMessage();
    if (!message.equals(again))
        throw new AssertionError("expected stable message but got '%s' then '%s'".formatted(message, again));
}
