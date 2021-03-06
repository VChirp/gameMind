package com.gamemind;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class GameGui {

    private int rows;
    private int columns;
    private GameLogic logic;

    GameGui(GameLogic logic, int ROWS, int COLUMNS) {
        this.logic = logic;
        MyPoint[][] initialStatus = this.logic.getStatus();
        this.rows = ROWS;
        this.columns = COLUMNS;
        showGUI(initialStatus);
    }

    private void showGUI(MyPoint[][] status) {
        JFrame frame = new JFrame("Kill all numbers");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel[][] labels = createLabels(status);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JLabel label = labels[i][j];
                frame.add(label);
            }
        }

        frame.setLayout(new GridLayout(rows, columns));
        frame.pack();
        frame.setVisible(true);
    }

    private JLabel[][] createLabels(MyPoint[][] initialStatus) {
        JLabel[][] labels = new JLabel[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final Point labelPoint = new Point();
                labelPoint.setLocation(i, j);

                MouseAdapter mouseListener = new MyMouseAdapter(labelPoint, labels);

                labels[i][j] = new JLabelPainter(String.valueOf(initialStatus[i][j].getValue()));
                labels[i][j].setForeground(Color.BLACK);
                labels[i][j].setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.gray));
                labels[i][j].setFont(new Font("Times New Roman", Font.PLAIN, 48));
                labels[i][j].setBackground(Color.black);
                labels[i][j].addMouseListener(mouseListener);
            }
        }
        return labels;
    }

    private static class JLabelPainter extends JLabel {
        JLabelPainter(String text) {
            super(text);
        }

        @Override
        public void paintComponent(Graphics g) {
            //draw background
            Color old = g.getColor();
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(old);
            super.paintComponent(g);
        }
    }

    private class MyMouseAdapter extends MouseAdapter {
        private final Point labelPoint;
        private final JLabel[][] labels;

        MyMouseAdapter(Point labelPoint, JLabel[][] labels) {
            this.labelPoint = labelPoint;
            this.labels = labels;
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            System.out.println(labelPoint);
            MyPoint[][] status = logic.userClick(labelPoint.x, labelPoint.y);
            JLabel labelOnClick = (JLabel) event.getComponent();
            labelOnClick.setBackground(Color.green);
            SwingUtilities.invokeLater(() -> {
                try {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < columns; j++) {
                            if (status[i][j].isWasSelected()) {
                                labels[i][j].setBackground(Color.white);
                            } else {
                                labels[i][j].setBackground(Color.black);
                            }
                        }
                    }
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            super.mouseClicked(event);
        }
    }
}
