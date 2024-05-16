package org.acme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

public class Virtual {

    static ThreadLocal<Expensive> threadLocal = ThreadLocal.withInitial(Expensive::new);

    record Expensive(UUID uuid) {
        Expensive() {
            this(UUID.randomUUID());
        }

        @Override
        public String toString() {
            return "Expensive{" +
                "uuid=" + uuid +
                '}';
        }
    }

    void main() throws Exception {
//        Thread vta = Thread.ofVirtual().start(() -> {
//            log("Thread A start");
//            sleep(100);
//            log("Thread A end");
//        });
//        Thread vtb = Thread.ofVirtual().start(() -> {
//            log("Thread B start");
//            sleep(100);
//            log("Thread B end");
//        });
//
//        vta.join();
//        vtb.join();

        ThreadFactory factory = Thread.ofVirtual().name("geecon-", 0).factory();
        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);

        Future<?> f1 = executor.submit(() -> {
            log(threadLocal.get().toString());
            pinLock();
        });
        Future<?> f2 = executor.submit(() -> {
            log(threadLocal.get().toString());
            log("Thread B start");
            sleep(100);
            log("Thread B end");
        });

        f1.get();
        f2.get();
    }

    static ReentrantLock lock = new ReentrantLock();

    private static void pinLock() {
        lock.lock();
        try {
            log("Thread A start");
            sleep(100);
            log("Thread A end");
        } finally {
            lock.unlock();
        }
    }

    private synchronized static void pin() {
        log("Thread A start");
        sleep(100);
        log("Thread A end");
    }

    private boolean forever() {
        return true;
    }

    private static void log(String s) {
        Objects.requireNonNull(s);
        System.out.printf("%s %s %s%n", LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")), Thread.currentThread(), s);
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException _) {}
    }
}
