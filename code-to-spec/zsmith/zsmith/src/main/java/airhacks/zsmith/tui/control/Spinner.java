package airhacks.zsmith.tui.control;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/// Prints progress dots on a virtual thread until closed; meant for try-with-resources
/// around a blocking call. `System.out` is intentional here — dots are live terminal
/// rendering, not log records.
public class Spinner implements AutoCloseable {

    static final char TICK = '.';
    static final Duration INTERVAL = Duration.ofMillis(500);

    AtomicBoolean running = new AtomicBoolean(true);
    Thread thread = Thread.startVirtualThread(this::spin);

    void spin() {
        while (this.running.get()) {
            System.out.print(TICK);
            System.out.flush();
            sleep();
        }
    }

    void sleep() {
        try {
            Thread.sleep(INTERVAL);
        } catch (InterruptedException _) {
            this.running.set(false);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void close() {
        this.running.set(false);
        try {
            this.thread.join();
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
        System.out.println();
    }
}
