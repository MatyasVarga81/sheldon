package org.example;

import java.util.*;
public class GameLogic {
    private final Opponent opponent;
    private final Random random = new Random();

    private static final Map<String, String> RULES = createRules();
    private static Map<String, String> createRules() {
        Map<String, String> m = new HashMap<>();
        m.put("OLLO_PAPIR", "Az olló elvágja a papírt.");
        m.put("PAPIR_KO", "A papír bevonja a követ.");
        m.put("KO_GYIK", "A kő agyonüti a gyíkot.");
        m.put("GYIK_SPOCK", "A gyík megmarja Spockot.");
        m.put("SPOCK_OLLO", "Spock eltöri az ollót.");
        m.put("OLLO_GYIK", "Az olló lefejezi a gyíkot.");
        m.put("GYIK_PAPIR", "a gyík megeszi a papírt.");
        m.put("PAPIR_SPOCK", "a papír cáfolja Spockot.");
        m.put("SPOCK_KO", "Spock feloldja a követ.");
        m.put("KO_OLLO", "a kő eltöri az ollót.");
        return m;
    }

    public GameLogic(Opponent opponent) {
        this.opponent = opponent;
    }

    public Move aiChoose(Move playerMove) {
        double s = opponent.getStrength();
        Map<Move, Double> weights = new EnumMap<>(Move.class);
        for (Move m : Move.values()) {
            double w = m.beats(playerMove) ? s : 1.0;
            if (m == playerMove) w = 1.0;
            weights.put(m, w);
        }
        double total = weights.values().stream().mapToDouble(Double::doubleValue).sum();
        double r = random.nextDouble() * total;
        double cum = 0;
        for (Map.Entry<Move, Double> e : weights.entrySet()) {
            cum += e.getValue();
            if (r <= cum) return e.getKey();
        }
        return playerMove;
    }

    public String getRuleSentence(Move player, Move ai) {
        if (player == ai) return "";
        String key = player.name() + "_" + ai.name();
        return RULES.getOrDefault(key, RULES.getOrDefault(ai.name() + "_" + player.name(), ""));
    }
}
