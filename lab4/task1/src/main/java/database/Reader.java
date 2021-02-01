package database;
import database.instructions.ReaderInstruction;
import database.lock.CustomReadWriteLock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Reader implements Runnable {
    private ReaderInstruction instruction;
    private String keyForSearch;
    private String file;
    private CustomReadWriteLock lock;
    private volatile String result = null;


    public Reader(String fileName, CustomReadWriteLock lock) {
        this.file = fileName;
        this.lock = lock;
    }

    public String doInstruction(ReaderInstruction instruction, String keyForSearch) throws InterruptedException {
        result = null;
        this.instruction = instruction;
        this.keyForSearch = keyForSearch;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
        return result;
    }

    private boolean checkInstructionForRecord(String name, String number) {
        switch (instruction) {
            case FIND_NAME_BY_NUMBER: {
                if (number.equals(keyForSearch)) {
                    result = name;
                    return true;
                }
                break;
            }
            case FIND_NUMBER_BY_NAME: {
                if (name.equals(keyForSearch)) {
                    result = number;
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            lock.readLock();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String record;
            while ((record = reader.readLine()) != null) {
                int separatingSignIndex = record.indexOf('-');
                String name = record.substring(0, separatingSignIndex);
                String number = record.substring(separatingSignIndex + 1);
                if (checkInstructionForRecord(name, number)) {
                    lock.readUnlock();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readUnlock();
        }
    }
}
