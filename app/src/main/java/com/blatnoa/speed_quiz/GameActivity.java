package com.blatnoa.speed_quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.blatnoa.speed_quiz.Controllers.QuestionManager;
import com.blatnoa.speed_quiz.Models.Question;

import java.sql.Time;
import java.time.Instant;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    private final int START_TIMER = 3;

    private TextView player1Name;
    private TextView player2Name;
    private TextView player1Question;
    private TextView player2Question;
    private Button player1AnswerButton;
    private Button player2AnswerButton;
    private Button stopGameButton;
    private Button replayButton;

    private Bundle extras;
    private Handler handler;
    private QuestionManager manager;

    private boolean gameRunning;
    private Runnable questionRunnable;
    private Question currentQuestion;
    private float questionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Test questions
        new Question("Est-ce un jeudi aujourd'hui", 0);
        new Question("L'atterisage lunaire à eu lieu en 1966", 0);
        new Question("AZERTY est un bon layout de clavier", 0);
        new Question("La première guerre mondial à durée de 1914 à 1918", 1);

        // Set to full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        extras = getIntent().getExtras();

        player1Name = findViewById(R.id.game_name_player1);
        player2Name = findViewById(R.id.game_name_player2);
        player1Question = findViewById(R.id.game_question_player1);
        player1Question.setText("");
        player2Question = findViewById(R.id.game_question_player2);
        player2Question.setText("");

        player1AnswerButton = findViewById(R.id.game_button_player1);
        player1AnswerButton.setEnabled(false);
        player2AnswerButton = findViewById(R.id.game_button_player2);
        player2AnswerButton.setEnabled(false);
        stopGameButton = findViewById(R.id.game_button_stop);
        replayButton = findViewById(R.id.game_button_replay);
    }

    @Override
    protected void onStart() {
        super.onStart();

        player1Name.setText(extras.getString("Player1"));
        player2Name.setText(extras.getString("Player2"));
        questionTime = extras.getFloat("DisplayTime");

        player1AnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        player2AnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Start game
        game();
    }

    private void game() {
        questionCycle();
    }

    private void questionCycle() {
        handler = new Handler();
        manager = new QuestionManager();

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
                        // Wait for x time
                        handler.postDelayed(this, (long)(questionTime * 1000));
                    } else {
                        handler.removeCallbacks(this);
                    }
            }
        };

        handler.postDelayed(questionRunnable, START_TIMER);

    }

    private void endGame(String winner) {
        handler.removeCallbacks(questionRunnable);
    }


}