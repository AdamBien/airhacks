package airhacks;

import airhacks.greeting.control.Greetings;

public interface App {

    static void main(String... args) {
        IO.println(Greetings.friendlyMessage());
    }

}