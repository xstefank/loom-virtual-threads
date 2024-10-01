import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Virtual {

    void main() throws Exception {
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static void log(String s) {
        Objects.requireNonNull(s);
        System.out.printf("%s %s %s%n", LocalDateTime.now()
            .format(formatter), Thread.currentThread(), s);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException _) {}
    }
}
