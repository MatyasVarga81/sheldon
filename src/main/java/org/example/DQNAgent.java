package org.example;

import java.util.List;
import java.util.Random;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class DQNAgent {
    private final MultiLayerNetwork qNetwork;
    private final ReplayBuffer buffer;
    private double epsilon = 1.0;
    private final double epsilonMin = 0.1;
    private final double epsilonDecay = 0.995;
    private final double gamma = 0.99;
    private final int batchSize = 64;
    private final int stateSize;
    private final int actionSize;

    public DQNAgent(int stateSize, int actionSize) {
        this.stateSize = stateSize;
        this.actionSize = actionSize;
        this.buffer = new ReplayBuffer(10000);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)
                .updater(new Adam(1e-3))
                .list()
                .layer(new DenseLayer.Builder().nIn(stateSize).nOut(64).activation(Activation.RELU).build())
                .layer(new DenseLayer.Builder().nIn(64).nOut(64).activation(Activation.RELU).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nIn(64).nOut(actionSize).activation(Activation.IDENTITY).build())
                .build();
        qNetwork = new MultiLayerNetwork(conf);
        qNetwork.init();
    }

    public int selectAction(INDArray state) {
        if (Math.random() < epsilon) {
            return new Random().nextInt(actionSize);
        } else {
            INDArray q = qNetwork.output(state);
            return q.argMax(1).getInt(0);
        }
    }

    public void store(INDArray s, int a, double r, INDArray s2, boolean done) {
        buffer.add(s, a, r, s2, done);
        train();
    }

    private void train() {
        if (buffer.size() < batchSize) return;

        // Transition osztályból már csak a publikus getterek érhetők el
        List<ReplayBuffer.Transition> batch = buffer.sample(batchSize);

        // Állapotok és következő állapotok összeállítása
        INDArray states = Nd4j.vstack(
                batch.stream()
                        .map(t -> t.getState())
                        .toArray(INDArray[]::new)
        );
        INDArray nextStates = Nd4j.vstack(
                batch.stream()
                        .map(t -> t.getNextState())
                        .toArray(INDArray[]::new)
        );

        // Q-értékek predikciója
        INDArray qValues = qNetwork.output(states);
        INDArray qNext   = qNetwork.output(nextStates);

        // Célértékek beállítása
        for (int i = 0; i < batch.size(); i++) {
            ReplayBuffer.Transition t = batch.get(i);
            double maxNext = t.isDone()
                    ? 0.0
                    : qNext.getRow(i).maxNumber().doubleValue();
            double target = t.getReward() + gamma * maxNext;
            // beállítjuk a kimeneti Q-tömbben a megfelelő akció célértékét
            qValues.putScalar(new int[]{i, t.getAction()}, target);
        }

        // Tanítás a frissített célértékekkel
        qNetwork.fit(states, qValues);

        // Epsilon csökkenése
        if (epsilon > epsilonMin) {
            epsilon *= epsilonDecay;
        }
    }
}