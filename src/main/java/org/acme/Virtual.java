package org.acme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Virtual {

    void main() {
    }

    private static void log(String s) {
        Objects.requireNonNull(s);
        System.out.printf("%s %s%n", LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")), s);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException _) {}
    }
}
