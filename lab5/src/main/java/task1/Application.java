package task1;

import java.util.Arrays;
import java.util.Random;

public class Application {

    private static int size = 200;
    static int partsNumber = 4;

    public static void main(String[] args) {
        int partSize = size / partsNumber;
        Turn[] soldier = new Turn[size];

        CustomCyclicBarrier cyclicBarrier = new CustomCyclicBarrier(partsNumber);
        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < size; i++) {
            soldier[i] = Turn.values()[random.nextInt(1)];
        }

        Thread[] threads = new Thread[partsNumber];

        for (int i = 0; i < partsNumber; i++) {
            threads[i] = new Thread(
                    new SoldiersGroup(soldier, i, partSize * i, partSize * (i + 1),
                            cyclicBarrier)
            );
        }

        for (int i = 0; i < partsNumber; i++) {
            threads[i].start();
        }

        for (int i = 0; i < partsNumber; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(Arrays.toString(soldier));
    }
}
