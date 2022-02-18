package com.blatnoa.speed_quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;
import com.blatnoa.speed_quiz.Models.Question;
import com.google.android.material.snackbar.Snackbar;

public class GameActivity extends AppCompatActivity {

    private final int START_TIMER_MS = 3000;

    private ConstraintLayout gameLayout;
    private ConstraintLayout winnerOverlay;
    private TextView player1Name;
    private TextView player2Name;
    private TextView player1ScoreText;
    private TextView player2ScoreText;
    private TextView player1Question;
    private TextView player2Question;
    private TextView gameWinner;
    private Button player1AnswerButton;
    private Button player2AnswerButton;
    private Button stopGameButton;
    private Button replayButton;
    private Button menuButton;

    private Bundle extras;
    private Runnable questionRunnable;
    private Runnable waitForNextQuestionRunnable;
    private Handler handler;
    private QuestionManager manager;
    private Question currentQuestion;
    private float questionTimeSeconds;
    private int player1Score;
    private int player2Score;
    private int winRequirement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Test questions
        new Question("L'anglais est la langue la plus utilisé au monde.", 0);
        new Question("La distance Terre-Soleil est d'environ 150 mille kilomètres", 0);
        new Question("Le diamètre de la Terre est d'environ 12'000 kilomètres", 1);
        new Question("Freddie Mercury s'appelait Farrokh Bulsara lors de sa naissance.", 1);
        new Question("John f. Kennedy s'est fait assasiner le 22 novembre 1963.", 1);
        new Question("Horizon Forbidden West est développé par Guerilla.", 1);
        new Question("Le temps UNIX atteindra sa limite en 2038.", 1);
        new Question("L'atterisage lunaire à eu lieu en 1966.", 0);
        new Question("AZERTY est un bon layout de clavier.", 0);
        new Question("La première guerre mondial à durée de 1914 à 1918.", 1);

        // Set to full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        extras = getIntent().getExtras();

        gameLayout = findViewById(R.id.game_layout);
        winnerOverlay = findViewById(R.id.game_winner_overlay);

        player1Name = findViewById(R.id.game_name_player1);
        player2Name = findViewById(R.id.game_name_player2);
        player1ScoreText = findViewById(R.id.game_points_player1);
        player2ScoreText = findViewById(R.id.game_points_player2);
        player1Question = findViewById(R.id.game_question_player1);
        player1Question.setText("");
        player2Question = findViewById(R.id.game_question_player2);
        player2Question.setText("");
        gameWinner = findViewById(R.id.game_winner);

        player1AnswerButton = findViewById(R.id.game_button_player1);
        player1AnswerButton.setEnabled(false);
        player2AnswerButton = findViewById(R.id.game_button_player2);
        player2AnswerButton.setEnabled(false);
        stopGameButton = findViewById(R.id.game_button_stop);
        replayButton = findViewById(R.id.game_button_replay);
        menuButton = findViewById(R.id.game_button_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        player1Name.setText(extras.getString("Player1"));
        player2Name.setText(extras.getString("Player2"));
        questionTimeSeconds = extras.getFloat("DisplayTime");
        winRequirement = extras.getInt("WinRequirement");

        gameInitialization();

        player1AnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsSetEnabled(false);
                // If the questions answer is in fact true :
                if (manager.isCorrectAnswer(currentQuestion, 1)) {
                    // Add one point to the players score
                    player1Score++;
                    if (player1Score >= winRequirement) {
                        endGame(extras.getString("Player1"));
                    }
                } else if (player1Score > 0) { // Else if the player has more than 0 points
                    // Remove 1 point
                    player1Score--;
                }
                player1ScoreText.setText(Integer.toString(player1Score));
            }
        });

        player2AnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsSetEnabled(false);
                // If the questions answer is in fact true :
                if (manager.isCorrectAnswer(currentQuestion, 1)) {
                    // Add one point to the players score
                    player2Score++;
                    if (player2Score >= winRequirement) {
                        endGame(extras.getString("Player2"));
                    }
                } else if (player2Score > 0) { // Else if the player has more than 0 points
                    // Remove 1 point
                    player2Score--;
                }
                player2ScoreText.setText(Integer.toString(player2Score));
            }
        });

        stopGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(gameLayout, R.string.game_abandon_match, Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.game_confirm, new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         finish();
                     }
                });

                snackbar.show();
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInitialization();
                questionCycle();
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Start game
        questionCycle();
    }

    /**
     * Set the anwser buttons enabled state
     * @param enabled Whether the buttons should be enabled or disabled
     */
    public void buttonsSetEnabled(boolean enabled) {
        player1AnswerButton.setEnabled(enabled);
        player2AnswerButton.setEnabled(enabled);
    }

    /**
     * Initialize everything needed for a new game
     */
    public void gameInitialization() {
        player1Score = 0;
        player2Score = 0;

        player1Question.setText("");
        player2Question.setText("");

        player1ScoreText.setText("0");
        player2ScoreText.setText("0");

        buttonsSetEnabled(false);

        winnerOverlay.setVisibility(View.GONE);

        manager = new QuestionManager();
    }

    /**
     * Cycle through all questions randomly on a thread
     */
    private void questionCycle() {
        handler = new Handler();

        waitForNextQuestionRunnable = new Runnable() {
            @Override
            public void run() {
                // Deactivate buttons
                buttonsSetEnabled(false);
                // Empty text in question fields
                player1Question.setText("");
                player2Question.setText("");
                // Start next question with random delay between 1 and 5 seconds
                int randTimeMS = (int)(Math.random() * 4000 + 1000);
                handler.postDelayed(questionRunnable, randTimeMS);
            }
        };

         questionRunnable = new Runnable() {
            @Override
            public void run() {
                    // If there are any questions left :
                    if (manager.anyQuestionsRemaining()) {
                        // Get a random question
                        currentQuestion = manager.getRandomQuestion();
                        // Set question text
                        player1Question.setText(currentQuestion.getQuestion());
                        player2Question.setText(currentQuestion.getQuestion());
                        // Enable buttons
                        buttonsSetEnabled(true);
                        // Wait for x time
                        handler.postDelayed(waitForNextQuestionRunnable, (long)(questionTimeSeconds * 1000));
                    } else {
                        endGame(getWinner());
                    }
            }
        };

        handler.postDelayed(questionRunnable, START_TIMER_MS);

    }

    /**
     * End the game and show the winner
     * @param winner The name of the winner
     */
    private void endGame(String winner) {
        // Stop question cycle
        handler.removeCallbacks(questionRunnable);
        // Set name of winner in textview
        gameWinner.setText(winner);
        // Activate winner overlay
        winnerOverlay.setVisibility(View.VISIBLE);
    }

    /**
     * Get the winning player of the game.
     * @return The name of the winning player. Or tie message when both players have the same score
     */
    private String getWinner() {
        if (player1Score > player2Score) {
            return extras.getString("Player1");
        }

        if (player2Score > player1Score) {
            return extras.getString("Player2");
        }

        return getString(R.string.game_draw);
    }

}