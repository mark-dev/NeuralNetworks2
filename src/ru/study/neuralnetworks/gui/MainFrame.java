package ru.study.neuralnetworks.gui;


import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.apache.commons.collections15.Transformer;
import ru.study.neuralnetworks.entity.HopfieldImage;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.managers.HopfieldNetwork;
import ru.study.neuralnetworks.neurons.HopfieldNeuron;
import ru.study.neuralnetworks.neurons.Neuron;
import ru.study.neuralnetworks.neurons.VirtualNeuron;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Mark
 */
public class MainFrame extends JFrame {
    private static int NETWORK_SIZE = 25;

    private MainFrame() {
        setTitle("hopfield");
        initGUI();
    }

    private void initGUI() {
        int WIDTH = 700;
        int HEIGHT = 500;
        Color lightBlue = new Color(51, 204, 255);   // light blue
        Color lightYellow = new Color(255, 255, 215);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //init
        panelCenter = new JPanel();
        panelTop = new JPanel();
        panelBottom = new JPanel();
        buttonRecognize = new JButton("recognize");
        buttonTeach = new JButton("teach");
        buttonSetRandom = new JButton("rnd");
        buttonRebuildNetwork = new JButton("rebuild");
        arrowLabel = new JLabel("->");
        checkBoxsInput = new CheckBoxPanel(NETWORK_SIZE, true,true);
        checkBoxsOutput = new CheckBoxPanel(NETWORK_SIZE, false,false);
        Font font = arrowLabel.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
        arrowLabel.setFont(boldFont);
        //set bg Color
        panelTop.setBackground(lightBlue);
        panelCenter.setBackground(lightYellow);
        panelBottom.setBackground(lightBlue);
        buttonRecognize.setBackground(lightBlue);
        checkBoxsInput.setForeground(lightBlue);
        checkBoxsOutput.setForeground(lightBlue);
        checkBoxsInput.setBackground(panelBottom.getBackground());
        checkBoxsOutput.setBackground(panelBottom.getBackground());
        //preffered size
        panelTop.setPreferredSize(new Dimension(WIDTH, (int) (0.1 * HEIGHT)));
        panelCenter.setPreferredSize(new Dimension(WIDTH, (int) (0.65 * HEIGHT)));
        panelBottom.setPreferredSize(new Dimension(WIDTH, (int) (0.25 * HEIGHT)));
//        checkBoxsInput.setPreferredSize(new Dimension((int) (WIDTH * 0.3), (int) (0.2 * HEIGHT)));
//        checkBoxsOutput.setPreferredSize(new Dimension((int) (WIDTH * 0.3), (int) (0.2 * HEIGHT)));
        // borders
        panelTop.setBorder(BorderFactory.createEtchedBorder());
        checkBoxsInput.setBorder(BorderFactory.createEtchedBorder());
        checkBoxsOutput.setBorder(checkBoxsInput.getBorder());
        //layouts
        panelTop.setLayout(new FlowLayout());
        panelCenter.setLayout(new BorderLayout());

        // action listeners
        buttonRecognize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonOkActionPerformed();
            }
        });
        buttonSetRandom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buttonRandomActionPerformed();
            }
        });
        buttonTeach.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonTeachActionPerformed();
            }
        });
        buttonRebuildNetwork.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonRebuildActionPerformed();
            }
        });

        /*
         hopfield network
        * */

        createNetwork();
        // add components to panels
        panelBottom.add(buttonTeach);
        panelBottom.add(buttonRecognize);
        panelBottom.add(buttonSetRandom);
        panelBottom.add(checkBoxsInput);
        panelBottom.add(arrowLabel);
        panelBottom.add(checkBoxsOutput);
        panelBottom.add(buttonRebuildNetwork);
        //add panels to frame
        getContentPane().add(panelCenter, BorderLayout.CENTER);
        getContentPane().add(panelTop, BorderLayout.NORTH);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);
        pack();
    }

    private void buttonRebuildActionPerformed() {
        checkBoxsInput.clear();
        checkBoxsOutput.clear();
        createNetwork();
    }

    private void createNetwork() {
        if (sp != null) {
            panelCenter.remove(sp);
        }
        hopfieldNetwork = new HopfieldNetwork(NETWORK_SIZE);
        VisualizationViewer vs = getVIS(hopfieldNetwork.getNetwork());
        sp = new GraphZoomScrollPane(vs);
        panelCenter.add(sp, BorderLayout.CENTER);
    }

    private void buttonTeachActionPerformed() {
        try {
            hopfieldNetwork.saveImg(new HopfieldImage(checkBoxsInput.getMatrix(), "desc"));
        } catch (Exception e) {
            if (e.getMessage().equals("hopfield network full")) {
                JOptionPane.showMessageDialog(this, "The maximum image count is reached, re create network.");
            }
        }
        sp.repaint();
        checkBoxsOutput.clear();
    }

    private void buttonRandomActionPerformed() {
        checkBoxsInput.random();
        checkBoxsOutput.clear();
    }


    private void buttonOkActionPerformed() {
        HopfieldImage recognized = hopfieldNetwork.recognizeImg(new HopfieldImage(checkBoxsInput.getMatrix(), "desc"));
        checkBoxsOutput.fromMatrix(recognized.getImg());
        sp.repaint();
    }


    private VisualizationViewer getVIS(Graph g) {
        VisualizationViewer vv = new VisualizationViewer(new CircleLayout(g), new Dimension(1000, 1000));
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<Neuron, String>() {
            @Override
            public String transform(Neuron n) {
                return "[" + n.getLabel() + ";" + n.getOut() + "]";
            }
        });
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<NeuronEdge, String>() {
            @Override
            public String transform(NeuronEdge neuronEdge) {
                return "" + neuronEdge.getWeight();
            }
        });
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<Neuron, Paint>() {

            @Override
            public Paint transform(Neuron neuron) {
                if (neuron instanceof HopfieldNeuron)
                    return new Color(187, 247, 250);
                else if (neuron instanceof VirtualNeuron)
                    return new Color(240, 160, 65);
                else return Color.RED;
            }
        });
        return vv;
    }

    public static void main(String[] args) throws
            ClassNotFoundException,
            UnsupportedLookAndFeelException,
            InstantiationException,
            IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        }).run();
    }

    //-----------------------
    private JPanel panelTop;
    private JPanel panelBottom;
    private JPanel panelCenter;
    private GraphZoomScrollPane sp;
    private JButton buttonRecognize;
    private JButton buttonSetRandom;
    private JButton buttonTeach;
    private JButton buttonRebuildNetwork;
    private JLabel arrowLabel;
    private CheckBoxPanel checkBoxsInput;
    private CheckBoxPanel checkBoxsOutput;
    private HopfieldNetwork hopfieldNetwork;

}
