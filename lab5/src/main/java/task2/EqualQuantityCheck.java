package task2;

import java.util.HashMap;

public class EqualQuantityCheck implements Runnable {

    private final HashMap<Character, Integer>[] lettersAmounts = new HashMap[Application.getStrings().length];

    @Override
    public void run() {
        for (int i = 0; i < Application.getStrings().length; i++) {
            lettersAmounts[i] = new HashMap<>();
            lettersAmounts[i].put('A', 0);
            lettersAmounts[i].put('B', 0);
        }

        for (int i = 0; i < Application.getStrings().length; i++) {
            countLettersInString(Application.getStrings()[i], i);
        }

        if (checkIfDone()) {
            for (int i = 0; i < Application.getThreads().length; i++) {
                Application.getThreads()[i].interrupt();
                System.out.println(Application.getStrings()[i].toString());
            }
        }
    }

    private void countLettersInString(StringBuilder stringBuilder, int index) {
        for (int i = 0; i < stringBuilder.length(); i++) {
            Character currChar = stringBuilder.charAt(i);
            if (!currChar.equals('A') && !currChar.equals('B')) {
                continue;
            }
            lettersAmounts[index].put(currChar,
                    lettersAmounts[index].get(currChar) + 1);
        }
    }

    private boolean checkIfDone() {
        int totalAmountOfA = 0;
        int totalAmountOfB = 0;
        for (HashMap<Character, Integer> lettersAmount : lettersAmounts) {
            totalAmountOfA += lettersAmount.get('A');
            totalAmountOfB += lettersAmount.get('B');
        }

        for (int i = 0; i < lettersAmounts.length; i++) {
            if (totalAmountOfA - lettersAmounts[i].get('A')
                    == totalAmountOfB - lettersAmounts[i].get('B')) {
                System.out.println("Finished successfully, resulted strings: "
                        + getResultedStrings());
                return true;
            }
        }
        return false;
    }

    private String getResultedStrings() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lettersAmounts.length; i++) {
            stringBuilder.append(lettersAmounts[i].get('A') + lettersAmounts[i].get('B')).append(", ");
            //stringBuilder.append(i).append(", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
