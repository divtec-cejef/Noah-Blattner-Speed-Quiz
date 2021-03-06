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
    private TextView countdownText;
    private Button player1AnswerButton;
    private Button player2AnswerButton;
    private Button stopGameButton;
    private Button replayButton;
    private Button menuButton;

    private Bundle extras;
    private Runnable questionRunnable;
    private Runnable waitForNextQuestionRunnable;
    private Runnable countdownRunnable;
    private Handler handler;
    private QuestionManager manager;
    private Question currentQuestion;
    private float questionTimeSeconds;
    private int player1Score;
    private int player2Score;
    private int winRequirement;
    private int round;
    private int maxRounds;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
        countdownText = findViewById(R.id.countdown_text);

        player1AnswerButton = findViewById(R.id.game_button_player1);
        player1AnswerButton.setEnabled(false);
        player2AnswerButton = findViewById(R.id.game_button_player2);
        player2AnswerButton.setEnabled(false);
        stopGameButton = findViewById(R.id.game_button_stop);
        stopGameButton.setVisibility(View.GONE);
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
        maxRounds = extras.getInt("MaxRounds");

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
                         quitActivity();
                     }
                });

                snackbar.show();
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInitialization();
                startGameWithCountdown(START_TIMER_MS);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitActivity();
            }
        });

        // Start game
        startGameWithCountdown(START_TIMER_MS);
    }

    private void quitActivity() {
        manager.deleteAllCustomEntries();
        finish();
    }

    private void startGameWithCountdown(int countdownTime) {
        handler = new Handler();

        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (time <= START_TIMER_MS) {
                    countdownText.setText(Integer.toString((START_TIMER_MS - time) / 1000));
                    time += 1000;
                    handler.postDelayed(countdownRunnable, 1000);
                } else {
                    countdownText.setVisibility(View.GONE);
                    stopGameButton.setVisibility(View.VISIBLE);
                    questionCycle();
                }
            }
        };

        handler.post(countdownRunnable);
    }

    /**
     * Set the anwser buttons enabled state
     * @param enabled Whether the buttons should be enabled or disabled
     */
    private void buttonsSetEnabled(boolean enabled) {
        player1AnswerButton.setEnabled(enabled);
        player2AnswerButton.setEnabled(enabled);
    }

    /**
     * Initialize everything needed for a new game
     */
    private void gameInitialization() {
        player1Score = 0;
        player2Score = 0;

        player1Question.setText("");
        player2Question.setText("");

        player1ScoreText.setText("0");
        player2ScoreText.setText("0");

        stopGameButton.setVisibility(View.GONE);
        countdownText.setVisibility(View.VISIBLE);
        time = 0;
        round = 0;

        buttonsSetEnabled(false);

        winnerOverlay.setVisibility(View.GONE);

        manager = new QuestionManager(GameActivity.this);
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
                round++;
                    // If there are any questions left :
                    if (manager.anyQuestionsRemaining() && round <= maxRounds) {
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

        handler.postDelayed(questionRunnable,0);

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