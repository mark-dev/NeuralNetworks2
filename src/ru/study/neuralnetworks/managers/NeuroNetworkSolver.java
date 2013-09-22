package ru.study.neuralnetworks.managers;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import ru.study.neuralnetworks.entity.NeuroInput;
import ru.study.neuralnetworks.entity.NeuronEdge;
import ru.study.neuralnetworks.neurons.Neuron;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NeuroNetworkSolver {
    private final DirectedSparseGraph<Neuron, NeuronEdge> network;

    public NeuroNetworkSolver(DirectedSparseGraph<Neuron, NeuronEdge> network) {
        this.network = network;
    }

    public ArrayList<Neuron> solve() throws Exception {
        ArrayList<Neuron> allVertex = new ArrayList<Neuron>(network.getVertices());
        /*
            Сначала получаем список всех нейронов которые имеют значение
            как правило это виртуальные нейроны которым назначили значение
         */
        LinkedList<Neuron> ready = getReadyNeurons(allVertex);
        /*
            Условие остановки цикла = все нейроны обсчитали свои входы
            Ну или же схема построена не правильно
        */
        while (!ready.isEmpty()) {
            /*
                Это список нейронов которые при получении
                сигнала на этой итерации, обсчитали свой выход
            */
            LinkedList<Neuron> newReady = new LinkedList<Neuron>();
            //Передаем результат готовых нейронов дальше
            for (Neuron source : ready) {
                //Это список всех ребер выходящих из вершины source
                ArrayList<NeuronEdge> outEdges = new ArrayList<NeuronEdge>(network.getOutEdges(source));
                for (NeuronEdge outEdge : outEdges) {
                    //destination - вершина на другом конце ребра
                    Neuron destination = network.getOpposite(source, outEdge);
                    NeuroInput input = new NeuroInput(source, outEdge);
                    //Этот нейрон обсчитал свой выход
                    if (destination.addInput(input)) {
                        newReady.add(destination);
                    }
                }
                //Далее работаем только с теми нейронами, что обсчитали свое значение
                ready = newReady;
            }
        }
        return getFinalVertex(allVertex);
    }

    private ArrayList<Neuron> getFinalVertex(ArrayList<Neuron> allVertex) {
        ArrayList<Neuron> finalVertex = new ArrayList<Neuron>();
        for (Neuron anAllVertex : allVertex) {
            if (anAllVertex.getType() == Neuron.NEURON_TYPE_FINAL) {
                finalVertex.add(anAllVertex);
            }
        }
        return finalVertex;
    }

    //вернет нейроны которые обсчитали все свои входы
    private LinkedList<Neuron> getReadyNeurons(ArrayList<Neuron> neurons) {
        LinkedList<Neuron> ready = new LinkedList<Neuron>();
        for (Neuron neuron : neurons) {
            if (neuron.hasOut()) {
                ready.add(neuron);
            }
        }
        return ready;
    }
}
