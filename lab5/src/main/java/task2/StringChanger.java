package task2;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class StringChanger implements Runnable {

    private final int stringIndex;
    private final CyclicBarrier cyclicBarrier;

    StringChanger(int stringIndex, CyclicBarrier cyclicBarrier) {
        this.stringIndex = stringIndex;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        Random random = new Random(System.currentTimeMillis());
        while (!Thread.currentThread().isInterrupted()) {
            StringBuilder stringBuilder = Application.getStrings()[stringIndex];
            switch (random.nextInt(4)) {
                case 0:
                    replaceChars(stringBuilder, 'A', 'C');
                    break;
                case 1:
                    replaceChars(stringBuilder, 'C', 'A');
                    break;
                case 2:
                    replaceChars(stringBuilder, 'B', 'D');
                    break;
                case 3:
                    replaceChars(stringBuilder, 'D', 'B');
                    break;
                default:
                    break;
            }
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void replaceChars(StringBuilder stringBuilder, char toReplace, char replacer) {
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == toReplace) {
                stringBuilder.setCharAt(i, replacer);
            }
        }
    }
}
