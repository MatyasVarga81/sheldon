package org.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import java.util.*;

public class StateHistory {
    private final int N; // hány kör
    private final Deque<Integer> history = new ArrayDeque<>();
    private final int actionSize = Move.values().length;

    public StateHistory(int N) { this.N = N; }

    public void add(int playerAction, int aiAction) {
        history.addLast(playerAction);
        history.addLast(aiAction);
        while (history.size() > 2 * N) history.removeFirst();
    }

    public INDArray toINDArray() {
        double[] data = new double[2 * N * actionSize];
        int idx = 0;
        List<Integer> list = new ArrayList<>(history);
        for (int i = 0; i < 2 * N; i++) {
            int a = (i < list.size() ? list.get(i) : -1);
            for (int j = 0; j < actionSize; j++) {
                data[idx++] = (a == j ? 1.0 : 0.0);
            }
        }
        return Nd4j.create(data).reshape(1, 2 * N * actionSize);
    }
}