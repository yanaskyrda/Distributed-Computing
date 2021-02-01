import database.*;
import database.instructions.ReaderInstruction;
import database.instructions.WriterInstruction;
import database.lock.CustomReadWriteLock;

import java.util.Random;

public class Main {
    private static final String fileName = "database.txt";

    public static void main(String... args) {
        CustomReadWriteLock lock = new CustomReadWriteLock();
        Reader reader = new Reader(fileName, lock);
        Writer writer = new Writer(fileName, lock);

        try {
            System.out.println("Status of add operations: " +
                    writer.doInstruction(WriterInstruction.ADD, "Name" + new Random().nextInt(100),
                    "11" + (new Random().nextInt(8999) + 1000)));
        System.out.println("Name with number 102030: " +
                reader.doInstruction(ReaderInstruction.FIND_NAME_BY_NUMBER, "102030"));
            System.out.println("Number of Stella: " +
                    reader.doInstruction(ReaderInstruction.FIND_NUMBER_BY_NAME, "Stella"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
