import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class NumberGuessingGame extends JPanel {
    private final GameFrame gameFrame;
    private int randomNumber;
    private int score;
    private int attempts;
    private int maxNumber;
    private JLabel scoreLabel;
    private JLabel messageLabel;
    private JTextField inputField;
    private JButton enterButton;
    private JButton restartButton;
    private JComboBox<String> difficultyComboBox;

    public NumberGuessingGame(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeGame();
        createUI();
    }

    private void initializeGame() {
        maxNumber = 100; // Default to easy level
        randomNumber = generateRandomNumber();
        score = 0;
        attempts = 0;
    }

    private void createUI() {
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(scoreLabel);

        messageLabel = new JLabel("Select Difficulty and Guess a number!");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        messageLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(messageLabel);

        String[] difficulties = {"Easy (1-100)", "Medium (1-300)", "Hard (1-500)"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        difficultyComboBox.setAlignmentX(CENTER_ALIGNMENT);
        difficultyComboBox.addActionListener(e -> setDifficulty());
        add(difficultyComboBox);

        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 20));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        add(inputField);

        enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Arial", Font.BOLD, 20));
        enterButton.setAlignmentX(CENTER_ALIGNMENT);
        enterButton.addActionListener(new EnterButtonListener());
        add(enterButton);

        restartButton = new JButton("Restart Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setAlignmentX(CENTER_ALIGNMENT);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setVisible(false);
        add(restartButton);
    }

    private void setDifficulty() {
        String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
        switch (selectedDifficulty) {
            case "Easy (1-100)":
                maxNumber = 100;
                break;
            case "Medium (1-300)":
                maxNumber = 300;
                break;
            case "Hard (1-500)":
                maxNumber = 500;
                break;
        }
        restartGame();
    }

    private class EnterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int userGuess = Integer.parseInt(inputField.getText());
                attempts++;
                if (userGuess < 1 || userGuess > maxNumber) {
                    messageLabel.setText("Out of Range! Try again.");
                } else if (userGuess == randomNumber) {
                    score += Math.max(0, 10 - attempts); // Score decreases with more attempts
                    scoreLabel.setText("Score: " + score);
                    messageLabel.setText("Correct! The number was " + randomNumber);
                    enterButton.setEnabled(false);
                    restartButton.setVisible(true);
                } else if (userGuess < randomNumber) {
                    messageLabel.setText("Too Low! Try again.");
                } else {
                    messageLabel.setText("Too High! Try again.");
                }
                inputField.setText("");
            } catch (NumberFormatException ex) {
                messageLabel.setText("Please enter a valid number.");
            }
        }
    }

    private void restartGame() {
        randomNumber = generateRandomNumber();
        attempts = 0;
        inputField.setText("");
        enterButton.setEnabled(true);
        messageLabel.setText("Guess a number between 1 and " + maxNumber);
        restartButton.setVisible(false);
    }

    private int generateRandomNumber() {
        return new Random().nextInt(maxNumber) + 1; // Random number based on maxNumber
    }
}

class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Number Guessing Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new NumberGuessingGame(this));
        setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}