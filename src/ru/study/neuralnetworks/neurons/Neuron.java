package ru.study.neuralnetworks.neurons;

import ru.study.neuralnetworks.entity.NeuroInput;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: markdev
 * Date: 9/16/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Neuron {
    public static final byte NEURON_TYPE_FINAL = 2;
    public static final byte NEURON_TYPE_MEDIUM = 3;
    public static final byte NEURON_TYPE_VIRTUAL = 4;

    private final ArrayList<NeuroInput> in = new ArrayList<NeuroInput>();
    protected Double out = null;
    private byte type;
    private final int requiredInputs;
    private String label;

    public void setLabel(String label) {
        this.label = label;
    }

    protected Neuron(byte type, int requiredInputs) {
        this.type = type;
        this.requiredInputs = requiredInputs;
    }

    protected Neuron(byte type, int requiredInputs, String label) {
        this.type = type;
        this.requiredInputs = requiredInputs;
        setLabel(label);
    }

    public Double getOut() {
        return out;
    }

    public boolean hasOut() {
        return out != null;
    }

    public String getLabel() {
        return label;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public boolean addInput(ArrayList<NeuroInput> inputs) {
        in.addAll(inputs);
        return checkInputCount();
    }

    /*
    * @return true если достаточно входов для просчета результата
    * */
    public boolean addInput(NeuroInput p) {
        in.add(p);
        return checkInputCount();
    }

    private boolean checkInputCount() {
        if (in.size() >= requiredInputs) {
            out = processInputs(sumInputs(in));
            in.clear();
            return true;
        }
        return false;
    }

    public byte getType() {
        return type;
    }

    protected abstract double processInputs(double sum);

    private double sumInputs(ArrayList<NeuroInput> inputs) {
        double sum = 0;
        for (NeuroInput input : inputs) {
            System.out.println("sum + "+input.getValue() + "*" + input.getWeight());
            sum = sum + input.getValue() * input.getWeight();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "\nNeuron{label=" + label + "," +
                "out=" + out +
                "}";
    }
}

