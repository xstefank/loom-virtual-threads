package org.acme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

public class Virtual {

    void main() throws Exception {
//        Thread t1 = Thread.ofVirtual().start(() -> {
//            log("Thread A start");
//            sleep(100);
//            log("Thread A end");
//        });
//
//        Thread t2 = Thread.ofVirtual().start(() -> {
//            log("Thread B start");
//            sleep(100);
//            log("Thread B end");
//        });
//
//        t1.join();
//        t2.join();

        ThreadFactory factory = Thread.ofVirtual().name("devconfcz-", 0).factory();
        ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory);

        Future<?> f1 = executorService.submit(() -> {
            log("Thread A start");
//            sleep(100);
            while (true) {

            }
//            log("Thread A end");
        });

        Future<?> f2 = executorService.submit(() -> {
            log("Thread B start");
            sleep(100);
            log("Thread B end");
        });

        f1.get();
        f2.get();
    }

    private static ReentrantLock lock = new ReentrantLock();
    private  static void extracted() {
        lock.lock();
        try {
            log("Thread A start");
            sleep(100);
            log("Thread A end");
        } finally {
            lock.unlock();
        }
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
