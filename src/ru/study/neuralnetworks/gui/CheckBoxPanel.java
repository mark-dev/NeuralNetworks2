package ru.study.neuralnetworks.gui;

import Jama.Matrix;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/22/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxPanel extends JPanel {
    private JCheckBox[] checkers;

    public CheckBoxPanel(int size, boolean rnd) {
        checkers = new JCheckBox[size*5];
        GridLayout layout = new GridLayout(size, size, 1, 1);
        setLayout(layout);
        for (int i = 0; i < size * size; i++) {
            JCheckBox chBox = new JCheckBox();
            chBox.setBorder(BorderFactory.createEmptyBorder());
            if (rnd)
                chBox.setSelected(Math.random() > 0.5);
            add(chBox);
            checkers[i] = chBox;
        }
    }

    public void random() {
        for (int i = 0; i < checkers.length; i++) {
            checkers[i].setSelected(Math.random() > 0.5);
        }
    }
    public void clear(){
        for (int i = 0; i < checkers.length; i++) {
            checkers[i].setSelected(false);
        }
    }
    public Matrix getMatrix() {
        double[][] inputs = new double[1][checkers.length];
        for (int i = 0; i < inputs.length; i++) {
            inputs[0][i] = checkers[i].isSelected() ? 1 : -1;
        }
        return new Matrix(inputs);
    }

    public void fromMatrix(Matrix m) {
        for (int i = 0; i < m.getColumnDimension(); i++) {
            checkers[i].setSelected(m.get(0, i) > 0);
        }

    }

    public static void main(String[] args) {
        JFrame fr = new JFrame();
        fr.setLayout(new BorderLayout());
        fr.add(new CheckBoxPanel(5, true), BorderLayout.CENTER);
        fr.setPreferredSize(new Dimension(300, 300));
        fr.pack();
        fr.setVisible(true);
    }
}
