package ru.study.neuralnetworks.managers;

import Jama.Matrix;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.neuralnetworks.entity.HopfieldImage;
import ru.study.neuralnetworks.entity.NeuroInput;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.neurons.HopfieldNeuron;
import ru.study.neuralnetworks.neurons.Neuron;
import ru.study.neuralnetworks.neurons.VirtualNeuron;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/21/13
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class HopfieldNetwork {
    private ArrayList<HopfieldImage> knownImages;

    private ArrayList<HopfieldNeuron> workerNeurons;
    private ArrayList<VirtualNeuron> virtualNeurons;
    private ArrayList<NeuronEdge> edges;
    private DirectedSparseGraph<Neuron, NeuronEdge> network;

    public HopfieldNetwork(int neuronCount) {
        knownImages = new ArrayList<HopfieldImage>();
        virtualNeurons = new ArrayList<VirtualNeuron>();
        workerNeurons = new ArrayList<HopfieldNeuron>();
        edges = new ArrayList<NeuronEdge>();
        network = new DirectedSparseGraph<Neuron, NeuronEdge>();
        buildNetwork(neuronCount);
    }

    public DirectedSparseGraph<Neuron, NeuronEdge> getNetwork() {
        return network;
    }

    private void buildNetwork(int neuronCount) {
        for (int i = 0; i < neuronCount; i++) {
            //в virtualNeurons записываются входы
            VirtualNeuron input = new VirtualNeuron(0);
            HopfieldNeuron worker = new HopfieldNeuron(1, "" + i);
            virtualNeurons.add(input);
            workerNeurons.add(worker);
            network.addVertex(input);
            network.addVertex(worker);
            network.addEdge(new NeuronEdge(1), input, worker);
        }
        //Создали входы и нейроны обработчики
        // O->O --->
        // O->O --->
        //Теперь создаем пути пути вес которых будет менятся в процессе работы
        for (Neuron source : workerNeurons) {
            for (Neuron destination : workerNeurons) {
                NeuronEdge edge = new NeuronEdge(0);
                network.addEdge(edge, source, destination);
                edges.add(edge);
            }
        }
    }

    public void saveImg(HopfieldImage img) throws Exception {
        if (hasFreeSpace()) {
            knownImages.add(img);
            //modify edge weight
            Matrix weights = getWeightMatrix();
            Matrix imgMatrix = img.getImg();
            //weight + X^T * X
            Matrix newWeight = weights.plus(imgMatrix.transpose().times(imgMatrix));
            setWeights(newWeight);
        } else {
            throw new Exception("hopfield network full");
        }
    }

    private boolean hasFreeSpace() {
        int networkSize = workerNeurons.size();
        int capability = (int) (networkSize / (2 * Math.log(networkSize)));
        System.out.println("capability="+capability);
        return knownImages.size() < capability;
    }

    public HopfieldImage recognizeImg(HopfieldImage img) {
        Matrix imgMatrix = img.getImg().copy();
        Matrix nextIterMatrix = imgMatrix.copy();
        Matrix weights = getWeightMatrix();

        boolean isStateChanged = true; //хотя-бы один нейрон изменил состояние в процессе

        while (isStateChanged) {
            isStateChanged = false;
            //Перебираем все нейроны, уставливаем им состояние в соответствии с входными нейронами
            for (int i = 0; i < workerNeurons.size(); i++) {
                HopfieldNeuron source = workerNeurons.get(i);
                virtualNeurons.get(i).setOutValue(imgMatrix.get(0, i));
                source.setState(imgMatrix.get(0, i));

                //Формируем список входов для текущего нейрона
                ArrayList<NeuroInput> inputs = new ArrayList<NeuroInput>();
                for (int e = 0; e < workerNeurons.size(); e++) {
                    double weight = weights.get(e, i);
                    NeuroInput input = new NeuroInput(imgMatrix.get(0, e), weight);
                    inputs.add(input);
                }
                /*Передаем в нейрон связи из других нейронов*/
                if (source.addInput(inputs)) {
                    //Проверяет, стоит ли изменить текущему нейрону состояние
                    if (source.getOut() < 0) {
                        double oldValue = imgMatrix.get(0, i);
                        //Эта новые входы, которые будут использоватся на след. итерации
                        nextIterMatrix.set(0, i, invert(oldValue));
                        source.invertState();
                        isStateChanged = true; //нейрон изменил состояние, значит продолжаем
                    }
                } else {
                    throw new IllegalArgumentException("wrong network");
                }
            }
            imgMatrix = nextIterMatrix.copy();
        }
        return new HopfieldImage(imgMatrix, img.getDescription());
    }

    private Matrix getWeightMatrix() {
        int size = workerNeurons.size();
        double[][] matrix = new double[size][size];
        int j = 0, k = 0;

        for (int i = 0; i < edges.size(); i++) {
            matrix[k][j] = edges.get(i).getWeight();
            if ((i + 1) % size == 0) {
                k++;
                j = 0;
            } else {
                j++;
            }
        }
        return new Matrix(matrix);
    }

    private void setWeights(Matrix m) {
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                double weight = i == j ? 0 : m.get(i, j);
                edges.get(m.getColumnDimension() * i + j).setWeight(weight);
            }
        }
    }

    private double invert(double val) {
        return val * -1;
    }

    public static void main(String[] args) throws Exception {
        HopfieldNetwork h = new HopfieldNetwork(3);
        Matrix z = new Matrix(new double[][]{
                {-1, 1, -1}
        });
        Matrix z1 = new Matrix(new double[][]{
                {1, -1, 1}
        });
        Matrix z2 = new Matrix(new double[][]{
                {-1, 1, 1}
        });
        System.out.println("zlen: " + z.getColumnDimension() + "\trows: " + z.getRowDimension());
        h.saveImg(new HopfieldImage(z, "first"));
        h.saveImg(new HopfieldImage(z1, "sec"));
        h.recognizeImg(new HopfieldImage(z2, "test"));
    }
}
