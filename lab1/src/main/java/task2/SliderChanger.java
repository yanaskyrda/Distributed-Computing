package task2;

public class SliderChanger extends Thread {
    private boolean stop = false;
    private int setValue;

    SliderChanger(int setValue) {
        this.setValue = setValue;
    }

    @Override
    public void run() {
        Application.stopSettingButtons.get(setValue).setEnabled(true);

        Application.semaphore = 1;
        while (!stop) {
            synchronized (Application.slider) {
                Application.slider.setValue(setValue);
            }
        }
        Application.semaphore = 0;
        Thread.currentThread().interrupt();
    }

    void release() {
        Application.stopSettingButtons.get(setValue).setEnabled(false);
        Application.statusLabel.setText("");
        stop = true;
    }
}
