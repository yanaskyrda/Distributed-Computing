package task2;

import java.util.concurrent.CyclicBarrier;

public class Application {
    private static final StringBuilder[] strings = new StringBuilder[4];
    private static final Thread[] threads = new Thread[4];

    static StringBuilder[] getStrings() {
        return strings;
    }

    static Thread[] getThreads() {
        return threads;
    }

    public static void main(String[] args) {
        strings[0] = new StringBuilder("ABABBCBDBDBDBCB");
        strings[1] = new StringBuilder("ADADADADADADADA");
        strings[2] = new StringBuilder("BCBCBCBCBCBCBCBCB");
        strings[3] = new StringBuilder("ABCDBCBDCBBCDBCABCD");

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new EqualQuantityCheck());

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new StringChanger(i, cyclicBarrier));
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }

}
