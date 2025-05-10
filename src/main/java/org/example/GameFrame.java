package org.example;
import javax.swing.*;
import java.awt.*;
import org.nd4j.linalg.api.ndarray.INDArray;

public class GameFrame extends JFrame {
    private static final int N = 10;                                  // múlt körök száma
    private static final int ACTION_SIZE = Move.values().length;       // 5 lépés
    private static final int STATE_SIZE = 2 * N * ACTION_SIZE;         // állapot dimenzió

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private final NamePanel namePanel;
    private final OpponentSelectionPanel opponentPanel;
    private final CountdownPanel countdownPanel;
    private final MoveSelectionPanel movePanel;
    private final ResultPanel resultPanel;

    private String playerName;
    private Opponent currentOpponent;

    // --- DQN komponensek ---
    private final StateHistory history = new StateHistory(N);
    private final DQNAgent agent = new DQNAgent(STATE_SIZE, ACTION_SIZE);

    public GameFrame() {
        setTitle("Kő-Papír-Olló-Gyík-Spock DQN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        namePanel     = new NamePanel(this);
        opponentPanel = new OpponentSelectionPanel(this);
        countdownPanel= new CountdownPanel(this);
        movePanel     = new MoveSelectionPanel(this);
        resultPanel   = new ResultPanel(this);

        cards.add(namePanel,     "NAME");
        cards.add(opponentPanel, "OPPONENT");
        cards.add(countdownPanel,"COUNTDOWN");
        cards.add(movePanel,     "MOVE_SELECTION");
        cards.add(resultPanel,   "RESULT");
        add(cards);

        cardLayout.show(cards, "NAME");
        setVisible(true);
    }

    public void setPlayerName(String name) { this.playerName = name; }
    public String getPlayerName() { return playerName; }
    public Opponent getCurrentOpponent() { return currentOpponent; }

    public void showOpponentSelection() {
        cardLayout.show(cards, "OPPONENT");
    }

    public void startGameWithOpponent(Opponent o) {
        this.currentOpponent = o;
        // új történet minden ellenfélválasztásnál
        history.add(-1, -1); // üres kezdet
        cardLayout.show(cards, "COUNTDOWN");
        countdownPanel.startCountdown();
    }

    public void countdownFinished() {
        cardLayout.show(cards, "MOVE_SELECTION");
    }

    public void onPlayerMoveSelected(Move playerMove) {
        // Állapot előkészítése
        INDArray state = history.toINDArray();
        // AI választás DQN-model alapján
        int aiIdx = agent.selectAction(state);
        Move aiMove = Move.values()[aiIdx];

        // Jutalom kiszámítása
        boolean done = false;
        double reward;
        if (playerMove == aiMove) {
            reward = 0;
        } else if (playerMove.beats(aiMove)) {
            reward = 1;
            done = true;
        } else {
            reward = -1;
            done = true;
        }

        // Frissítjük a történetet
        history.add(playerMove.ordinal(), aiMove.ordinal());
        INDArray nextState = history.toINDArray();

        // Tapasztalat eltárolása és tanítás
        agent.store(state, aiIdx, reward, nextState, done);

        // Eredmény megjelenítése
        resultPanel.showResult(playerMove, aiMove);
        cardLayout.show(cards, "RESULT");
    }

    public void backToOpponentSelection() {
        showOpponentSelection();
    }

    public void showHistory() {
        HistoryPanel.show(this);
    }
}
