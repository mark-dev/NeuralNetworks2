package ru.study.neuralnetworks.gui;

import Jama.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/22/13
 * Time: 11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckBoxPanel extends JPanel {
    private JCheckBox[] checkers;

    public CheckBoxPanel(int size, boolean rnd, boolean editable) {
        checkers = new JCheckBox[size];
        GridLayout layout = new GridLayout((int)(Math.sqrt(size)),(int) (Math.sqrt(size)), 1, 1);
        setLayout(layout);
        ImageIcon black = getIcon(Color.BLACK);
        ImageIcon white = getIcon(Color.WHITE);
        for (int i = 0; i < size; i++) {
            JCheckBox chBox = new JCheckBox();
            chBox.setIcon(white);
            chBox.setEnabled(editable);
            chBox.setSelectedIcon(black);
            chBox.setDisabledSelectedIcon(black);
            chBox.setDisabledIcon(white);
            chBox.setBorder(BorderFactory.createEmptyBorder());
            if (rnd)
                chBox.setSelected(Math.random() > 0.5);
            add(chBox);
            checkers[i] = chBox;
        }
    }

    public void random() {
        for (JCheckBox checker : checkers) {
            checker.setSelected(Math.random() > 0.5);
        }
    }
    public void clear(){
        for (JCheckBox checker : checkers) {
            checker.setSelected(false);
        }
    }
    public Matrix getMatrix() {
        double[][] inputs = new double[1][checkers.length];
        for (int i = 0; i < checkers.length; i++) {
            inputs[0][i] = checkers[i].isSelected() ? 1 : -1;
        }
        return new Matrix(inputs);
    }

    public void fromMatrix(Matrix m) {
        clear();
        for (int i = 0; i < m.getColumnDimension(); i++) {
            checkers[i].setSelected(m.get(0, i) > 0);
        }

    }
    private ImageIcon getIcon(Color c){
        int W = 20, H = 20;
        BufferedImage bi = new BufferedImage(20,20,BufferedImage.TYPE_INT_RGB);
        for(int i = 0 ; i < W; i ++ ){
            for(int j  = 0 ; j < H; j++){
                bi.setRGB(i,j,c.getRGB());
            }
        }
        return new ImageIcon(bi);
    }
}
