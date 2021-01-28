package task1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Application {
    private ArrayList<Integer> sliderSettingValues = new ArrayList<>(2);

    private JSlider slider;
    private JFrame frame;
    private JPanel panel;
    private JButton start;
    private HashMap<Integer, Component> prioritySpinners = new HashMap<>();
    private HashMap<Integer, Thread> settingThreads = new HashMap<>();
    private HashMap<Integer, Component> settingCountLabels = new HashMap<>();
    private HashMap<Integer, Integer> settingCounts = new HashMap<>();

    private void initializeFrame(JFrame frame, JPanel panel, String title) {
        frame.add(panel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);
        frame.pack();
        frame.setVisible(true);
    }

    private JSpinner createSpinner(int defaultValue) {
        if (defaultValue < 1 || defaultValue > 10) {
            defaultValue = 5;
        }
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, 1, 10, 1));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setEditable(false);
        spinner.addChangeListener(e -> {
            int setValue = -1;
            for (Map.Entry<Integer, Component> entry : prioritySpinners.entrySet()) {
                if (entry.getValue() == e.getSource()) {
                    setValue = entry.getKey();
                    break;
                }
            }
            if (setValue == -1) {
                return;
            }
            settingThreads.get(setValue).setPriority((Integer) ((JSpinner) e.getSource()).getValue());
        });
        return spinner;
    }

    private Thread createThread(int setValue) {
        Thread setSliderThread = new Thread(() -> {
            while (true) {
                synchronized (slider) {
                    slider.setValue(setValue);
                    settingCounts.put(setValue, settingCounts.get(setValue) + 1);
                    ((JLabel) settingCountLabels.get(setValue)).setText("Number of assignments: "
                            + settingCounts.get(setValue));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread.yield();
                }
            }
        });
        setSliderThread.setPriority((Integer) ((JSpinner) prioritySpinners.get(setValue)).getValue());
        return setSliderThread;
    }

    private JButton createButton(String title, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.addActionListener(actionListener);
        return button;
    }

    private JSlider createSlider(int defaultValue) {
        JSlider slider = new JSlider(0, 100);
        if (defaultValue < 0 || defaultValue > 100) {
            slider.setValue(50);
        } else {
            slider.setValue(defaultValue);
        }
        slider.setEnabled(false);
        return slider;
    }

    private void addAllValuesToPanel(JPanel panel, HashMap<Integer, Component> componentsMap) {
        for (Component component : componentsMap.values()) {
            panel.add(component);
        }
    }

    private void initializePanel(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setLayout(new GridLayout(3, 2, 30, 30));

        slider = createSlider(50);
        start = createButton("Start", e -> {
                    for (Thread thread : settingThreads.values()) {
                        thread.start();
                    }
                });
        for (int value : sliderSettingValues) {
            prioritySpinners.put(value, createSpinner(4));
            settingThreads.put(value, createThread(value));
            settingCounts.put(value, 0);
            settingCountLabels.put(value, new JLabel("Number of assignments: 0"));
        }

        panel.add(slider);
        panel.add(start);

        addAllValuesToPanel(panel, prioritySpinners);
        addAllValuesToPanel(panel, settingCountLabels);
    }
    private Application() {
        frame = new JFrame();
        panel = new JPanel();

        sliderSettingValues.add(10);
        sliderSettingValues.add(90);

        initializePanel(panel);

        initializeFrame(frame, panel, "lab 1");
    }

    public static void main(String... args) {
        new Application();
    }
}
