package task2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Application {
    private ArrayList<Integer> sliderSettingValues = new ArrayList<>(2);

    static JSlider slider;
    private JFrame frame;
    private JPanel panel;

    private HashMap<Integer, Integer> priorities = new HashMap<>();
    private HashMap<Integer, SliderChanger> settingThreads = new HashMap<>();
    private HashMap<Integer, Component> startSettingButtons = new HashMap<>();
    static HashMap<Integer, Component> stopSettingButtons = new HashMap<>();

    static JLabel statusLabel;

    static int semaphore = 0;

    private void initializeFrame(JFrame frame, JPanel panel, String title) {
        frame.add(panel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title);
        frame.pack();
        frame.setVisible(true);
    }

    private SliderChanger createThread(int setValue) {
        SliderChanger setSliderThread = new SliderChanger(setValue);
        setSliderThread.setPriority(priorities.get(setValue));

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

    private ActionListener createStartActionListener(int setValue) {
        return e -> {
            if (Application.semaphore != 0) {
                Application.statusLabel.setText("Taken!");
                return;
            }
            settingThreads.put(setValue, createThread(setValue));
            settingThreads.get(setValue).start();
        };
    }

    private void addAllValuesToPanel(JPanel panel, HashMap<Integer, Component> componentsMap) {
            for (Component component : componentsMap.values()) {
                panel.add(component);
            }
    }

    private ActionListener createStopActionListener(int setValue) {
        return e -> settingThreads.get(setValue).release();
    }

    private void initializePanel(JPanel panel) {
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setLayout(new GridLayout(3, 2, 30, 30));

        slider = createSlider(50);
        statusLabel = new JLabel("");

        for (int value : sliderSettingValues) {
            startSettingButtons.put(value,
                    createButton("Start setting to " + value, createStartActionListener(value)));
            stopSettingButtons.put(value,
                    createButton("Stop setting to " + value, createStopActionListener(value)));
        }

        for (Component button : stopSettingButtons.values()) {
            button.setEnabled(false);
        }

        panel.add(slider);
        panel.add(statusLabel);
        addAllValuesToPanel(panel, startSettingButtons);
        addAllValuesToPanel(panel, stopSettingButtons);
    }
    private Application() {
        frame = new JFrame();
        panel = new JPanel();

        sliderSettingValues.add(10);
        sliderSettingValues.add(90);

        priorities.put(10, Thread.MIN_PRIORITY);
        priorities.put(90, Thread.MAX_PRIORITY);

        initializePanel(panel);
        initializeFrame(frame, panel, "lab 1");
    }

    public static void main(String... args) {
        new Application();
    }
}
