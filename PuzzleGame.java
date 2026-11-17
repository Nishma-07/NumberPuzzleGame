
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class PuzzleGame extends JFrame {

    private JButton[] buttons = new JButton[9];
    private int[] board = {1, 2, 3, 4, 5, 6, 7, 8, 0};

    private int moves = 0;
    private int seconds = 0;

    private JLabel moveLabel;
    private JLabel timerLabel;

    private Timer timer;

    public PuzzleGame() {

        setTitle("Number Puzzle");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title and information
        JLabel title = new JLabel("Number Puzzle", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        moveLabel = new JLabel("Moves: 0", JLabel.CENTER);
        timerLabel = new JLabel("Time: 0 sec", JLabel.CENTER);

        JPanel top = new JPanel(new GridLayout(3, 1));
        top.add(title);
        top.add(moveLabel);
        top.add(timerLabel);

        add(top, BorderLayout.NORTH);

        // Puzzle grid
        JPanel grid = new JPanel(new GridLayout(3, 3, 5, 5));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 30));

            final int index = i;
            buttons[i].addActionListener(e -> moveTile(index));

            grid.add(buttons[i]);
        }

        add(grid, BorderLayout.CENTER);

        // Shuffle button
        JButton shuffle = new JButton("Shuffle");
        shuffle.addActionListener(e -> shuffleBoard());

        add(shuffle, BorderLayout.SOUTH);

        // Timer
        timer = new Timer(1000, e -> {
            seconds++;
            timerLabel.setText("Time: " + seconds + " sec");
        });

        shuffleBoard();
        setVisible(true);
    }

    // Find empty space
    private int getEmpty() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // Shuffle puzzle
    private void shuffleBoard() {
        timer.stop();
        seconds = 0;
        moves = 0;

        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            int empty = getEmpty();
            int[] choices = new int[4];
            int count = 0;

            if (empty >= 3) choices[count++] = empty - 3;
            if (empty < 6) choices[count++] = empty + 3;
            if (empty % 3 != 0) choices[count++] = empty - 1;
            if (empty % 3 != 2) choices[count++] = empty + 1;

            int selected = choices[random.nextInt(count)];

            board[empty] = board[selected];
            board[selected] = 0;
        }

        updateBoard();
    }

    // Move tile
    private void moveTile(int index) {
        int empty = getEmpty();

        int row1 = index / 3;
        int col1 = index % 3;
        int row2 = empty / 3;
        int col2 = empty % 3;

        if (Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1) {

            if (!timer.isRunning()) {
                timer.start();
            }

            board[empty] = board[index];
            board[index] = 0;

            moves++;
            updateBoard();

            if (isSolved()) {
                timer.stop();

                JOptionPane.showMessageDialog(this,
                    "Congratulations! Puzzle Solved!\n"
                    + "Moves: " + moves + "\n"
                    + "Time: " + seconds + " seconds");

                shuffleBoard();
            }
        }
    }

    // Check whether puzzle is solved
    private boolean isSolved() {
        for (int i = 0; i < 8; i++) {
            if (board[i] != i + 1) {
                return false;
            }
        }
        return board[8] == 0;
    }

    // Update buttons
    private void updateBoard() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                buttons[i].setText("");
            } else {
                buttons[i].setText(String.valueOf(board[i]));
            }
        }

        moveLabel.setText("Moves: " + moves);
        timerLabel.setText("Time: " + seconds + " sec");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PuzzleGame::new);
    }
}
