package org.example;

import org.nd4j.linalg.api.ndarray.INDArray;
import java.util.*;

public class ReplayBuffer {
    static class Transition {
        public INDArray getState() {
            return state;
        }

        public void setState(INDArray state) {
            this.state = state;
        }

        public INDArray getNextState() {
            return nextState;
        }

        public void setNextState(INDArray nextState) {
            this.nextState = nextState;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public double getReward() {
            return reward;
        }

        public void setReward(double reward) {
            this.reward = reward;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        INDArray state, nextState;
        int action;
        double reward;
        boolean done;
        public Transition(INDArray s, int a, double r, INDArray s2, boolean d) {
            this.state = s; this.action = a; this.reward = r; this.nextState = s2; this.done = d;
        }
    }
    private final Deque<Transition> buffer;
    private final int capacity;
    public ReplayBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new ArrayDeque<>(capacity);
    }
    public void add(INDArray s, int a, double r, INDArray s2, boolean done) {
        if (buffer.size() >= capacity) buffer.pollFirst();
        buffer.offerLast(new Transition(s,a,r,s2,done));
    }
    public List<Transition> sample(int batchSize) {
        List<Transition> list = new ArrayList<>(buffer);
        Collections.shuffle(list);
        return list.subList(0, Math.min(batchSize, list.size()));
    }
    public int size() { return buffer.size(); }
}