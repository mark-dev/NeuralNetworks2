package ru.study.neuralnetworks.gui;

import Jama.Matrix;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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
        int checkersPerLine =   (int) (Math.sqrt(size));
        GridLayout layout = new GridLayout(checkersPerLine,checkersPerLine, 0, 0);
        setLayout(layout);
        final ImageIcon black = getIcon(Color.BLACK);
        final ImageIcon white = getIcon(Color.WHITE);
        for (int i = 0; i < size; i++) {
            final JCheckBox chBox = new JCheckBox();
            chBox.setIcon(white);
            chBox.setEnabled(editable);
            chBox.setSelectedIcon(black);
            chBox.setDisabledSelectedIcon(black);
            chBox.setRolloverIcon(white);
            chBox.setDisabledIcon(white);
            chBox.setBorder(BorderFactory.createEmptyBorder());
            chBox.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(chBox.isSelected())
                        chBox.setIcon(black);

                    else
                        chBox.setIcon(white);
                }
            });
            if (rnd)
                chBox.setSelected(Math.random() > 0.5);
            add(chBox);
            checkers[i] = chBox;
        }
        Dimension prefCh = checkers[0].getPreferredSize();

        setPreferredSize(new Dimension(prefCh.width * checkersPerLine,prefCh.height * checkersPerLine));
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

    public static void main(String[] args) {
        JFrame fs = new JFrame();
        BorderLayout bl = new BorderLayout();
        bl.setHgap(1);
        bl.setVgap(1);
        fs.setLayout(bl);
        fs.add(new CheckBoxPanel(25,true,true),BorderLayout.CENTER);

        fs.setPreferredSize(new Dimension(300,300));
        fs.pack();
        fs.setVisible(true);
    }
}
