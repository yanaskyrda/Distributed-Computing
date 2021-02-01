package database;

import database.instructions.WriterInstruction;
import database.lock.CustomReadWriteLock;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Writer implements Runnable {
    private WriterInstruction instruction;
    private String name;
    private String number;
    private File file;
    private CustomReadWriteLock lock;
    private volatile Boolean result = null;


    public Writer(String fileName, CustomReadWriteLock lock) {
        this.file = new File(fileName);
        this.lock = lock;
    }


    public boolean doInstruction(WriterInstruction instruction, String name, String number) throws InterruptedException {
        result = null;
        this.instruction = instruction;
        this.name = name;
        this.number = number;
        Thread thread = new Thread(this);
        thread.start();
        thread.join();
        return result;
    }


    private void addToFile(String name, String number) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write(name + "-" + number + System.getProperty("line.separator"));
        writer.close();
        result = true;
    }

    private void deleteFromFile(String name, String number) throws IOException {
        File tempFile = new File("~tempfile.txt");
        String toDelete = name + "-" + number;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String currRecord;
        while ((currRecord  = reader.readLine()) != null) {
            if (toDelete.equals(currRecord.trim())) {
                result = true;
            } else {
                writer.write(currRecord + System.getProperty("line.separator"));
            }
        }
        writer.close();
        reader.close();
        if (result == null) {
            result = false;
        }

        if (!file.delete()) {
            result = false;
            return;
        }
        if (!tempFile.renameTo(file)) {
            result = false;
        }
    }

    @Override
    public void run() {
        try {
            lock.writeLock();
            switch (instruction) {
                case ADD: {
                    addToFile(name, number);
                    break;
                }
                case DELETE: {
                    deleteFromFile(name, number);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeUnlock();
        }
    }
}
